package smartmon.taskmanager;

import java.util.List;
import smartmon.taskmanager.types.TaskContext;

public interface TaskManagerService {
  TaskContext createTask(String name);

  void invokeTask(TaskContext context, Runnable... steps);

  TaskContext invokeTask(String name, Runnable... steps);

  List<TaskContext> getAll();

  TaskContext findById(Long taskId);
}
