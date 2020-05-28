package smartmon.taskmanager.types;

public interface TaskEvent {
  void contextUpdated(TaskContext context);

  void stepUpdated(TaskStep taskStep);
}
