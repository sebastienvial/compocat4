package bobst.catalog.compoCat4.models;

import java.util.UUID;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CatLog {

    @Id
    @GeneratedValue
    private UUID logId;
    private Date logDate;
    private String fileName;
    private String status;
    
    public CatLog(Date logDate, String fileName, String status) {
        this.logDate = logDate;
        this.fileName = fileName;
        this.status = status;
    }

    public UUID getLogId() {
        return logId;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
