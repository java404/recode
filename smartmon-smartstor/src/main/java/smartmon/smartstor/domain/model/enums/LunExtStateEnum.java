package smartmon.smartstor.domain.model.enums;

public enum LunExtStateEnum implements IEnum {
  UNDEFINE(10, "未配置"), OFFLINE(20, "OFFLINE"),
  ONLINE(30, "ONLINE"), ACTIVE(40, "ACTIVE"),
  INACTIVE(50, "INACTIVE"), SYNCING(60, "SYNCING"),
  DROPPING(70, "DROPPING"), MISSING(80, "MISSING");

  private int code;
  private String name;

  LunExtStateEnum(int code, String name) {
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
