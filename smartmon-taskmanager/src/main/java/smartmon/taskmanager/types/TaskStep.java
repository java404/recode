package smartmon.taskmanager.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import lombok.Data;
import smartmon.taskmanager.dto.TaskStepDto;
import smartmon.taskmanager.vo.TaskStepVo;
import smartmon.utilities.misc.JsonConverter;

@Data
public class TaskStep {
  public static final String STRATEGY_CONTINUE = "continue";
  public static final String STRATEGY_BREAK = "break";

  private Long taskStepId;
  private Long taskId;

  private final String strategy;

  private Date createTime = new Date();
  private Date completeTime = new Date();
  private boolean success = true;
  private String error;
  @JsonIgnore
  private Object detail;

  private final String stepName;
  private final String stepType;

  @JsonIgnore
  private final Runnable step;
  @JsonIgnore
  private final StringBuffer logBuffer = new StringBuffer();
  @JsonIgnore
  private TaskEvent taskEvent;

  public TaskStep(String type, String name, String strategy, Runnable step) {
    this.stepType = type;
    this.stepName = name;
    this.strategy = strategy;
    this.step = step;
  }

  public TaskStep(String type, String name, Runnable step) {
    this(type, name, STRATEGY_BREAK, step);
  }

  public void appendLog(String line) {
    logBuffer.append(line).append("\n");
    dump();
  }

  public void updateLog(String logs) {
    logBuffer.setLength(0);
    logBuffer.append(logs);
    dump();
  }

  public void dump() {
    if (taskEvent != null) {
      taskEvent.stepUpdated(this);
    }
  }

  public String dumpLog() {
    return logBuffer.toString();
  }

  public boolean isBreakStrategy() {
    return STRATEGY_BREAK.equalsIgnoreCase(strategy);
  }

  public TaskStepDto dumpDto() {
    final TaskStepDto dto = new TaskStepDto();
    dto.setTaskStepId(taskStepId);
    dto.setTaskId(taskId);
    dto.setStrategy(strategy);
    dto.setDetail(detail == null ? "" : JsonConverter.writeValueAsStringQuietly(detail));
    dto.setError(error);
    dto.setSuccess(success);
    dto.setCreateTime(createTime);
    dto.setCompleteTime(completeTime);
    dto.setStepName(stepName);
    dto.setStepType(stepType);
    dto.setStepLog(dumpLog());
    return dto;
  }

  public TaskStepVo dumpVo() {
    final TaskStepVo vo = new TaskStepVo();
    vo.setTaskStepId(taskStepId);
    vo.setTaskId(taskId);
    vo.setStrategy(strategy);
    vo.setDetail(detail == null ? "" : JsonConverter.writeValueAsStringQuietly(detail));
    vo.setError(error);
    vo.setSuccess(success);
    vo.setCreateTime(createTime);
    vo.setCompleteTime(completeTime);
    vo.setStepName(stepName);
    vo.setStepType(stepType);
    vo.setStepLog(dumpLog());
    return vo;
  }

  public static TaskStepVo dumpVo(TaskStepDto dto) {
    final TaskStepVo vo = new TaskStepVo();
    vo.setTaskStepId(dto.getTaskStepId());
    vo.setTaskId(dto.getTaskId());
    vo.setStrategy(dto.getStrategy());
    vo.setDetail(dto.getDetail());
    vo.setError(dto.getError());
    vo.setSuccess(dto.isSuccess());
    vo.setCreateTime(dto.getCreateTime());
    vo.setCompleteTime(dto.getCompleteTime());
    vo.setStepName(dto.getStepName());
    vo.setStepType(dto.getStepType());
    vo.setStepLog(dto.getStepLog());
    return vo;
  }
}
