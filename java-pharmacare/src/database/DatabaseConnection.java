package database;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        connect();
    }
    
    private void connect() {
        try {
            Class.forName(DatabaseConfig.getDriverClass());
            String url = DatabaseConfig.getUrl();
            String user = DatabaseConfig.getUser();
            String password = DatabaseConfig.getPassword();
            
            System.out.println("Attempting to connect to database...");
            System.out.println("URL: " + url);
            System.out.println("User: " + user);
            
            this.connection = DriverManager.getConnection(url, user, password);
            
            if (this.connection != null && !this.connection.isClosed()) {
                System.out.println("Database connection established successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            System.err.println("Make sure mysql-connector-j-9.7.0.jar is in the lib folder");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                System.out.println("Connection lost, reconnecting...");
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Failed to check connection: " + e.getMessage());
            connect();
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
