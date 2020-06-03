package smartmon.oracle.types;

import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.ListUtils;

@Data
public class OraInventoryHome {
  private String name;
  private String loc;
  private String type;
  private int idx;
  private boolean crs;
  private List<String> nodeNames;

  public String print() {
    return String.format("%s : (%s) %s/%s %d: %s", name, loc, type, crs, idx,
      String.join(",", ListUtils.emptyIfNull(nodeNames)));
  }
}
