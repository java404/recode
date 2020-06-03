package smartmon.falcon.remote.test;

import smartmon.falcon.strategy.model.StrategyPriorityEnum;

import java.util.Arrays;
import java.util.Objects;

public enum C {
  HDD(10, "HDD"), STORAGE(20, "SSD");
  private final int code;
  private final String name;

  C(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public int getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public static C getByCode(int code) {
    return Arrays.stream(C.values())
      .filter(enumConstant -> Objects.equals(enumConstant.getCode(), code))
      .findFirst().orElse(null);
  }
}
