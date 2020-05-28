package smartmon.smartstor.domain.model.enums;

public enum LunTypeEnum implements IEnum {
  LUN_TYPE_BASEDEV(10, "basedev"), LUN_TYPE_BASEDISK(20, "basedisk"),
  LUN_TYPE_PALCACHE(30, "palcache"), LUN_TYPE_PALPMT(40, "palpmt"),
  LUN_TYPE_PALRAW(50, "palraw");

  private int code;
  private String name;

  LunTypeEnum(int code, String name) {
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
