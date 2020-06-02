package smartmon.falcon.alarm.model;

import lombok.Data;
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

  private String events;
  private Strategy strategy;
  private AlarmTypeEnum alarmTypeEnum;
  private String alarmObject;
  private String alarmMessage;
  private String alarmTime;
  private String alarmRecoverTime;
}
