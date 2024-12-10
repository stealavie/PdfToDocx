package Models.BO;

import java.sql.Date;
import java.util.List;

import Models.Bean.ConversionHistory;
import Models.DAO.ConversionHistoryDAO;

public class ConversionHistoryBO {
    private ConversionHistoryDAO conversionHistoryDAO;

    public ConversionHistoryBO() {
        conversionHistoryDAO = new ConversionHistoryDAO();
    }

    // Create a new data entry
    public void storeConversion(ConversionHistory data) {
        conversionHistoryDAO.storeConversion(data);
    }

    // Retrieve data by account ID
    public List<ConversionHistory> getConversionHistory(int accountId) {
        return conversionHistoryDAO.getConversionHistory(accountId);
    }

    public int getRecordLength() {
        return conversionHistoryDAO.getLength();
    }

    public void update(int id, int status, String docx_path, Date conversion_time) {
        conversionHistoryDAO.update(id, status, docx_path, conversion_time);
    }
}
