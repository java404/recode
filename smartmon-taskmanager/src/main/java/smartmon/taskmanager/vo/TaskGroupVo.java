package smartmon.taskmanager.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class TaskGroupVo {
  private Long taskGroupId;
  private String service;
  private String name;
  private Date createTime;
  private Date completeTime;
  private String status;
  private boolean success;

  private List<TaskVo> tasks;
}
