package Models.DAO;

import java.sql.*;

import Models.Bean.Account;
import Utils.DBHelper;

public class AccountDAO {
    public AccountDAO() {

    }

    // Get account by username logon
    public Account authenticateUser(String username, String password) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create new account
    public boolean createAccount(Account account) {
        String query = "INSERT INTO users (id, username, password) VALUES (?, ?, ?)";
        Connection connection = null;
        try {
            connection = DBHelper.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, Integer.toString(account.getId()));
            stmt.setString(2, account.getUsername());
            stmt.setString(3, account.getPassword());
            boolean result = stmt.executeUpdate() > 0;
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(connection);
        }
        return false;
    }

    public int getLength() {
        int recordCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // SQL query to get the number of records in the table
            String sql = "SELECT COUNT(*) FROM users";

            connection = DBHelper.getConnection();

            // Create a PreparedStatement to execute the query
            preparedStatement = connection.prepareStatement(sql);

            // Execute the query and get the result
            resultSet = preparedStatement.executeQuery();

            // Retrieve the count of records
            if (resultSet.next()) {
                recordCount = resultSet.getInt(1); // The first column contains the count
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        } finally {
            // Close resources to avoid memory leaks
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    DBHelper.close(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return recordCount;
    }
}
