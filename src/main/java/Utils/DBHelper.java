package Utils;

import java.sql.*;

public class DBHelper {

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fileconversiondb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    // Load the MySQL JDBC Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found");
        }
    }

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        try {
            // check if the database exists
            createDatabase();
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to connect to the database");
        }
    }

    // Method to create the database
    private static void createDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", DB_USERNAME, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS fileconversiondb";
            stmt.executeUpdate(sql);
            String sql2 = "USE fileconversiondb";
            stmt.executeUpdate(sql2);
            String sql3 = "CREATE TABLE IF NOT EXISTS conversions (id INT PRIMARY KEY, user_id INT, pdf_path TEXT, docx_path TEXT, status INT, conversion_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(sql3);
            String sql4 = "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, username TEXT, password TEXT)";
            stmt.executeUpdate(sql4);
            // check if the users table is empty
            String sql6 = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(sql6);
            if (!rs.next()) {
                String sql5 = "INSERT INTO users (id, username, password) VALUES (1, 'pda', '12345678'), (2, 'nvb', '12345678')";
                stmt.executeUpdate(sql5);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Close the database resources (Connection, Statement, ResultSet)
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
