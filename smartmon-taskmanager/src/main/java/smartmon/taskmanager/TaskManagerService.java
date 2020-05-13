package smartmon.taskmanager;

import java.util.List;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskGroup;

public interface TaskManagerService {
  TaskGroup createTaskGroup(String name, List<TaskDescription> tasks);

  void invokeTaskGroup(TaskGroup taskGroup);

  TaskGroup createTask(String name, Runnable step);

  TaskGroup invokeTask(String name, Runnable... steps);

  TaskGroup findTaskGroupById(Long id);

  List<TaskGroup> getAllTaskGroups();
}
