package smartmon.falcon.controller.vo;

import lombok.Data;

@Data
public class StrategyUpdateVo {
  private Long id;
  private Integer pause;
  private String op;
  private String rightValue;
  private Long maxStep;
  private Integer priority;
  private String note;
}
