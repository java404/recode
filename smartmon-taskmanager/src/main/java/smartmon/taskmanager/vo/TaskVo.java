package smartmon.taskmanager.vo;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class TaskVo {
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

  private List<TaskStepVo> steps;
}
