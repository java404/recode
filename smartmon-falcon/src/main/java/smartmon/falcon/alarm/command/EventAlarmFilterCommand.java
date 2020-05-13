package smartmon.falcon.alarm.command;

import java.util.List;
import lombok.Data;
import smartmon.falcon.controller.vo.EventAlarmFilterVo;
import smartmon.utilities.misc.BeanConverter;

@Data
public class EventAlarmFilterCommand {
  private Long startTime;
  private Long endTime;
  private Integer limit;
  private String processStatus;
  private String status;
  private List<String> endpoints;
  private Integer strategyId;
  private Integer templateId;
  private String priority;
}
