package smartmon.falcon.strategy.model;

import lombok.Data;

@Data
public class Strategy {
  private Long id;
  private String metric;
  private String tags;
  private Long maxStep;
  private PriorityEnum priority;
  private String func;
  private String op;
  private String rightValue;
  private String note;
  private String runBegin;
  private String runEnd;
  private Long templateId;
  private PauseEnum pause;
}
