package smartmon.smartstor.infra.remote.types.disk;

import lombok.Data;

@Data
public class PbDataSimpleKv {
  private String key;
  private String value;

  public String toString() {
    return key + "=" + value;
  }
}
