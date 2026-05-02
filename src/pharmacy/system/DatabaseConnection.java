package pharmacy.system;

import java.sql.*;

/**
 * DatabaseConnection class handles all MySQL database connectivity
 * @author Member 1
 * @version 1.0
 */
public class DatabaseConnection {
    
    // Database connection parameters
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "pharmacy_db";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";  // XAMPP default is empty
    
    /**
     * Establishes and returns a connection to the MySQL database
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Try with empty password first
            try {
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✓ Database connected successfully!");
                return connection;
            } catch (SQLException e) {
                // If empty password fails, try with null password
                if (e.getMessage().contains("Access denied")) {
                    Connection connection = DriverManager.getConnection(URL, USERNAME, null);
                    System.out.println("✓ Database connected successfully with null password!");
                    return connection;
                }
                throw e;
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver not found!");
            System.err.println("Please add mysql-connector-java.jar to classpath");
            throw new SQLException("MySQL JDBC Driver not found. Please add the connector JAR file.", e);
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Tests the database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Closes a database connection safely
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Closes all database resources safely
     * @param conn Connection to close
     * @param stmt Statement to close
     * @param rs ResultSet to close
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closeConnection(conn);
    }
}
