package Models.Bean;

import java.sql.Date;

public class ConversionHistory {
    public int id;
    public int user_id;
    public String pdf_path;
    public String docx_path;
    public int status;
    public Date conversion_time;

    // Default constructor
    public ConversionHistory() {
    }

    public ConversionHistory(int id, int id_u, String pdf_path, String docx_path, int status, Date conversion_time) {
        this.id = id;
        this.user_id = id_u;
        this.pdf_path = pdf_path;
        this.docx_path = docx_path;
        this.status = status;
        this.conversion_time = conversion_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int id_u) {
        this.user_id = id_u;
    }

    public String getPdfPath() {
        return pdf_path;
    }

    public void setPdfPath(String pdf_path) {
        this.pdf_path = pdf_path;
    }

    public String getDocxPath() {
        return docx_path;
    }

    public void setDocxPath(String docx_path) {
        this.docx_path = docx_path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getConversionTime() {
        return conversion_time;
    }

    public void setConversionTime(Date conversion_time) {
        this.conversion_time = conversion_time;
    }
}
