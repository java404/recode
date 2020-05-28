package smartmon.smartstor.domain.model.enums;

public enum PoolCacheModeEnum implements IEnum {

  UNKNOWN(1, "unknown"), WRITEBACK(2, "write-back"),
  WRITETHROUGH(3, "write-through"), MIX(4, "mix"),
  EXT_UNKNOWN(10, "unknown"), EXT_WRITEBACK(20, "write-back"),
  EXT_WRITETHROUGH(30, "write-through"), EXT_MIX(40, "mix");
  private final int code;
  private final String name;

  PoolCacheModeEnum(int code, String name) {
    this.name = name;
    this.code = code;
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
