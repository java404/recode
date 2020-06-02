package smartmon.core.store;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class SmartMonStoreFile implements Serializable {
  private static final long serialVersionUID = -994259928051691350L;

  private long fileId;

  private String originalFilename;

  private String localFilename;
  private Long localFileSize;
  private Date timestamp;

  private String type;
  private String desc;
  private String status;
  private String errorMessage;
}
