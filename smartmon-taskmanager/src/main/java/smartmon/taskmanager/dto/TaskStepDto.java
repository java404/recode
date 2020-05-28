package smartmon.taskmanager.dto;

import java.util.Date;
import lombok.Data;

@Data
public class TaskStepDto {
  private Long taskStepId;
  private Long taskId;

  private String strategy;
  private String detail;
  private String error;
  private boolean success;

  private Date createTime;
  private Date completeTime;

  private String stepName;
  private String stepType;

  private String stepLog;
}
