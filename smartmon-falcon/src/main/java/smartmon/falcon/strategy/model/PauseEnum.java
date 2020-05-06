package smartmon.falcon.strategy.model;

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
}
