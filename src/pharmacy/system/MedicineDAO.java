package pharmacy.system;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {
    
    public void addMedicine(Medicine medicine) throws SQLException {
        String sql = "INSERT INTO medicines (name, category, manufacturer, price, stock_quantity, expiry_date, prescription_required) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, medicine.getName());
            pstmt.setString(2, medicine.getCategory());
            pstmt.setString(3, medicine.getManufacturer());
            pstmt.setBigDecimal(4, medicine.getPrice());
            pstmt.setInt(5, medicine.getStockQuantity());
            pstmt.setDate(6, new java.sql.Date(medicine.getExpiryDate().getTime()));
            pstmt.setBoolean(7, medicine.isPrescriptionRequired());
            pstmt.executeUpdate();
        }
    }
    
    public List<Medicine> getAllMedicines() throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Medicine medicine = new Medicine();
                medicine.setMedicineId(rs.getInt("medicine_id"));
                medicine.setName(rs.getString("name"));
                medicine.setCategory(rs.getString("category"));
                medicine.setManufacturer(rs.getString("manufacturer"));
                medicine.setPrice(rs.getBigDecimal("price"));
                medicine.setStockQuantity(rs.getInt("stock_quantity"));
                medicine.setExpiryDate(rs.getDate("expiry_date"));
                medicine.setPrescriptionRequired(rs.getBoolean("prescription_required"));
                medicines.add(medicine);
            }
        }
        return medicines;
    }
    
    public Medicine getMedicineById(int id) throws SQLException {
        String sql = "SELECT * FROM medicines WHERE medicine_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Medicine medicine = new Medicine();
                    medicine.setMedicineId(rs.getInt("medicine_id"));
                    medicine.setName(rs.getString("name"));
                    medicine.setPrice(rs.getBigDecimal("price"));
                    medicine.setStockQuantity(rs.getInt("stock_quantity"));
                    return medicine;
                }
            }
        }
        return null;
    }
    
    public void updateMedicine(Medicine medicine) throws SQLException {
        String sql = "UPDATE medicines SET name=?, price=?, stock_quantity=? WHERE medicine_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, medicine.getName());
            pstmt.setBigDecimal(2, medicine.getPrice());
            pstmt.setInt(3, medicine.getStockQuantity());
            pstmt.setInt(4, medicine.getMedicineId());
            pstmt.executeUpdate();
        }
    }
    
    public void deleteMedicine(int id) throws SQLException {
        String sql = "DELETE FROM medicines WHERE medicine_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    public List<Medicine> searchMedicines(String keyword) throws SQLException {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE name LIKE ? OR category LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Medicine medicine = new Medicine();
                    medicine.setMedicineId(rs.getInt("medicine_id"));
                    medicine.setName(rs.getString("name"));
                    medicines.add(medicine);
                }
            }
        }
        return medicines;
    }
    
    public void updateStock(int medicineId, int quantity) throws SQLException {
        String sql = "UPDATE medicines SET stock_quantity = stock_quantity - ? WHERE medicine_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, medicineId);
            pstmt.executeUpdate();
        }
    }
}