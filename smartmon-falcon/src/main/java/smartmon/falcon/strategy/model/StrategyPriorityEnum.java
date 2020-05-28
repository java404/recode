package smartmon.falcon.strategy.model;

import java.util.Arrays;
import java.util.Objects;

//策略报警级别
public enum StrategyPriorityEnum {
  NOT_CLASSIFIED(5, "未分类", "Not_Classified"),
  INFORMATION(4, "信息", "Information"),
  WARNING(3, "警告", "Warning"),
  AVERAGE(2, "一般严重", "Average"),
  HIGH(1, "严重", "High"),
  DISASTER(0, "灾难", "Disaster");

  private Integer index;
  private String desc;
  private String name;

  StrategyPriorityEnum(Integer index, String desc, String name) {
    this.index = index;
    this.desc = desc;
    this.name = name;
  }

  public Integer getIndex() {
    return this.index;
  }

  public String getDesc() {
    return this.desc;
  }

  public String getName() {
    return this.name;
  }

  public static StrategyPriorityEnum getByIndex(Integer index) {
    return Arrays.stream(StrategyPriorityEnum.values())
      .filter(enumConstant -> Objects.equals(enumConstant.getIndex(), index))
      .findFirst().orElse(null);
  }
}
