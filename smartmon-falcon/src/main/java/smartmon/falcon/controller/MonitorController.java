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
import org.springframework.web.bind.annotation.RestController;
import smartmon.falcon.monitor.FalconConfigService;
import smartmon.falcon.monitor.types.CallBackParam;
import smartmon.falcon.monitor.types.EmailConfigInfo;
import smartmon.falcon.monitor.types.FalconConfig;
import smartmon.falcon.monitor.types.SnmpConfigInfo;
import smartmon.falcon.template.TemplateService;
import smartmon.falcon.user.TeamService;
import smartmon.falcon.user.User;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.JsonConverter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "monitors")
@RequestMapping("${smartmon.api.prefix:/falcon/api/v2}/monitors")
@RestController
public class MonitorController {
  @Autowired
  private FalconConfigService falconConfigService;
  @Autowired
  private TeamService teamService;

  @ApiOperation("falcon alarm callback")
  @GetMapping("callback")
  public void callback(ServerHttpRequest request) {
    final CallBackParam callBackParam = falconConfigService.handleCallBackParam(request);
    final FalconConfig emailConfig = falconConfigService.findItem("email");
    final String emailConfigValue = emailConfig.getValue();
    final EmailConfigInfo emailConfigInfo = JsonConverter.readValueQuietly(emailConfigValue, EmailConfigInfo.class);
    falconConfigService.sendEmail(emailConfigInfo, callBackParam);
    log.info(request.toString());
  }

  @ApiOperation("get snmp info")
  @GetMapping("falcon-config/snmp")
  public SmartMonResponse<FalconConfig> getSnmpInfo() {
    return new SmartMonResponse<>(falconConfigService.findItem("snmp"));
  }

  @ApiOperation("config snmp info")
  @PostMapping("falcon-config/snmp")
  public SmartMonResponse<String> configSnmpInfo(@RequestBody SnmpConfigInfo snmpConfigInfo) {
    FalconConfig falconConfig = new FalconConfig();
    falconConfig.setName("snmp");
    falconConfig.setValue(JsonConverter.writeValueAsStringQuietly(snmpConfigInfo));
    falconConfigService.put(Lists.newArrayList(falconConfig));
    return SmartMonResponse.OK;
  }

  @ApiOperation("get email info")
  @GetMapping("falcon-config/email")
  public SmartMonResponse<FalconConfig> getEmailInfo() {
    return new SmartMonResponse<>(falconConfigService.findItem("email"));
  }

  @ApiOperation("config email info")
  @PostMapping("falcon-config/email")
  public SmartMonResponse<String> configEmailInfo(@RequestBody EmailConfigInfo emailConfigInfo) {
    FalconConfig falconConfig = new FalconConfig();
    falconConfig.setName("email");
    falconConfig.setValue(JsonConverter.writeValueAsStringQuietly(emailConfigInfo));
    falconConfigService.put(Lists.newArrayList(falconConfig));
    return SmartMonResponse.OK;
  }
}
