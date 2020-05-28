package smartmon.core.store;

import lombok.Data;

@Data
public class SmartMonStoreInputFile {
  private String type;
  private String originalFilename;
  private String localFilename;
  private long localFileSize;
}
