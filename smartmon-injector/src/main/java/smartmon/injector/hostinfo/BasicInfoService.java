package smartmon.injector.hostinfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.BasicInfo;
import smartmon.injector.executors.ExecutorService;

@Slf4j
@Service
public class BasicInfoService {
  @Autowired
  private ExecutorService executorService;

  public BasicInfo getBasicInfo() {
    try {
      BasicInfo basicInfo = new BasicInfo();
      basicInfo.setHostname(executorService.executeShellCommand("hostname"));
      return basicInfo;
    } catch (Exception err) {
      log.error("get basic info error:", err);
      return null;
    }
  }
}
