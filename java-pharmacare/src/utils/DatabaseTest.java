package utils;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("DATABASE CONNECTION TEST");
        System.out.println("===========================================");
        
        System.out.println("\n1. Testing JDBC Driver...");
        try {
            Class.forName(DatabaseConfig.getDriverClass());
            System.out.println("   ✓ JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("   ✗ JDBC Driver NOT found!");
            System.err.println("   Make sure mysql-connector-j-9.7.0.jar is in lib folder");
            return;
        }
        
        System.out.println("\n2. Connection Parameters:");
        System.out.println("   URL: " + DatabaseConfig.getUrl());
        System.out.println("   User: " + DatabaseConfig.getUser());
        System.out.println("   Password: " + (DatabaseConfig.getPassword().isEmpty() ? "(empty)" : "(set)"));
        
        System.out.println("\n3. Testing Connection...");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUser(),
                DatabaseConfig.getPassword()
            );
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("   ✓ Connection established successfully!");
                
                System.out.println("\n4. Testing Database Query...");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DATABASE() as db, VERSION() as version");
                
                if (rs.next()) {
                    System.out.println("   ✓ Query executed successfully");
                    System.out.println("   Current Database: " + rs.getString("db"));
                    System.out.println("   MySQL Version: " + rs.getString("version"));
                }
                
                System.out.println("\n5. Checking Tables...");
                rs = stmt.executeQuery("SHOW TABLES");
                System.out.println("   Tables in database:");
                int tableCount = 0;
                while (rs.next()) {
                    System.out.println("   - " + rs.getString(1));
                    tableCount++;
                }
                
                if (tableCount == 0) {
                    System.out.println("   ⚠ WARNING: No tables found!");
                    System.out.println("   You need to import the SQL schema file.");
                } else {
                    System.out.println("   ✓ Found " + tableCount + " tables");
                }
                
                System.out.println("\n6. Checking Users Table...");
                try {
                    rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
                    if (rs.next()) {
                        int userCount = rs.getInt("count");
                        System.out.println("   ✓ Users table exists");
                        System.out.println("   Total users: " + userCount);
                        
                        if (userCount == 0) {
                            System.out.println("   ⚠ WARNING: No users in database!");
                            System.out.println("   Import the SQL file to add sample data.");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("   ✗ Users table does not exist or error: " + e.getMessage());
                }
                
                rs.close();
                stmt.close();
            }
            
        } catch (Exception e) {
            System.err.println("   ✗ Connection FAILED!");
            System.err.println("\n   Error Details:");
            System.err.println("   Message: " + e.getMessage());
            if (e instanceof java.sql.SQLException) {
                java.sql.SQLException sqlEx = (java.sql.SQLException) e;
                System.err.println("   SQLState: " + sqlEx.getSQLState());
                System.err.println("   Error Code: " + sqlEx.getErrorCode());
            }
            
            System.err.println("\n   Troubleshooting:");
            System.err.println("   1. Is MAMP MySQL running?");
            System.err.println("   2. Is MySQL running on port 3307?");
            System.err.println("   3. Does database 'pharmacare' exist?");
            System.err.println("   4. Is username 'root' with no password correct?");
            
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("\n7. Connection closed.");
                } catch (Exception e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        
        System.out.println("\n===========================================");
        System.out.println("TEST COMPLETE");
        System.out.println("===========================================");
    }
}
