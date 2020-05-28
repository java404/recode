package smartmon.falcon.monitor.types;

import lombok.Data;

@Data
public class CallBackParam {
  private String endpoint;
  private String metric;
  private String step;
  private String priority;
  private String time;
  private String note;
  private String status;
  private String tplId;
  private String expId;
  private String strategyId;
  private String tags;
  private String leftValue;
}
