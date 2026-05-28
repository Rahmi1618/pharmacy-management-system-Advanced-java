package dao;

import database.DatabaseConnection;
import models.Pharmacy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacyDAO {
    private Connection connection;
    
    public PharmacyDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    public Pharmacy create(Pharmacy pharmacy) throws SQLException {
        String sql = "INSERT INTO pharmacies (name, address, phone, email, latitude, longitude, rating, is_24_hours, description, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pharmacy.getName());
            stmt.setString(2, pharmacy.getAddress());
            stmt.setString(3, pharmacy.getPhone());
            stmt.setString(4, pharmacy.getEmail());
            stmt.setBigDecimal(5, pharmacy.getLatitude());
            stmt.setBigDecimal(6, pharmacy.getLongitude());
            stmt.setBigDecimal(7, pharmacy.getRating());
            stmt.setBoolean(8, pharmacy.isIs24Hours());
            stmt.setString(9, pharmacy.getDescription());
            stmt.setString(10, pharmacy.getImage());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    pharmacy.setId(rs.getInt(1));
                    return pharmacy;
                }
            }
        }
        return null;
    }
    
    public Pharmacy findById(int id) throws SQLException {
        String sql = "SELECT * FROM pharmacies WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractPharmacyFromResultSet(rs);
            }
        }
        return null;
    }
    
    public List<Pharmacy> findAll() throws SQLException {
        List<Pharmacy> pharmacies = new ArrayList<>();
        String sql = "SELECT * FROM pharmacies ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pharmacies.add(extractPharmacyFromResultSet(rs));
            }
        }
        return pharmacies;
    }
    
    public List<Pharmacy> search(String query) throws SQLException {
        List<Pharmacy> pharmacies = new ArrayList<>();
        String sql = "SELECT * FROM pharmacies WHERE name LIKE ? OR address LIKE ? ORDER BY name";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pharmacies.add(extractPharmacyFromResultSet(rs));
            }
        }
        return pharmacies;
    }
    
    public List<Pharmacy> find24HourPharmacies() throws SQLException {
        List<Pharmacy> pharmacies = new ArrayList<>();
        String sql = "SELECT * FROM pharmacies WHERE is_24_hours = TRUE ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pharmacies.add(extractPharmacyFromResultSet(rs));
            }
        }
        return pharmacies;
    }
    
    public boolean update(Pharmacy pharmacy) throws SQLException {
        String sql = "UPDATE pharmacies SET name = ?, address = ?, phone = ?, email = ?, latitude = ?, longitude = ?, rating = ?, is_24_hours = ?, description = ?, image = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pharmacy.getName());
            stmt.setString(2, pharmacy.getAddress());
            stmt.setString(3, pharmacy.getPhone());
            stmt.setString(4, pharmacy.getEmail());
            stmt.setBigDecimal(5, pharmacy.getLatitude());
            stmt.setBigDecimal(6, pharmacy.getLongitude());
            stmt.setBigDecimal(7, pharmacy.getRating());
            stmt.setBoolean(8, pharmacy.isIs24Hours());
            stmt.setString(9, pharmacy.getDescription());
            stmt.setString(10, pharmacy.getImage());
            stmt.setInt(11, pharmacy.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM pharmacies WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM pharmacies";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Pharmacy extractPharmacyFromResultSet(ResultSet rs) throws SQLException {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(rs.getInt("id"));
        pharmacy.setName(rs.getString("name"));
        pharmacy.setAddress(rs.getString("address"));
        pharmacy.setPhone(rs.getString("phone"));
        pharmacy.setEmail(rs.getString("email"));
        pharmacy.setLatitude(rs.getBigDecimal("latitude"));
        pharmacy.setLongitude(rs.getBigDecimal("longitude"));
        pharmacy.setRating(rs.getBigDecimal("rating"));
        pharmacy.setIs24Hours(rs.getBoolean("is_24_hours"));
        pharmacy.setDescription(rs.getString("description"));
        pharmacy.setImage(rs.getString("image"));
        pharmacy.setCreatedAt(rs.getTimestamp("created_at"));
        return pharmacy;
    }
}
