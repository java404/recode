package smartmon.taskmanager.exception;

import smartmon.utilities.general.SmartMonException;

public class InvalidTaskGroupId extends SmartMonException {
  private static final long serialVersionUID = 670108159638583041L;

  public InvalidTaskGroupId() {
    super(TaskManagerErrno.INVALID_TASK_GROUP_ID, "Invalid Task Group Id");
  }
}
