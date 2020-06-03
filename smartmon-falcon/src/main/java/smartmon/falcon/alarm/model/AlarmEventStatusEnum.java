package smartmon.falcon.alarm.model;

public enum AlarmEventStatusEnum {
  PROBLEM(0), RECOVER(1);

  private final Integer status;

  AlarmEventStatusEnum(Integer status) {
    this.status = status;
  }

  static AlarmEventStatusEnum fromStatus(String status) {
    for (AlarmEventStatusEnum alarmEventStatusEnum : AlarmEventStatusEnum.values()) {
      if (alarmEventStatusEnum.getStatus().toString().equals(status)) {
        return alarmEventStatusEnum;
      }
    }
    return null;
  }

  public Integer getStatus() {
    return status;
  }
}
