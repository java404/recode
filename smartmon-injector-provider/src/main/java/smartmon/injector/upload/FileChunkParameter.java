package smartmon.injector.upload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileChunkParameter {
  private long chunks;
  private long chunk;
  private String path;
  private String name;
  private byte[] bytes;
}
