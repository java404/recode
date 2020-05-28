package smartmon.taskmanager.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.ListUtils;
import smartmon.taskmanager.dto.TaskDto;
import smartmon.taskmanager.dto.TaskStepDto;
import smartmon.taskmanager.record.TaskStatus;
import smartmon.taskmanager.vo.TaskStepVo;
import smartmon.taskmanager.vo.TaskVo;
import smartmon.utilities.misc.JsonConverter;

@Data
public class TaskContext {
  private static final ThreadLocal<TaskContext> currentContext = new ThreadLocal<TaskContext>();

  private final Long taskGroupId;
  private Long taskId;


  private List<TaskStep> steps;
  @JsonIgnore
  private TaskStep currentStep;

  private Date createTime = new Date();
  private Date completeTime = new Date();
  private String status = TaskStatus.PENDING;
  private TaskOption taskOption;

  private boolean success = true;
  private Object detail;
  private String error;

  private int totalSteps = 0;
  private int completedSteps = 0;

  @JsonIgnore
  private TaskEvent taskEvent;

  public TaskContext(Long taskGroupId, TaskDescription description) {
    this.taskGroupId = taskGroupId;
    this.steps = ListUtils.emptyIfNull(description.getSteps());
    this.totalSteps = this.steps.size();
    this.taskOption = description.getOption();
  }

  public void dump() {
    if (taskEvent != null) {
      taskEvent.contextUpdated(this);
    }
  }

  public TaskDto dumpDto() {
    final TaskDto dto = new TaskDto();
    dto.setTaskGroupId(taskGroupId);
    dto.setTaskId(taskId);
    dto.setCreateTime(createTime);
    dto.setCompleteTime(completeTime);
    dto.setStatus(status);
    dto.setTaskOption(taskOption == null
      ? "" : JsonConverter.writeValueAsStringQuietly(taskOption));
    dto.setSuccess(success);
    dto.setDetail(null == detail ? "" : JsonConverter.writeValueAsStringQuietly(detail));
    dto.setError(error);
    dto.setTotalSteps(totalSteps);
    dto.setCompletedSteps(completedSteps);
    return dto;
  }

  public TaskVo dumpVo() {
    final TaskVo vo = new TaskVo();
    vo.setTaskId(taskId);
    vo.setCreateTime(createTime);
    vo.setCompleteTime(completeTime);
    vo.setStatus(status);
    vo.setTaskOption(taskOption == null
      ? "" : JsonConverter.writeValueAsStringQuietly(taskOption));
    vo.setSuccess(success);
    vo.setDetail(null == detail ? "" : JsonConverter.writeValueAsStringQuietly(detail));
    vo.setError(error);
    vo.setTotalSteps(totalSteps);
    vo.setCompletedSteps(completedSteps);
    final List<TaskStepVo> stepVoList = new ArrayList<>();
    for (final TaskStep step : steps) {
      stepVoList.add(step.dumpVo());
    }
    vo.setSteps(stepVoList);
    return vo;
  }

  public static TaskVo dumpVo(TaskDto taskDto, List<TaskStepDto> stepsDto) {
    final TaskVo vo = new TaskVo();
    vo.setTaskId(taskDto.getTaskId());
    vo.setCreateTime(taskDto.getCreateTime());
    vo.setCompleteTime(taskDto.getCompleteTime());
    vo.setStatus(taskDto.getStatus());
    vo.setTaskOption(taskDto.getTaskOption());
    vo.setSuccess(taskDto.isSuccess());
    vo.setDetail(taskDto.getDetail());
    vo.setError(taskDto.getError());
    vo.setTotalSteps(taskDto.getTotalSteps());
    vo.setCompletedSteps(taskDto.getCompletedSteps());
    final List<TaskStepVo> stepVoList = new ArrayList<>();
    for (final TaskStepDto stepDto : stepsDto) {
      stepVoList.add(TaskStep.dumpVo(stepDto));
    }
    vo.setSteps(stepVoList);
    return vo;
  }

  public static TaskContext currentTaskContext() {
    return currentContext.get();
  }

  private static boolean runTaskStep(TaskContext context, TaskStep step) {
    context.setCurrentStep(step);
    context.dump();
    try {
      step.getStep().run();
    } catch (Exception err) {
      step.setSuccess(false);
      step.setError(err.getMessage());
      step.dump();
      context.setError(err.getMessage());
      context.dump();
      return false;
    }
    step.dump();
    context.dump();
    return true;
  }

  public static void run(TaskContext context) {
    boolean taskSuccess = true;
    currentContext.set(context);

    context.setStatus(TaskStatus.RUNNING);
    final List<TaskStep> steps = context.getSteps();
    final int totalSteps = steps.size();
    int completedSteps = 0;
    context.setTotalSteps(totalSteps);
    context.setCompletedSteps(completedSteps);
    context.dump();

    for (final TaskStep step : steps) {
      context.setCurrentStep(step);
      step.setTaskEvent(context.getTaskEvent());
      boolean stepResult = runTaskStep(context, step);
      context.setCompletedSteps(++completedSteps);
      if (stepResult) {
        context.dump();
        continue;
      }
      if (step.isBreakStrategy()) {
        taskSuccess = false;
        break;
      }
      if (completedSteps == totalSteps) {
        taskSuccess = false;
        break;
      }
    }
    context.setSuccess(taskSuccess);
    context.setStatus(TaskStatus.COMPLETED);
    context.dump();
  }
}
