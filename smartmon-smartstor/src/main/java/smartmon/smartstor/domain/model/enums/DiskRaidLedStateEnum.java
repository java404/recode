package smartmon.smartstor.domain.model.enums;

public enum DiskRaidLedStateEnum implements IEnum {
  ON(10, "on"), OFF(20, "off");
  private final int code;
  private final String name;

  DiskRaidLedStateEnum(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public static DiskRaidLedStateEnum getState(boolean ledOn) {
    return ledOn ? DiskRaidLedStateEnum.ON : DiskRaidLedStateEnum.OFF;
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
