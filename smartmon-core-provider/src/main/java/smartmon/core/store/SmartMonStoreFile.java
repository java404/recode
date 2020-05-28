package smartmon.core.store;

import java.util.Date;

import lombok.Data;

@Data
public class SmartMonStoreFile {
  private long fileId;

  private String originalFilename;

  private String localFilename;
  private Long localFileSize;
  private Date timestamp;

  private String type;
  private String status;
  private String errorMessage;
}
