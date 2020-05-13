package smartmon.falcon.remote.types.alarm;

import lombok.Data;

import java.util.List;

@Data
public class FalconEventCases {
  private Integer count;
  private List<FalconEventCase> events;
}
