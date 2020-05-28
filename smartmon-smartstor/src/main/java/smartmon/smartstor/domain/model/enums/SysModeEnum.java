package smartmon.smartstor.domain.model.enums;

public enum SysModeEnum implements IEnum {
  MERGE(10, "merge"), STORAGE(20, "storage"), DATABASE(30, "database");
  private final int code;
  private final String name;

  SysModeEnum(int code, String name) {
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

  public static boolean isIos(SysModeEnum sysMode) {
    return SysModeEnum.STORAGE == sysMode || SysModeEnum.MERGE == sysMode;
  }

  public static boolean isBac(SysModeEnum sysMode) {
    return SysModeEnum.DATABASE == sysMode || SysModeEnum.MERGE == sysMode;
  }
}
