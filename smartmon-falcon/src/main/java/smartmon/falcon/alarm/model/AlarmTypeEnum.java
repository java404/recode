package smartmon.falcon.alarm.model;

public enum AlarmTypeEnum {
  SYSTEM(0, "system", "节点"),
  STORAGE(1, "storage", "存储池"),
  STORAGE_CLUSTER(2, "storage-cluster", "存储集群"),
  DB_CLUSTER(3, "db-cluster", "数据库"),
  DB_MYSQL(4, "dbmysql", "MySQL数据库"),
  DB_POSTGRESQL(5, "dbpostgresql", "PostgreSQL数据库"),
  OTHERS(6, "others", "其他");

  private final Integer index;
  private final String name;
  private final String desc;

  public Integer getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public String getDesc() {
    return desc;
  }

  AlarmTypeEnum(Integer index, String name, String desc) {
    this.index = index;
    this.name = name;
    this.desc = desc;
  }

  public static AlarmTypeEnum fromName(String name) {
    for (AlarmTypeEnum alarmTypeEnum : AlarmTypeEnum.values()) {
      if (alarmTypeEnum.getName().equalsIgnoreCase(name)) {
        return alarmTypeEnum;
      }
    }
    return null;
  }
}
