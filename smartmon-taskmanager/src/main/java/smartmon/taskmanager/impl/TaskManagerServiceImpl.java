package smartmon.taskmanager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.dto.TaskDto;
import smartmon.taskmanager.dto.TaskGroupDto;
import smartmon.taskmanager.dto.TaskStepDto;
import smartmon.taskmanager.mapper.TaskManagerMapper;
import smartmon.taskmanager.record.TaskStatus;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskEvent;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.types.TaskStep;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.taskmanager.vo.TaskVo;

@Service
@Slf4j
public class TaskManagerServiceImpl implements TaskManagerService {
  @Value("${smartmon.task.serviceName:taskManager}")
  private String serviceName;

  @Autowired
  private AsyncTaskExecutor asyncTaskExecutor;

  @Autowired
  private TaskManagerMapper taskManagerMapper;


  private final TaskEvent taskEvent = new TaskEvent() {
    @Override
    public void contextUpdated(TaskContext context) {
      syncTaskContext(context);
    }

    @Override
    public void stepUpdated(TaskStep taskStep) {
      syncTaskStep(taskStep);
    }
  };

  private void syncTaskGroup(TaskGroup taskGroup) {
    final TaskGroupDto dto = taskGroup.dumpDto();
    taskManagerMapper.updateTaskGroup(dto);
  }

  private void syncTaskContext(TaskContext taskContext) {
    final TaskDto dto = taskContext.dumpDto();
    taskManagerMapper.updateTask(dto);
  }

  private void syncTaskStep(TaskStep taskStep) {
    final TaskStepDto dto = taskStep.dumpDto();
    taskManagerMapper.updateTaskStep(dto);
  }

  private void run(TaskGroup taskGroup) {
    asyncTaskExecutor.submit(() -> {
      boolean taskGroupSuccess = true;
      taskGroup.setStatus(TaskStatus.RUNNING);
      syncTaskGroup(taskGroup);

      for (TaskContext task : taskGroup.getTasks()) {
        // TODO launch task in different threads.
        task.setTaskEvent(taskEvent);
        TaskContext.run(task);
        if (!task.isSuccess()) {
          taskGroupSuccess = false;
        }
      }

      taskGroup.setSuccess(taskGroupSuccess);
      taskGroup.setStatus(TaskStatus.COMPLETED);
      syncTaskGroup(taskGroup);
    });
  }

  private void createTaskStep(Long taskId, TaskStep taskStep) {
    taskStep.setTaskId(taskId);
    final TaskStepDto dto = taskStep.dumpDto();
    taskManagerMapper.addTaskStep(dto);
    taskStep.setTaskStepId(dto.getTaskStepId());
  }

  private TaskContext createTask(Long taskGroupId, TaskDescription taskDescription) {
    final TaskContext context = new TaskContext(taskGroupId, taskDescription);
    final TaskDto taskDto = context.dumpDto();
    taskManagerMapper.addTask(taskDto);
    context.setTaskId(taskDto.getTaskId());
    for (final TaskStep taskStep : context.getSteps()) {
      createTaskStep(context.getTaskId(), taskStep);
    }
    return context;
  }

  private List<TaskContext> createTasks(Long taskGroupId, List<TaskDescription> taskDescriptions) {
    final List<TaskContext> tasks = new ArrayList<>();
    for (TaskDescription taskDescription : taskDescriptions) {
      tasks.add(createTask(taskGroupId, taskDescription));
    }
    return tasks;
  }

  @Override
  public TaskGroup createTaskGroup(String name, List<TaskDescription> taskDescriptions) {
    final TaskGroup group = new TaskGroup(serviceName, name, null);
    final TaskGroupDto taskGroupDto = group.dumpDto();
    taskManagerMapper.addTaskGroup(taskGroupDto);
    group.setTaskGroupId(taskGroupDto.getTaskGroupId());

    final List<TaskContext> taskContexts = createTasks(group.getTaskGroupId(), taskDescriptions);
    group.setTasks(taskContexts);
    return group;
  }

  @Override
  public TaskGroup createTaskGroup(String name, TaskDescription task) {
    return createTaskGroup(name, Collections.singletonList(task));
  }

  @Override
  public void invokeTaskGroup(TaskGroup taskGroup) {
    run(taskGroup);
  }

  private List<TaskVo> findTasks(Long groupId) {
    final List<TaskVo> taskVoList = new ArrayList<>();
    final List<TaskDto> tasksDto = taskManagerMapper.findTasks(groupId);
    for (final TaskDto taskDto : tasksDto) {
      final List<TaskStepDto> stepsDto = taskManagerMapper.findTaskSteps(taskDto.getTaskId());
      final TaskVo taskVo = TaskContext.dumpVo(taskDto, stepsDto);
      taskVoList.add(taskVo);
    }
    return taskVoList;
  }

  @Override
  public TaskGroupVo findTaskGroupById(Long id) {
    final TaskGroupDto taskGroupDto = taskManagerMapper.findTaskGroupInfo(id);
    final List<TaskVo> tasks = findTasks(taskGroupDto.getTaskGroupId());
    return TaskGroup.dumpVo(taskGroupDto, tasks);
  }

  @Override
  public List<TaskGroupVo> getAllTaskGroups() {
    final List<TaskGroupVo> taskGroupVos = new ArrayList<>();
    final List<TaskGroupDto> taskGroupDtos = taskManagerMapper.findTaskGroupsInfo();
    for (final TaskGroupDto taskGroupDto : taskGroupDtos) {
      final List<TaskVo> tasks = findTasks(taskGroupDto.getTaskGroupId());
      final TaskGroupVo taskGroupVo = TaskGroup.dumpVo(taskGroupDto, tasks);
      taskGroupVos.add(taskGroupVo);
    }
    return taskGroupVos;
  }
}
