package smartmon.falcon.remote.types.alarm;

import java.util.List;
import lombok.Data;

@Data
public class FalconEventCaseDeleteParam {
  private List<String> alarms;
}
