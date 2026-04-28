
package pharmacy.system;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/pharmacy_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Try with empty password
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found");
        } catch (SQLException e) {
            // If empty password fails, try without password parameter
            if (e.getMessage().contains("Access denied")) {
                return DriverManager.getConnection(URL, USERNAME, null);
            }
            throw e;
        }
    }
}