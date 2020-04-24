package smartmon.smartstor.domain.model.enums;

public enum TransModeEnum implements IEnum {
  SMARTSCSI(10, "smartscsi"), SMARTNVME(20, "smartnvme");
  private final int code;
  private final String name;

  TransModeEnum(int code, String name) {
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
