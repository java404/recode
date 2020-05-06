package smartmon.falcon.graph.model;

public enum MergerTypeEnum {
  AVERAGE(0, "average"), MAX(1, "max"), MIN(2, "min");

  private int code;
  private String name;

  MergerTypeEnum(int code, String name) {
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
