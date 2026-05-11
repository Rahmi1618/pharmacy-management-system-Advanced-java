package pharmacy.system;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    
    public void addSale(Sale sale) throws SQLException {
        String sql = "INSERT INTO sales (customer_name, total_amount, payment_method) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, sale.getCustomerName());
            pstmt.setBigDecimal(2, sale.getTotalAmount());
            pstmt.setString(3, sale.getPaymentMethod());
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int saleId = rs.getInt(1);
                    String itemSql = "INSERT INTO sale_items (sale_id, medicine_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                        for (SaleItem item : sale.getItems()) {
                            itemStmt.setInt(1, saleId);
                            itemStmt.setInt(2, item.getMedicineId());
                            itemStmt.setInt(3, item.getQuantity());
                            itemStmt.setBigDecimal(4, item.getUnitPrice());
                            itemStmt.setBigDecimal(5, item.getSubtotal());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }
                }
            }
        }
    }
    
    public List<Sale> getAllSales() throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setSaleId(rs.getInt("sale_id"));
                sale.setSaleDate(rs.getTimestamp("sale_date"));
                sale.setCustomerName(rs.getString("customer_name"));
                sale.setTotalAmount(rs.getBigDecimal("total_amount"));
                sale.setPaymentMethod(rs.getString("payment_method"));
                sales.add(sale);
            }
        }
        return sales;
    }
    
    public BigDecimal getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(total_amount) as total FROM sales";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal("total") != null ? rs.getBigDecimal("total") : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
}
