package dao;

import database.DatabaseConnection;
import models.PharmacyMedicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacyMedicineDAO {
    private Connection connection;
    
    public PharmacyMedicineDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    public PharmacyMedicine create(PharmacyMedicine pm) throws SQLException {
        String sql = "INSERT INTO pharmacy_medicines (pharmacy_id, medicine_id, price, stock_quantity, availability) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pm.getPharmacyId());
            stmt.setInt(2, pm.getMedicineId());
            stmt.setBigDecimal(3, pm.getPrice());
            stmt.setInt(4, pm.getStockQuantity());
            stmt.setString(5, pm.getAvailability());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    pm.setId(rs.getInt(1));
                    return pm;
                }
            }
        }
        return null;
    }
    
    public List<PharmacyMedicine> findByPharmacy(int pharmacyId) throws SQLException {
        List<PharmacyMedicine> list = new ArrayList<>();
        String sql = "SELECT pm.*, m.name as medicine_name, m.brand as medicine_brand FROM pharmacy_medicines pm JOIN medicines m ON pm.medicine_id = m.id WHERE pm.pharmacy_id = ? ORDER BY m.name";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pharmacyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        }
        return list;
    }
    
    public List<PharmacyMedicine> findByMedicine(int medicineId) throws SQLException {
        List<PharmacyMedicine> list = new ArrayList<>();
        String sql = "SELECT pm.*, p.name as pharmacy_name FROM pharmacy_medicines pm JOIN pharmacies p ON pm.pharmacy_id = p.id WHERE pm.medicine_id = ? ORDER BY pm.price";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medicineId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        }
        return list;
    }
    
    public PharmacyMedicine findByPharmacyAndMedicine(int pharmacyId, int medicineId) throws SQLException {
        String sql = "SELECT pm.*, m.name as medicine_name, m.brand as medicine_brand, p.name as pharmacy_name FROM pharmacy_medicines pm JOIN medicines m ON pm.medicine_id = m.id JOIN pharmacies p ON pm.pharmacy_id = p.id WHERE pm.pharmacy_id = ? AND pm.medicine_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pharmacyId);
            stmt.setInt(2, medicineId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        }
        return null;
    }
    
    public List<PharmacyMedicine> findLowStock(int threshold) throws SQLException {
        List<PharmacyMedicine> list = new ArrayList<>();
        String sql = "SELECT pm.*, m.name as medicine_name, m.brand as medicine_brand, p.name as pharmacy_name FROM pharmacy_medicines pm JOIN medicines m ON pm.medicine_id = m.id JOIN pharmacies p ON pm.pharmacy_id = p.id WHERE pm.stock_quantity <= ? AND pm.stock_quantity > 0 ORDER BY pm.stock_quantity";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        }
        return list;
    }
    
    public boolean update(PharmacyMedicine pm) throws SQLException {
        String sql = "UPDATE pharmacy_medicines SET price = ?, stock_quantity = ?, availability = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, pm.getPrice());
            stmt.setInt(2, pm.getStockQuantity());
            stmt.setString(3, pm.getAvailability());
            stmt.setInt(4, pm.getId());
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateStock(int pharmacyId, int medicineId, int quantity) throws SQLException {
        String sql = "UPDATE pharmacy_medicines SET stock_quantity = stock_quantity - ?, availability = CASE WHEN stock_quantity - ? <= 0 THEN 'Out of Stock' WHEN stock_quantity - ? <= 5 THEN 'Limited Stock' ELSE 'In Stock' END WHERE pharmacy_id = ? AND medicine_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, quantity);
            stmt.setInt(3, quantity);
            stmt.setInt(4, pharmacyId);
            stmt.setInt(5, medicineId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM pharmacy_medicines WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    private PharmacyMedicine extractFromResultSet(ResultSet rs) throws SQLException {
        PharmacyMedicine pm = new PharmacyMedicine();
        pm.setId(rs.getInt("id"));
        pm.setPharmacyId(rs.getInt("pharmacy_id"));
        pm.setMedicineId(rs.getInt("medicine_id"));
        pm.setPrice(rs.getBigDecimal("price"));
        pm.setStockQuantity(rs.getInt("stock_quantity"));
        pm.setAvailability(rs.getString("availability"));
        pm.setCreatedAt(rs.getTimestamp("created_at"));
        
        try {
            pm.setMedicineName(rs.getString("medicine_name"));
            pm.setMedicineBrand(rs.getString("medicine_brand"));
        } catch (SQLException e) {}
        
        try {
            pm.setPharmacyName(rs.getString("pharmacy_name"));
        } catch (SQLException e) {}
        
        return pm;
    }
}
