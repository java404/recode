package smartmon.taskmanager.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import smartmon.taskmanager.dto.TaskDto;
import smartmon.taskmanager.dto.TaskGroupDto;
import smartmon.taskmanager.dto.TaskStepDto;

@Mapper
public interface TaskManagerMapper {
  @Insert("INSERT INTO task_group "
    + " ( create_time, complete_time, status, success, name, service ) VALUES "
    + " (#{createTime}, #{completeTime}, #{status}, #{success}, #{name}, #{service}) ")
  @Options(useGeneratedKeys = true, keyProperty = "taskGroupId", keyColumn = "task_group_id")
  void addTaskGroup(TaskGroupDto taskGroup);

  @Update("UPDATE task_group SET "
    + " complete_time = #{completeTime}, status = #{status}, success = #{success}"
    + " WHERE task_group_id = #{taskGroupId}")
  void updateTaskGroup(TaskGroupDto taskGroup);

  @Insert("INSERT INTO task "
    + " (task_group_id, create_time, complete_time, option, status, "
    + "  success, total_steps, completed_steps )  VALUES "
    + " (#{taskGroupId}, #{createTime}, #{completeTime}, #{taskOption}, #{status}, "
    + "  #{success}, #{totalSteps}, #{completedSteps} ) ")
  @Options(useGeneratedKeys = true, keyProperty = "taskId", keyColumn = "task_id")
  void addTask(TaskDto task);

  @Update("UPDATE task SET "
    + " complete_time = #{completeTime}, status = #{status}, detail = #{detail}, "
    + " success = #{success}, error = #{error}, "
    + " total_steps = #{totalSteps}, completed_steps = #{completedSteps} "
    + " WHERE task_id = #{taskId}")
  void updateTask(TaskDto task);

  @Insert("INSERT INTO task_step "
    + " (task_id, create_time, complete_time, strategy, error, step_name, step_type )  VALUES "
    + " (#{taskId}, #{createTime}, #{completeTime}, #{strategy}, "
    + " #{error}, #{stepName}, #{stepType} ) ")
  @Options(useGeneratedKeys = true, keyProperty = "taskStepId", keyColumn = "task_step_id")
  void addTaskStep(TaskStepDto taskStep);

  @Update("UPDATE task_step SET "
    + " complete_time = #{completeTime}, detail = #{detail}, step_log = #{stepLog}, "
    + " error = #{error}, success = #{success} "
    + " WHERE task_step_id = #{taskStepId}")
  void updateTaskStep(TaskStepDto taskStep);

  @Select("SELECT * FROM task_group WHERE task_group_id = #{taskGroupId}")
  @Results({
    @Result(property = "taskGroupId", column = "task_group_id"),
    @Result(property = "name", column = "name"),
    @Result(property = "service", column = "service"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "completeTime", column = "complete_time"),
    @Result(property = "status", column = "status"),
    @Result(property = "success", column = "success"),
  })
  TaskGroupDto findTaskGroupInfo(Long taskGroupId);

  @Select("SELECT * FROM task_group ORDER BY task_group_id ASC")
  @Results({
    @Result(property = "taskGroupId", column = "task_group_id"),
    @Result(property = "name", column = "name"),
    @Result(property = "service", column = "service"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "completeTime", column = "complete_time"),
    @Result(property = "status", column = "status"),
    @Result(property = "success", column = "success"),
  })
  List<TaskGroupDto> findTaskGroupsInfo();

  @Select("SELECT * FROM task WHERE task_group_id = #{taskGroupId} ORDER BY task_id ASC")
  @Results({
    @Result(property = "taskId", column = "task_id"),
    @Result(property = "taskGroupId", column = "task_group_id"),
    @Result(property = "taskOption", column = "option"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "completeTime", column = "complete_time"),
    @Result(property = "status", column = "status"),
    @Result(property = "success", column = "success"),
    @Result(property = "detail", column = "detail"),
    @Result(property = "error", column = "error"),
    @Result(property = "totalSteps", column = "total_steps"),
    @Result(property = "completedSteps", column = "completed_steps")
  })
  List<TaskDto> findTasks(Long taskGroupId);

  @Select("SELECT * FROM task_step WHERE task_id = #{taskId} ORDER BY task_step_id ASC")
  @Results({
    @Result(property = "taskStepId", column = "task_step_id"),
    @Result(property = "taskId", column = "task_id"),
    @Result(property = "createTime", column = "create_time"),
    @Result(property = "completeTime", column = "complete_time"),
    @Result(property = "detail", column = "detail"),
    @Result(property = "stepLog", column = "step_log"),
    @Result(property = "stepName", column = "step_name"),
    @Result(property = "stepType", column = "step_type"),
    @Result(property = "error", column = "error"),
    @Result(property = "success", column = "success"),
    @Result(property = "strategy", column = "strategy")
  })
  List<TaskStepDto> findTaskSteps(Long taskId);
}
