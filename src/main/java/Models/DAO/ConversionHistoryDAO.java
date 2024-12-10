package Models.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Models.Bean.ConversionHistory;
import Utils.DBHelper;

public class ConversionHistoryDAO {
    public ConversionHistoryDAO() {

    }

    // Method to store a conversion
    public void storeConversion(ConversionHistory conversion) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "INSERT INTO conversions (id, user_id, pdf_path, docx_path, status, conversion_time) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, conversion.getId());
                stmt.setInt(2, conversion.getUserId());
                stmt.setString(3, conversion.getPdfPath());
                stmt.setString(4, conversion.getDocxPath());
                stmt.setInt(5, conversion.getStatus());
                stmt.setDate(6, conversion.getConversionTime());
                stmt.executeUpdate();

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get conversion history
    public List<ConversionHistory> getConversionHistory(int userId) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT * FROM conversions WHERE user_id = ?";
            List<ConversionHistory> data = new ArrayList<ConversionHistory>();
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);
                System.out.println(stmt.executeQuery());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    data.add(new ConversionHistory(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
                            rs.getInt(5), rs.getDate(6)));
                }
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // method to update the status of a conversion
    public void update(int id, int status, String docx_path, Date conversion_time) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "UPDATE conversions SET status = ?, docx_path = ?, conversion_time = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, status);
                stmt.setString(2, docx_path);
                stmt.setDate(3, conversion_time);
                stmt.setInt(4, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLength() {
        int recordCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // SQL query to get the number of records in the table
            String sql = "SELECT COUNT(*) FROM conversions";

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
