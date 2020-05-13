package smartmon.falcon.remote.types.alarm;

import java.util.List;
import lombok.Data;

@Data
public class FalconEventNotes {
  private Integer count;
  private List<FalconEventNote> notes;
}
