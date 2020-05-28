package smartmon.smartstor.domain.model.enums;

public enum RaidTypeEnum implements IEnum {
  MEGARAID(10, "MEGARAID"), SAS2RAID(20, "SAS2RAID"), HPSARAID(30, "HPSARAID");
  private final int code;
  private final String name;

  RaidTypeEnum(int code, String name) {
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
