package smartmon.injector.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import smartmon.core.hosts.SmartMonHostInfo;
import smartmon.injector.upload.FileChunkParameter;
import smartmon.injector.upload.UploadConstants;
import smartmon.taskmanager.vo.TaskGroupVo;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.JsonConverter;
import smartmon.webdata.config.SystemConfig;

//TODO: simple implement, need improve
@Slf4j
@Service
public class AgentClientServiceImpl implements AgentClientService {
  private static final RestTemplate restTemplate = new RestTemplate();

  @Autowired
  private SystemConfig systemConfig;

  @Override
  public Boolean isInjectorHealthy(String serviceIp) {
    String url = String.format("http://%s:%s/injector/api/v2/health", serviceIp, systemConfig.getInjectorPort());
    HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY);
    ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Boolean.class);
    return responseEntity.getBody();
  }

  @Override
  public void uploadFile(String serviceIp, String targetPath, String filename, File file) throws IOException {
    FileChunkParameter fileChunkParameter = new FileChunkParameter();
    fileChunkParameter.setChunks(1);
    fileChunkParameter.setPath(targetPath);
    fileChunkParameter.setName(filename);
    long chunks = file.length() / UploadConstants.CHUNK_SIZE;
    if (file.length() % UploadConstants.CHUNK_SIZE > 0 || file.length() == 0) {
      chunks++;
    }
    fileChunkParameter.setChunks(chunks);
    int bytesRead = 0;
    byte[] buffer = new byte[UploadConstants.CHUNK_SIZE];
    int chunk = 0;
    try (FileInputStream fis = new FileInputStream(file)) {
      while ((bytesRead = fis.read(buffer, 0, UploadConstants.CHUNK_SIZE)) != -1 || file.length() == 0) {
        fileChunkParameter.setChunk(chunk++);
        if (file.length() == 0) {
          bytesRead = 0;
        }
        byte[] newBytes = new byte[bytesRead];
        System.arraycopy(buffer, 0, newBytes, 0, bytesRead);
        fileChunkParameter.setBytes(newBytes);
        String uploadUrl = String.format("http://%s:%s/injector/api/v2/upload",
          serviceIp, systemConfig.getInjectorPort());
        HttpEntity<FileChunkParameter> httpEntity = new HttpEntity<>(fileChunkParameter);
        restTemplate.exchange(uploadUrl, HttpMethod.POST, httpEntity, String.class);
        if (file.length() == 0) {
          break;
        }
      }
    }
  }

  @Override
  public void deleteTempDir(String serviceIp, String dir) {
    String url = String.format("http://%s:%s/injector/api/v2/executors/dirs", serviceIp, systemConfig.getInjectorPort());
    url = String.format("%s?dir=%s", url, dir);
    HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY);
    restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, Boolean.class);
  }

  @Override
  public String executeShellCommand(String serviceIp, String command) {
    HttpEntity<String> httpEntity = new HttpEntity<String>(command);
    String url = String.format("http://%s:%s/injector/api/v2/executors/shell-command",
      serviceIp, systemConfig.getInjectorPort());
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    return responseEntity.getStatusCode() == HttpStatus.OK ? responseEntity.getBody() : null;
  }

  @Override
  public TaskGroupVo getTaskResult(String serviceIp, String taskGroupId) {
    String url = String.format("http://%s:%s/injector/api/v2/tasks/%s",
      serviceIp, systemConfig.getInjectorPort(), taskGroupId);
    HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY);
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
    SmartMonResponse<TaskGroupVo> response = JsonConverter.readValueQuietly(
      responseEntity.getBody(), SmartMonResponse.class, TaskGroupVo.class);
    return response == null ? null : response.getContent();
  }

  @Override
  public SmartMonHostInfo getHostInfo(String serviceIp) {
    String url = String.format("http://%s:%s/injector/api/v2/host/info", serviceIp, systemConfig.getInjectorPort());
    HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY);
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
    return JsonConverter.readValueQuietly(responseEntity.getBody(), SmartMonHostInfo.class);
  }

  @Override
  public String getNetInterfaces(String serviceIp) {
    String url = String.format("http://%s:%s/injector/api/v2/host/network/interfaces",
      serviceIp, systemConfig.getInjectorPort());
    HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY);
    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
    return responseEntity.getBody();
  }

  @Override
  public List<String> getMonitorInterfaces(String serviceIp) {
    try {
      String url = String.format("http://%s:%s/config/iface", serviceIp, systemConfig.getAgentPort());
      HttpEntity<String> httpEntity = new HttpEntity<String>(StringUtils.EMPTY);
      ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
      String result = responseEntity.getBody();
      AgentMonitorInterfaces monitorInterfaces = JsonConverter.readValueQuietly(result, AgentMonitorInterfaces.class);
      if (monitorInterfaces != null && "success".equalsIgnoreCase(monitorInterfaces.getMsg())) {
        return monitorInterfaces.getData();
      }
    } catch (Exception ignored) {
    }
    return null;
  }

  @Override
  public void configMonitorInterfaces(String serviceIp, List<String> monitorNetInterfaces) {
    try {
      String url = String.format("http://%s:%s/config/iface", serviceIp, systemConfig.getAgentPort());
      String parameters = JsonConverter.writeValueAsStringQuietly(monitorNetInterfaces);
      HttpEntity<String> httpEntity = new HttpEntity<String>(parameters);
      restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    } catch (Exception err) {
      String message = "config monitor net interfaces failed";
      log.error(message, err);
      throw new RuntimeException(message, err);
    }
  }

  @Getter
  @Setter
  private static class AgentMonitorInterfaces {
    private String msg;
    private List<String> data;
  }
}
