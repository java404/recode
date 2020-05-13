package smartmon.falcon.remote.types.alarm;

import lombok.Data;

import java.util.List;

@Data
public class FalconEvents {
  private Integer count;
  private List<FalconEvent> events;
}
