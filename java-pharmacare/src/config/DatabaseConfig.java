package config;

public class DatabaseConfig {
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3307";
    private static final String DB_NAME = "pharmacare";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    public static String getUrl() {
        return "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + 
               "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    }
    
    public static String getUser() {
        return DB_USER;
    }
    
    public static String getPassword() {
        return DB_PASSWORD;
    }
    
    public static String getDriverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }
}
