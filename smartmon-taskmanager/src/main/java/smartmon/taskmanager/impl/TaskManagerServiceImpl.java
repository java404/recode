package smartmon.taskmanager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;

@Service
public class TaskManagerServiceImpl implements TaskManagerService {
  // TODO save these to Databases
  private Long taskId = 0L;
  private Map<Long, TaskContext> tasks = new ConcurrentHashMap<>();

  @Autowired
  private AsyncTaskExecutor asyncTaskExecutor;

  private void taskExecutor(TaskContext context, Runnable step) {
    step.run();
  }

  private synchronized TaskContext makeTaskContext(String name) {
    final TaskContext taskContext = new TaskContext(taskId++, name);
    tasks.put(taskContext.getTaskId(), taskContext);
    return taskContext;
  }

  @Override
  public TaskContext createTask(String name) {
    return makeTaskContext(name);
  }

  @Override
  public void invokeTask(TaskContext context, Runnable... steps) {
    asyncTaskExecutor.submit(() -> {
      context.setStatus(TaskContext.TASK_RUNNING);
      for (final Runnable step : steps) {
        taskExecutor(context, step);
      }
      context.setStatus(TaskContext.TASK_COMPLETED);
    });
  }

  @Override
  public TaskContext invokeTask(String name, Runnable... steps) {
    final TaskContext context = makeTaskContext(name);
    invokeTask(context, steps);
    return context;
  }

  @Override
  public List<TaskContext> getAll() {
    return new ArrayList<>(tasks.values());
  }

  @Override
  public synchronized TaskContext findById(Long taskId) {
    if (!tasks.containsKey(taskId)) {
      return null;
    }
    return tasks.get(taskId);
  }
}
