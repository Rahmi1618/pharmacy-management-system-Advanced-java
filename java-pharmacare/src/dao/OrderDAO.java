package dao;

import database.DatabaseConnection;
import models.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection connection;
    
    public OrderDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    public Order create(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, pharmacy_id, medicine_id, quantity, total_price, status, delivery_address, delivery_phone, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getUserId());
            stmt.setInt(2, order.getPharmacyId());
            stmt.setInt(3, order.getMedicineId());
            stmt.setInt(4, order.getQuantity());
            stmt.setBigDecimal(5, order.getTotalPrice());
            stmt.setString(6, order.getStatus());
            stmt.setString(7, order.getDeliveryAddress());
            stmt.setString(8, order.getDeliveryPhone());
            stmt.setString(9, order.getPaymentMethod());
            stmt.setString(10, order.getNotes());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    order.setId(rs.getInt(1));
                    return order;
                }
            }
        }
        return null;
    }
    
    public Order findById(int id) throws SQLException {
        String sql = "SELECT o.*, u.username as user_name, p.name as pharmacy_name, m.name as medicine_name, m.brand as medicine_brand FROM orders o JOIN users u ON o.user_id = u.id JOIN pharmacies p ON o.pharmacy_id = p.id JOIN medicines m ON o.medicine_id = m.id WHERE o.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractOrderFromResultSet(rs);
            }
        }
        return null;
    }
    
    public List<Order> findByUser(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username as user_name, p.name as pharmacy_name, m.name as medicine_name, m.brand as medicine_brand FROM orders o JOIN users u ON o.user_id = u.id JOIN pharmacies p ON o.pharmacy_id = p.id JOIN medicines m ON o.medicine_id = m.id WHERE o.user_id = ? ORDER BY o.created_at DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        }
        return orders;
    }
    
    public List<Order> findByPharmacy(int pharmacyId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username as user_name, p.name as pharmacy_name, m.name as medicine_name, m.brand as medicine_brand FROM orders o JOIN users u ON o.user_id = u.id JOIN pharmacies p ON o.pharmacy_id = p.id JOIN medicines m ON o.medicine_id = m.id WHERE o.pharmacy_id = ? ORDER BY o.created_at DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pharmacyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        }
        return orders;
    }
    
    public List<Order> findByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username as user_name, p.name as pharmacy_name, m.name as medicine_name, m.brand as medicine_brand FROM orders o JOIN users u ON o.user_id = u.id JOIN pharmacies p ON o.pharmacy_id = p.id JOIN medicines m ON o.medicine_id = m.id WHERE o.status = ? ORDER BY o.created_at DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        }
        return orders;
    }
    
    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username as user_name, p.name as pharmacy_name, m.name as medicine_name, m.brand as medicine_brand FROM orders o JOIN users u ON o.user_id = u.id JOIN pharmacies p ON o.pharmacy_id = p.id JOIN medicines m ON o.medicine_id = m.id ORDER BY o.created_at DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        }
        return orders;
    }
    
    public boolean updateStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public int getCountByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setPharmacyId(rs.getInt("pharmacy_id"));
        order.setMedicineId(rs.getInt("medicine_id"));
        order.setQuantity(rs.getInt("quantity"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setStatus(rs.getString("status"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setDeliveryPhone(rs.getString("delivery_phone"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setNotes(rs.getString("notes"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        try {
            order.setUserName(rs.getString("user_name"));
            order.setPharmacyName(rs.getString("pharmacy_name"));
            order.setMedicineName(rs.getString("medicine_name"));
            order.setMedicineBrand(rs.getString("medicine_brand"));
        } catch (SQLException e) {}
        
        return order;
    }
}
