package smartmon.taskmanager.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.types.TaskStep;

@Mapper
public interface TaskManagerMapper {
  @Insert("INSERT INTO task_group "
    + " ( create_time, complete_time, status, task_error, name, service ) VALUES "
    + " (#{createTime}, #{completeTime}, #{status}, #{taskError}, #{name}, #{service}) ")
  @Options(useGeneratedKeys = true, keyProperty = "taskGroupId", keyColumn = "task_group_id")
  void addTaskGroup(TaskGroup taskGroup);

  @Update("UPDATE task_group SET "
    + " complete_time = #{completeTime}, status = #{status}, task_error = #{taskError}"
    + " WHERE task_group_id = #{taskGroupId}")
  void updateTaskGroup(TaskGroup taskGroup);

  @Insert("INSERT INTO task "
    + " (task_group_id, create_time, complete_time, option, status, "
    + "  success, total_steps, completed_steps )  VALUES "
    + " (#{taskGroupId}, #{createTime}, #{completeTime}, #{option}, #{status}, "
    + "  #{success}, #{totalSteps}, #{completedSteps} ) ")
  @Options(useGeneratedKeys = true, keyProperty = "taskId", keyColumn = "task_id")
  void addTask(TaskContext taskContext);

  @Update("UPDATE task SET "
    + " complete_time = #{completeTime}, status = #{status}, details = #{detailContent}, "
    + " success = #{success}, total_steps = #{totalSteps}, completed_steps = #{completedSteps} "
    + " WHERE task_id = #{taskId}")
  void updateTask(TaskContext taskContext);

  @Insert("INSERT INTO task_step "
    + " (task_id, create_time, complete_time, strategy )  VALUES "
    + " (#{taskId}, #{createTime}, #{completeTime}, #{strategy} ) ")
  @Options(useGeneratedKeys = true, keyProperty = "taskStepId", keyColumn = "task_step_id")
  void addTaskStep(TaskStep taskStep);

  @Update("UPDATE task_step SET "
    + " complete_time = #{completeTime}, detail = #{detailContent}, step_log = #{stepLog} "
    + " WHERE task_step_id = #{taskStepId}")
  void updateTaskStep(TaskStep taskStep);

  @Select("SELECT * FROM task_group WHERE task_group_id = #{taskGroupId}")
  @Results({
    @Result(property = "taskGroupId", column = "task_group_id"),
    @Result(property = "name", column = "name"),
    @Result(property = "service", column = "service"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "completeTime", column = "complete_time"),
    @Result(property = "status", column = "status"),
    @Result(property = "taskError", column = "task_error"),
  })
  TaskGroup findTaskGroupInfo(Long taskGroupId);

  @Select("SELECT * FROM task_group ORDER BY task_group_id ASC")
  @Results({
    @Result(property = "taskGroupId", column = "task_group_id"),
    @Result(property = "name", column = "name"),
    @Result(property = "service", column = "service"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "completeTime", column = "complete_time"),
    @Result(property = "status", column = "status"),
    @Result(property = "taskError", column = "task_error"),
  })
  List<TaskGroup> findTaskGroupsInfo();

  @Select("SELECT * FROM task WHERE task_group_id = #{taskGroupId} ORDER BY task_id ASC")
  @Results({
    @Result(property = "taskId", column = "task_id"),
    @Result(property = "taskGroupId", column = "task_group_id"),
    @Result(property = "option", column = "option"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "completeTime", column = "complete_time"),
    @Result(property = "status", column = "status"),
    @Result(property = "success", column = "success"),
    @Result(property = "detail", column = "detail"),
    @Result(property = "error", column = "error"),
    @Result(property = "totalSteps", column = "total_steps"),
    @Result(property = "completedSteps", column = "completed_steps")
  })
  List<TaskContext> findTasks(Long taskGroupId);


  @Select("SELECT * FROM task_step WHERE task_id = #{taskId} ORDER BY task_step_id ASC")
  @Results({
    @Result(property = "taskStepId", column = "task_step_id"),
    @Result(property = "taskId", column = "task_id"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "completeTime", column = "complete_time"),
    @Result(property = "detail", column = "detail"),
    @Result(property = "stepLog", column = "step_log")
  })
  List<TaskStep> findTaskSteps(Long taskId);
}
