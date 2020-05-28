package smartmon.taskmanager.dto;

import java.util.Date;
import lombok.Data;

@Data
public class TaskGroupDto {
  private Long taskGroupId;
  private String service;
  private String name;

  private Date createTime;
  private Date completeTime;
  private String status;
  private boolean success;
}
