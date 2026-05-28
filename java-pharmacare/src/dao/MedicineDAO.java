package dao;

import database.DatabaseConnection;
import models.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {
    private Connection connection;
    
    public MedicineDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    public Medicine create(Medicine medicine) throws SQLException {
        String sql = "INSERT INTO medicines (name, brand, category, description, image, requires_prescription, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medicine.getName());
            stmt.setString(2, medicine.getBrand());
            stmt.setString(3, medicine.getCategory());
            stmt.setString(4, medicine.getDescription());
            stmt.setString(5, medicine.getImage());
            stmt.setBoolean(6, medicine.isRequiresPrescription());
            stmt.setDate(7, medicine.getExpiryDate());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    medicine.setId(rs.getInt(1));
                    return medicine;
                }
            }
        }
        return null;
    }
    
    public Medicine findById(int id) throws SQLException {
        String sql = "SELECT * FROM medicines WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractMedicineFromResultSet(rs);
            }
        }
        return null;
    }
    
    public List<Medicine> findAll() throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }
        }
        return medicines;
    }
    
    public List<Medicine> search(String query) throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE name LIKE ? OR brand LIKE ? OR category LIKE ? ORDER BY name";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }
        }
        return medicines;
    }
    
    public List<Medicine> findByCategory(String category) throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE category = ? ORDER BY name";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }
        }
        return medicines;
    }
    
    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM medicines WHERE category IS NOT NULL ORDER BY category";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }
    
    public List<Medicine> findExpiringSoon(int days) throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE expiry_date <= DATE_ADD(CURDATE(), INTERVAL ? DAY) AND expiry_date >= CURDATE() ORDER BY expiry_date";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }
        }
        return medicines;
    }
    
    public boolean update(Medicine medicine) throws SQLException {
        String sql = "UPDATE medicines SET name = ?, brand = ?, category = ?, description = ?, image = ?, requires_prescription = ?, expiry_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medicine.getName());
            stmt.setString(2, medicine.getBrand());
            stmt.setString(3, medicine.getCategory());
            stmt.setString(4, medicine.getDescription());
            stmt.setString(5, medicine.getImage());
            stmt.setBoolean(6, medicine.isRequiresPrescription());
            stmt.setDate(7, medicine.getExpiryDate());
            stmt.setInt(8, medicine.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM medicines WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM medicines";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Medicine extractMedicineFromResultSet(ResultSet rs) throws SQLException {
        Medicine medicine = new Medicine();
        medicine.setId(rs.getInt("id"));
        medicine.setName(rs.getString("name"));
        medicine.setBrand(rs.getString("brand"));
        medicine.setCategory(rs.getString("category"));
        medicine.setDescription(rs.getString("description"));
        medicine.setImage(rs.getString("image"));
        medicine.setRequiresPrescription(rs.getBoolean("requires_prescription"));
        medicine.setExpiryDate(rs.getDate("expiry_date"));
        medicine.setCreatedAt(rs.getTimestamp("created_at"));
        return medicine;
    }
}
