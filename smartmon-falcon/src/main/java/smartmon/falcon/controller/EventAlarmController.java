package smartmon.falcon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.alarm.command.EventAlarmFilterCommand;
import smartmon.falcon.alarm.command.EventNoteCreateCommand;
import smartmon.falcon.alarm.model.Alarm;
import smartmon.falcon.alarm.model.Event;
import smartmon.falcon.alarm.model.EventNote;
import smartmon.falcon.alarm.service.EventAlarmService;
import smartmon.falcon.controller.vo.EventNoteCreateVo;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.webtools.page.SmartMonPageParams;
import smartmon.webtools.page.SmartMonPageResponseBuilder;


@Api(tags = "alarms")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/alarms")
@RestController
public class EventAlarmController {
  @Autowired
  private EventAlarmService eventAlarmService;
  @Autowired
  private TaskManagerService taskManagerService;

  /**
   * Get Alarm List.
   */
  @ApiOperation("Get Alarm List")
  @SmartMonPageParams
  @GetMapping
  public SmartMonResponse<Page<Alarm>> getAlarmList(
    @RequestParam(value = "start-time", required = false) Long startTime,
    @RequestParam(value = "end-time", required = false) Long endTime,
    @RequestParam(value = "process-status", required = false) String processStatus,
    @RequestParam(value = "status", required = false) String status,
    @RequestParam(value = "host-name", required = false) String hostName,
    @RequestParam(value = "priority", required = false) String priority,
    ServerHttpRequest request) {
    final EventAlarmFilterCommand filterCommand = new EventAlarmFilterCommand();
    filterCommand.setStartTime(startTime);
    filterCommand.setEndTime(endTime == null ? System.currentTimeMillis() / 1000 : endTime);
    filterCommand.setProcessStatus(processStatus);
    filterCommand.setStatus(status);
    if (StringUtils.isNotBlank(hostName)) {
      filterCommand.setEndpoints(Collections.singletonList(hostName));
    }
    filterCommand.setPriority(priority);
    final List<Alarm> alarms = eventAlarmService.getAlarms(filterCommand);
    return new SmartMonPageResponseBuilder<>(alarms, request, "endpoint").build();
  }

  /**
   * Get Event List.
   */
  @ApiOperation("Get Event List")
  @SmartMonPageParams
  @GetMapping("event/{alarm-id}")
  public SmartMonResponse<Page<Event>> getEventList(@PathVariable("alarm-id") String alarmId,
                                                    ServerHttpRequest request) {
    final List<Event> events = eventAlarmService.getEventsById(alarmId);
    return new SmartMonPageResponseBuilder<>(events, request, "id").build();
  }

  /**
   * Delete Alarms.
   */
  @ApiOperation("DELETE Alarm")
  @DeleteMapping("{alarm-id}")
  public SmartMonResponse<TaskGroupVo> deleteAlarm(@PathVariable("alarm-id") String alarmId) {
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_DEL).withResource(TaskRes.RES_FALCON_ALARM).withParameters(alarmId)
      .withStep("DELETE", "Delete alarm", () -> eventAlarmService.deleteAlarm(alarmId))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("DeleteAlarm", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Delete Alarms.
   */
  @ApiOperation("DELETE Alarms")
  @DeleteMapping
  public SmartMonResponse<TaskGroupVo> deleteAlarm(@RequestBody List<String> alarmIds) {
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_DEL).withResource(TaskRes.RES_FALCON_ALARM).withParameters(alarmIds)
      .withStep("DELETE", "Batch Delete alarm", () -> eventAlarmService.deleteAlarms(alarmIds))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("BatchDeleteAlarm", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * Get EventNotes.
   */
  @ApiOperation("Get Handle EventNote List")
  @GetMapping("/event-note/{alarm-id}")
  public SmartMonResponse<Page<EventNote>> getEventNoteList(@PathVariable("alarm-id") String alarmId,
                                                            ServerHttpRequest request) {
    final List<EventNote> eventNodes = eventAlarmService.getEventNodes(alarmId);
    return new SmartMonPageResponseBuilder<>(eventNodes, request, "caseId").build();
  }

  /**
   * Handle EventNote.
   */
  @ApiOperation("Handle EventNote")
  @PostMapping("/event-note/{alarm-id}")
  public SmartMonResponse<TaskGroupVo> handleEventNote(@PathVariable("alarm-id") String alarmId,
                                                       @RequestBody EventNoteCreateVo noteCreateVo) {
    final EventNoteCreateCommand eventNoteCreateCommand = new EventNoteCreateCommand();
    eventNoteCreateCommand.setEventId(alarmId);
    eventNoteCreateCommand.setNote(noteCreateVo.getNote());
    eventNoteCreateCommand.setStatus(noteCreateVo.getStatus().getStatusName());
    eventAlarmService.createEventNote(eventNoteCreateCommand);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_HANDLE).withResource(TaskRes.RES_FALCON_EVENT_NOTE).withParameters(eventNoteCreateCommand)
      .withStep("HANDLE", "Handle eventNote", () -> eventAlarmService.createEventNote(eventNoteCreateCommand))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("HandleEventNote", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }
}
