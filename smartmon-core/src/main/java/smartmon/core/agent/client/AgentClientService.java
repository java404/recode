package smartmon.core.agent.client;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import smartmon.core.hosts.SmartMonHostInfo;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.JsonConverter;
import smartmon.webdata.config.SystemConfig;

//TODO: simple implement, need improve
@Service
public class AgentClientService {
  private RestTemplate restTemplate = new RestTemplate();

  @Autowired
  private SystemConfig systemConfig;

  public void uploadFiles(String serviceIp, Map<String, File> files) {
    String uploadUrl = String.format("http://%s:%s/injector/api/v2/files/upload",
      serviceIp, systemConfig.getInjectorPort());
    try {
      boolean uploadSuccess = FileUploader.upLoadFiles(uploadUrl, files);
    } catch (IOException err) {
      throw new RuntimeException(err);
    }
  }

  public String executeShellCommand(String serviceIp, String command) {
    HttpEntity<String> httpEntity = new HttpEntity<String>(command);
    String url = String.format("http://%s:%s/injector/api/v2/executors/shell-command",
      serviceIp, systemConfig.getInjectorPort());
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    return responseEntity.getStatusCode() == HttpStatus.OK ? responseEntity.getBody() : null;
  }

  public TaskGroupVo getTaskResult(String serviceIp, String taskGroupId) {
    String url = String.format("http://%s:%s/injector/api/v2/tasks/%s",
      serviceIp, systemConfig.getInjectorPort(), taskGroupId);
    HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY);
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
    SmartMonResponse response = JsonConverter.readValueQuietly(responseEntity.getBody(), SmartMonResponse.class);
    return response == null || response.getContent() == null
      ? null : JsonConverter.readValueQuietly(JsonConverter.writeValueAsStringQuietly(response.getContent()),
      TaskGroupVo.class);
  }

  public SmartMonHostInfo getHostInfo(String serviceIp) {
    String url = String.format("http://%s:%s/injector/api/v2/host/info", serviceIp, systemConfig.getInjectorPort());
    HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY);
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
    return JsonConverter.readValueQuietly(responseEntity.getBody(), SmartMonHostInfo.class);
  }
}
