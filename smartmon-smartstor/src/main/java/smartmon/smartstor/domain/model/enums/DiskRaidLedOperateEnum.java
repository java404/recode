package smartmon.smartstor.domain.model.enums;

public enum DiskRaidLedOperateEnum implements IEnum {

  ON(10,"on"), OFF(20,"off");
  private final int code;
  private final String name;

  DiskRaidLedOperateEnum(int code, String name) {
    this.name = name;
    this.code = code;
  }

  public static DiskRaidLedOperateEnum getOpt(boolean ledOn) {
    return ledOn ? DiskRaidLedOperateEnum.ON : DiskRaidLedOperateEnum.OFF;
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
