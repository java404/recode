package smartmon.falcon.alarm.model;

import java.util.Arrays;
import java.util.Objects;

public enum EventNoteStatusEnum {
  IN_PROGRESS("in progress","处理中"),
  UNRESOLVED("unresolved","未处理"),
  RESOLVED("resolved","已处理"),
  IGNORED("ignored","已忽略"),
  COMMENT("comment","注解");

  private String statusName;
  private String statusCnName;

  EventNoteStatusEnum(String statusName, String statusCnName) {
    this.statusName = statusName;
    this.statusCnName = statusCnName;
  }

  public String getStatusName() {
    return this.statusName;
  }

  public String getStatusCnName() {
    return this.statusCnName;
  }

  /**
   * Get EventNoteStatusEnum By Status Name.
   */
  public static EventNoteStatusEnum getByStatusName(String statusName) {
    return Arrays.stream(EventNoteStatusEnum.values())
      .filter(enumConstant -> Objects.equals(enumConstant.getStatusName(), statusName))
      .findFirst().orElse(null);
  }
}
