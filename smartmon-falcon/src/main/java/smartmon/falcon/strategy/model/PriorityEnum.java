package smartmon.falcon.strategy.model;

import java.util.Arrays;
import java.util.Objects;

public enum PriorityEnum {
  DISASTER(0, "Disaster"),
  SERIOUS(1, "Serious"),
  GENERALLY_SERIOUS(2, "GenerallySerious"),
  WARNING(3, "Warning");

  private int code;
  private String name;

  PriorityEnum(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public int getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public static PriorityEnum getByCode(int code) {
    return Arrays.stream(PriorityEnum.values())
      .filter(enumConstant -> Objects.equals(enumConstant.getCode(), code))
      .findFirst().orElse(null);
  }
}
