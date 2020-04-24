package smartmon.smartstor.domain.model.enums;

public enum DiskTypeEnum implements IEnum {
  HDD(10, "HDD"), STORAGE(20, "SSD");
  private final int code;
  private final String name;

  DiskTypeEnum(int code, String name) {
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
