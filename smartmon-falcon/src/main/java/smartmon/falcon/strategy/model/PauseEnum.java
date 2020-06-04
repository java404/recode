package smartmon.falcon.strategy.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;
import java.util.Objects;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PauseEnum {
  ENABLED(0, "enabled"), DISABLED(1, "disabled");

  private int code;
  private String name;

  PauseEnum(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public int getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  /**
   * get PauseEnum by code.
   */
  public static PauseEnum getByCode(int code) {
    return Arrays.stream(PauseEnum.values())
      .filter(enumConstant -> Objects.equals(enumConstant.getCode(), code))
      .findFirst().orElse(null);
  }
}
