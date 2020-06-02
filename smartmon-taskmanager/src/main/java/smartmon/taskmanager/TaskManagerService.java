package smartmon.taskmanager;

import java.util.List;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;

public interface TaskManagerService {
  TaskGroup createTaskGroup(String name, List<TaskDescription> tasks);

  TaskGroup createTaskGroup(String name, TaskDescription task);


  void invokeTaskGroup(TaskGroup taskGroup);

  void invokeTaskGroupParallel(TaskGroup taskGroup);

  TaskGroupVo findTaskGroupById(Long id);

  List<TaskGroupVo> getAllTaskGroups();
}
