package smartmon.injector.hostinfo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.NetworkInfo;
import smartmon.injector.config.SmartMonBatchConfig;
import smartmon.injector.executors.ExecutorService;
import smartmon.utilities.misc.JsonConverter;
import smartmon.utilities.misc.SmartMonResultParser;

@Slf4j
@Service
public class NetworkInfoService {
  @Autowired
  private SmartMonBatchConfig smartMonBatchConfig;
  @Autowired
  private ExecutorService executorService;

  public NetworkInfo getNetworkInfo() {
    try {
      String command = String.format("cd %s && python ./if_info.py", smartMonBatchConfig.getScriptsPath());
      String result = executorService.executeShellCommand(command);
      return JsonConverter.readValueQuietly(result, NetworkInfo.class);
    } catch (Exception err) {
      log.error("get network info error:", err);
      return null;
    }
  }

  public String getNetInterfaces() {
    try {
      String command = String.format("cd %s && python ./if_list.py", smartMonBatchConfig.getScriptsPath());
      String result = executorService.executeShellCommand(command);
      return SmartMonResultParser.parseSmartMonResult(result);
    } catch (Exception err) {
      log.error("get network interfaces error:", err);
      return null;
    }
  }
}
