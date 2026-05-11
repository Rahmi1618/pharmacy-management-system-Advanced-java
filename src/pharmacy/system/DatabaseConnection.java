package pharmacy.system;
import java.sql.*;
/*
 DatabaseConnection class handles all MySQL database connectivity
 */
public class DatabaseConnection {
    
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "pharmacy_db";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connected successfully!");
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found");
        }
    }
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


