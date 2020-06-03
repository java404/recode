package smartmon.falcon.alarm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import smartmon.falcon.strategy.model.Strategy;
import smartmon.falcon.strategy.model.StrategyPriorityEnum;

@Data
public class Alarm {
  private String id;
  private String endpoint;
  private String metric;
  private String func;
  private String cond;
  private String note;
  private Integer step;
  private Integer currentStep;
  private String status;
  private String timestamp;
  private String updateAt;
  private String closedAt;
  private String closedNote;
  private Integer userModified;
  private String tplCreator;
  private Integer expressionId;
  private Integer strategyId;
  private Integer templateId;
  private Integer processNote;
  private String processStatus;
  private StrategyPriorityEnum priority;

  private String pdsBusinessName; //PDS 业务名称
  private String pdsBusinessUuid; //PDS UUID
  private String endpointAlias; //Endpoint 别名
  private String ip;

  @JsonIgnore
  private String events;
  private Strategy strategy;
  private AlarmTypeEnum alarmTypeEnum;
  private String alarmObject;
  private String alarmMessage;
  private String alarmTime;
  private String alarmRecoverTime;

  @JsonIgnore
  public String getThreshold() {
    return getCondItem(2);
  }

  @JsonIgnore
  public String getOp() {
    return getCondItem(1);
  }

  @JsonIgnore
  public String getValue() {
    return getCondItem(0);
  }

  private String getCondItem(Integer index) {
    if (index == null || index < 0 || index > 2) {
      return StringUtils.EMPTY;
    }
    String[] condItems = Strings.nullToEmpty(getCond()).split("\\s+");
    return condItems.length != 3 ? StringUtils.EMPTY : condItems[index];
  }
}
