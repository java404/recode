package smartmon.taskmanager.dto;

import java.util.Date;
import lombok.Data;

@Data
public class TaskDto {
  private Long taskGroupId;
  private Long taskId;

  private Date createTime;
  private Date completeTime;
  private String status;
  private String taskOption;

  private boolean success;
  private String detail;
  private String error;
  private int totalSteps;
  private int completedSteps;
}
