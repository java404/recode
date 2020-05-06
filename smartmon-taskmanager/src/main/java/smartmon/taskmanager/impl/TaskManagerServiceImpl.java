package smartmon.taskmanager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import smartmon.taskmanager.TaskManagerService;
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

  // TODO save these to Databases
  private Long taskId = 0L;
  private final Map<Long, TaskContext> tasks = new ConcurrentHashMap<>();

  // TODO save these to Databases
  private Long taskGroupId = 0L;
  private final Map<Long, TaskGroup> taskGroups = new ConcurrentHashMap<>();

  private synchronized TaskContext makeTaskContext(Long taskGroupId, String name, TaskDescription description) {
    final TaskContext taskContext = new TaskContext(taskId++, taskGroupId, name, description);
    tasks.put(taskContext.getTaskId(), taskContext);
    return taskContext;
  }

  private synchronized TaskGroup makeTaskGroup(String name, List<TaskContext> tasks) {
    final TaskGroup taskGroup = new TaskGroup(taskGroupId++, serviceName, name, tasks);
    taskGroups.put(taskGroup.getTaskGroupId(), taskGroup);
    return taskGroup;
  }

  private boolean runTask(TaskGroup taskGroup, TaskContext task) {
    TaskContext.setCurrentContext(task);
    task.setStatus(TaskContext.STATUS_RUNNING);
    boolean success = true;
    for (final TaskStep step : task.getSteps()) {
      try {
        step.getStep().run();
      } catch (Exception err) {
        log.error("task error {}", task.getName(), err);
        task.setTaskError(err);
        success = false;
      }
    }
    task.setSuccess(success);
    task.setStatus(TaskContext.STATUS_COMPLETED);
    return true;
  }

  @Override
  public TaskGroup createTaskGroup(String name, List<TaskDescription> taskDescriptions) {
    if (CollectionUtils.isEmpty(taskDescriptions)) {
      return null;
    }
    final List<TaskContext> tasks = new ArrayList<>();
    final TaskGroup taskGroup = makeTaskGroup(name, tasks);
    int taskIdx = 0;
    for (TaskDescription taskDesc : taskDescriptions) {
      final TaskContext taskContext = makeTaskContext(taskGroup.getTaskGroupId(),
        String.format("task-%d", ++taskIdx), taskDesc);
      tasks.add(taskContext);
    }
    return taskGroup;
  }

  @Override
  public void invokeTaskGroup(TaskGroup taskGroup) {
    asyncTaskExecutor.submit(() -> {
      boolean taskErr = false;
      taskGroup.setStatus(TaskGroup.STATUS_RUNNING);
      // TODO launch task in different thread.
      final List<TaskContext> tasks = taskGroup.getTasks();
      for (TaskContext task : tasks) {
        if (!runTask(taskGroup, task)) {
          taskErr = true;
        }
      }
      taskGroup.setTaskError(taskErr);
      taskGroup.setStatus(TaskGroup.STATUS_COMPLETED);
    });
  }

  @Override
  public TaskGroup createTask(String name, TaskDescription task) {
    final List<TaskContext> tasks = new ArrayList<>();
    final TaskGroup taskGroup = makeTaskGroup(name, tasks);
    final TaskContext taskContext = makeTaskContext(taskGroup.getTaskGroupId(),"task-1", task);
    tasks.add(taskContext);
    return taskGroup;
  }

  @Override
  public TaskGroup createTask(String name, Runnable step) {
    final List<TaskContext> tasks = new ArrayList<>();
    final TaskGroup taskGroup = makeTaskGroup(name, tasks);
    final TaskDescription taskDesc = new TaskDescription(TaskOption.EMPTY,
      Collections.singletonList(new TaskStep(step)));
    final TaskContext taskContext = makeTaskContext(taskGroup.getTaskGroupId(),"task-1", taskDesc);
    tasks.add(taskContext);
    return taskGroup;
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

  @Override
  public TaskGroup invokeTask(String name, Runnable step) {
    final TaskDescription task = new TaskDescription(TaskOption.EMPTY, Collections.singletonList(new TaskStep(step)));
    final TaskGroup taskGroup = createTaskGroup(name, Collections.singletonList(task));
    invokeTaskGroup(taskGroup);
    return taskGroup;
  }

  @Override
  public TaskGroup findTaskGroupById(Long id) {
    return taskGroups.getOrDefault(id, null);
  }

  @Override
  public List<TaskGroup> getAllTaskGroups() {
    return new ArrayList<>(taskGroups.values());
  }
}
