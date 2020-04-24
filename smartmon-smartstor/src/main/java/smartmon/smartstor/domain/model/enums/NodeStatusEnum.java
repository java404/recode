package smartmon.smartstor.domain.model.enums;

public enum NodeStatusEnum implements IEnum {
  UNCONFIGURED(1, "unconfigured"), CONFIGURED(2, "configured"), MISSING(3, "missing");

  private int code;
  private String name;

  NodeStatusEnum(int code, String name) {
    this.code = code;
    this.name = name;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getName() {
    return name;
  }
}
