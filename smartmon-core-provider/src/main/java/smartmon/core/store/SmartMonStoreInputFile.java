package smartmon.core.store;

import java.io.Serializable;
import lombok.Data;

@Data
public class SmartMonStoreInputFile implements Serializable {
  private static final long serialVersionUID = 4451533820832807322L;

  private String type;
  private String desc;
  private String originalFilename;
  private String localFilename;
  private long localFileSize;
}
