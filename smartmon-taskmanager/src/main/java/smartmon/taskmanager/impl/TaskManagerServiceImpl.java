package smartmon.taskmanager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.mapper.TaskManagerMapper;
import smartmon.taskmanager.types.LogBufferEvents;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.types.TaskOption;
import smartmon.taskmanager.types.TaskStep;

@Service
@Slf4j
public class TaskManagerServiceImpl implements TaskManagerService {
  @Value("${smartmon.task.serviceName:taskManager}")
  private String serviceName;

  @Autowired
  private AsyncTaskExecutor asyncTaskExecutor;

  @Autowired
  private TaskManagerMapper taskManagerMapper;

  private TaskGroup makeTaskGroup(String name, List<TaskContext> tasks) {
    final TaskGroup taskGroup = new TaskGroup(serviceName, name, tasks);
    taskManagerMapper.addTaskGroup(taskGroup);
    log.debug("Created Task Group {}", taskGroup);
    return taskGroup;
  }

  private TaskContext makeTaskContext(Long taskGroupId, TaskDescription description) {
    final TaskContext taskContext = new TaskContext(taskGroupId, description);
    taskManagerMapper.addTask(taskContext);
    log.debug("Created Task context {}", taskContext);
    final List<TaskStep> steps = taskContext.getSteps();
    for (final TaskStep step : steps) {
      step.setTaskId(taskContext.getTaskId());
      taskManagerMapper.addTaskStep(step);
    }
    return taskContext;
  }

  private void updateTaskGroupStatus(TaskGroup taskGroup) {
    taskManagerMapper.updateTaskGroup(taskGroup);
  }

  private void updateTaskStatus(TaskContext task) {
    task.flush();
    taskManagerMapper.updateTask(task);
  }

  private void updateTaskStepStatus(TaskStep taskStep) {
    taskStep.flush();
    taskManagerMapper.updateTaskStep(taskStep);
  }

  private boolean runTask(TaskGroup taskGroup, TaskContext task) {
    final List<TaskStep> steps = task.getSteps();

    TaskContext.setCurrentContext(task);
    task.setStatus(TaskContext.STATUS_RUNNING);
    task.setTotalSteps(steps.size());
    task.setCompletedSteps(0);
    updateTaskStatus(task);

    boolean success = true;
    int completedSteps = 0;
    for (final TaskStep step : steps) {
      task.setCurrentStep(step);
      step.setLogBufferEvents(new LogBufferEvents() {
        @Override
        public void bufferUpdated(TaskStep taskStep) {
          updateTaskStepStatus(step);
        }
      });

      try {
        step.getStep().run();
      } catch (Exception error) {
        log.warn("task step error: ", error);
        if (step.isBreakStrategy()) {
          task.setError(error.getMessage());
          updateTaskStatus(task);
          success = false;
          break;
        }
      } finally {
        step.setCompleteTime(new Date());
        updateTaskStepStatus(step);

        task.setCompletedSteps(++completedSteps);
        updateTaskStatus(task);
      }
    }
    task.setCompletedSteps(completedSteps);
    task.setSuccess(success);
    task.setCompleteTime(new Date());
    task.setStatus(TaskContext.STATUS_COMPLETED);
    updateTaskStatus(task);
    return success;
  }

  @Override
  public TaskGroup createTaskGroup(String name, List<TaskDescription> taskDescriptions) {
    if (CollectionUtils.isEmpty(taskDescriptions)) {
      log.warn("Cannot create task group (no taskDescriptions).");
      return null;
    }
    final List<TaskContext> tasks = new ArrayList<>();
    final TaskGroup taskGroup = makeTaskGroup(name, tasks);
    int taskIdx = 0;
    for (TaskDescription taskDesc : taskDescriptions) {
      final TaskContext taskContext = makeTaskContext(taskGroup.getTaskGroupId(), taskDesc);
      tasks.add(taskContext);
    }
    return taskGroup;
  }

  @Override
  public void invokeTaskGroup(TaskGroup taskGroup) {
    asyncTaskExecutor.submit(() -> {
      boolean taskErr = false;

      taskGroup.setStatus(TaskGroup.STATUS_RUNNING);
      updateTaskGroupStatus(taskGroup);

      // TODO launch task in different threads.
      for (TaskContext task : taskGroup.getTasks()) {
        if (!runTask(taskGroup, task)) {
          taskErr = true;
        }
      }

      taskGroup.setTaskError(taskErr);
      taskGroup.setStatus(TaskGroup.STATUS_COMPLETED);
      updateTaskGroupStatus(taskGroup);
    });
  }

  @Override
  public TaskGroup createTask(String name, Runnable step) {
    final List<TaskDescription> tasks = new ArrayList<>();
    tasks.add(new TaskDescription(TaskOption.EMPTY,
      Collections.singletonList(new TaskStep(step))));
    return createTaskGroup(name, tasks);
  }

  @Override
  public TaskGroup invokeTask(String name, Runnable... steps) {
    final List<TaskStep> taskSteps = new ArrayList<>();
    for (final Runnable step : steps) {
      final TaskStep taskStep = new TaskStep(step);
      taskSteps.add(taskStep);
    }
    final TaskDescription task = new TaskDescription(TaskOption.EMPTY, taskSteps);
    final TaskGroup taskGroup = createTaskGroup(name, Collections.singletonList(task));
    invokeTaskGroup(taskGroup);
    return taskGroup;
  }

  private List<TaskContext> findTasks(Long groupId) {
    final List<TaskContext> tasks = taskManagerMapper.findTasks(groupId);
    for (final TaskContext task : tasks) {
      final List<TaskStep> steps = taskManagerMapper.findTaskSteps(task.getTaskId());
      task.setSteps(steps);
    }
    return tasks;
  }

  @Override
  public TaskGroup findTaskGroupById(Long id) {
    final TaskGroup taskGroup = taskManagerMapper.findTaskGroupInfo(id);
    taskGroup.setTasks(findTasks(taskGroup.getTaskGroupId()));
    return taskGroup;
  }

  @Override
  public List<TaskGroup> getAllTaskGroups() {
    final List<TaskGroup> taskGroups = taskManagerMapper.findTaskGroupsInfo();
    for (final TaskGroup taskGroup : taskGroups) {
      taskGroup.setTasks(findTasks(taskGroup.getTaskGroupId()));
    }
    return taskGroups;
  }
}
