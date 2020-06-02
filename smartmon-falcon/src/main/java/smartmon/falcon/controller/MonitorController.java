package smartmon.falcon.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.monitor.FalconConfigService;
import smartmon.falcon.monitor.types.CallBackParam;
import smartmon.falcon.monitor.types.EmailConfigInfo;
import smartmon.falcon.monitor.types.FalconConfig;
import smartmon.falcon.monitor.types.SnmpConfigInfo;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.record.TaskAct;
import smartmon.taskmanager.record.TaskRes;
import smartmon.taskmanager.types.TaskDescription;
import smartmon.taskmanager.types.TaskDescriptionBuilder;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.JsonConverter;

@Slf4j
@Api(tags = "monitors")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/monitors")
@RestController
public class MonitorController {
  @Autowired
  private FalconConfigService falconConfigService;
  @Autowired
  private TaskManagerService taskManagerService;


  /**
   * falcon alarm callback.
   * @param request 接受告警回调参数
   */
  @ApiOperation("falcon alarm callback")
  @GetMapping("callback")
  public void callback(ServerHttpRequest request) {
    final CallBackParam callBackParam = falconConfigService.handleCallBackParam(request);
    final FalconConfig emailConfig = falconConfigService.findItem("email");
    if (emailConfig != null) {
      try {
        final String emailConfigValue = emailConfig.getValue();
        final EmailConfigInfo emailConfigInfo = JsonConverter.readValueQuietly(emailConfigValue, EmailConfigInfo.class);
        if (emailConfigInfo != null && emailConfigInfo.getEnabled()) {
          if (emailConfigInfo.getAuth()) {
            falconConfigService.sendEmail(emailConfigInfo,
              falconConfigService.callBackParamToSendEmailParam(callBackParam));
          } else {
            falconConfigService.sendEmailNoAuth(emailConfigInfo,
              falconConfigService.callBackParamToSendEmailParam(callBackParam));
          }
        }
      } catch (Exception err) {
        log.warn("send email error: ", err);
      }
    }
    final FalconConfig snmpConfig = falconConfigService.findItem("snmp");
    if (snmpConfig != null) {
      try {
        final String snmpConfigValue = snmpConfig.getValue();
        final SnmpConfigInfo snmpConfigInfo = JsonConverter.readValueQuietly(snmpConfigValue, SnmpConfigInfo.class);
        if (snmpConfigInfo != null && snmpConfigInfo.getEnabled()) {
          if ("2C".equals(snmpConfigInfo.getVersion())) {
            falconConfigService.snmpV2CTrap(snmpConfigInfo, callBackParam);
          }
          if ("3".equals(snmpConfigInfo.getVersion())) {
            falconConfigService.snmpV3Trap(snmpConfigInfo, callBackParam);
          }
        }
      } catch (Exception err) {
        log.warn("snmp trap error: ", err);
      }
    }
  }

  /**
   * get snmp info.
   * @return snmp配置信息
   */
  @ApiOperation("get snmp info")
  @GetMapping("falcon-config/snmp")
  public SmartMonResponse<FalconConfig> getSnmpInfo() {
    return new SmartMonResponse<>(falconConfigService.findItem("snmp"));
  }

  /**
   * config snmp info.
   * @param snmpConfigInfo snmp配置参数
   * @return 任务组（根据id去查询该任务是否成功）
   */
  @ApiOperation("config snmp info")
  @PostMapping("falcon-config/snmp")
  public SmartMonResponse<TaskGroupVo> configSnmpInfo(@RequestBody SnmpConfigInfo snmpConfigInfo) {
    FalconConfig falconConfig = new FalconConfig();
    falconConfig.setName("snmp");
    falconConfig.setValue(JsonConverter.writeValueAsStringQuietly(snmpConfigInfo));
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_CONFIG).withResource(TaskRes.RES_FALCON_SNMP).withParameters(snmpConfigInfo)
      .withStep("CONFIG", "Config snmp", () -> falconConfigService.put(Lists.newArrayList(falconConfig)))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ConfigSnmp", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * get email info.
   * @return 邮件配置信息
   */
  @ApiOperation("get email info")
  @GetMapping("falcon-config/email")
  public SmartMonResponse<FalconConfig> getEmailInfo() {
    return new SmartMonResponse<>(falconConfigService.findItem("email"));
  }

  /**
   * config email info.
   * @param emailConfigInfo 邮件配置参数
   * @return 任务组（根据id去查询该任务是否成功）
   */
  @ApiOperation("config email info")
  @PostMapping("falcon-config/email")
  public SmartMonResponse<TaskGroupVo> configEmailInfo(@RequestBody EmailConfigInfo emailConfigInfo) {
    FalconConfig falconConfig = new FalconConfig();
    falconConfig.setName("email");
    falconConfig.setValue(JsonConverter.writeValueAsStringQuietly(emailConfigInfo));
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_CONFIG).withResource(TaskRes.RES_FALCON_EMAIL).withParameters(emailConfigInfo)
      .withStep("CONFIG", "Config email", () -> falconConfigService.put(Lists.newArrayList(falconConfig)))
      .build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("ConfigEmail", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

  /**
   * test send email.
   * @param receiver 接受者邮件地址
   * @return 任务组（根据id去查询该任务是否成功）
   */
  @ApiOperation("test send email")
  @GetMapping("falcon-config/email/test")
  public SmartMonResponse<TaskGroupVo> testSendEmail(@RequestParam("receiver") String receiver) {
    final FalconConfig emailConfig = falconConfigService.findItem("email");
    final String emailConfigValue = emailConfig.getValue();
    final EmailConfigInfo emailConfigInfo = JsonConverter.readValueQuietly(emailConfigValue, EmailConfigInfo.class);
    final TaskDescription description = new TaskDescriptionBuilder()
      .withAction(TaskAct.ACT_SEND).withResource(TaskRes.RES_FALCON_EMAIL).withParameters(emailConfigInfo)
      .withStep("SEND", "Send email", () -> {
        if (emailConfigInfo.getAuth()) {
          falconConfigService.sendEmail(emailConfigInfo, falconConfigService.testSendEmailParam(receiver));
        } else {
          falconConfigService.sendEmailNoAuth(emailConfigInfo, falconConfigService.testSendEmailParam(receiver));
        }
      }).build();
    final TaskGroup taskGroup = taskManagerService.createTaskGroup("SendEmail", description);
    taskManagerService.invokeTaskGroup(taskGroup);
    return new SmartMonResponse<>(taskGroup.dumpVo());
  }

}
