package smartmon.injector.hostinfo.services;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.SystemInfo;
import smartmon.injector.executors.ExecutorService;

@Slf4j
@Service
public class SystemInfoService {
  @Autowired
  private ExecutorService executorService;

  public SystemInfo getSystemInfo() {
    try {
      SystemInfo systemInfo = new SystemInfo();
      Properties props = System.getProperties();
      systemInfo.setSystem(props.getProperty("os.name"));
      systemInfo.setKernel(props.getProperty("os.version"));
      systemInfo.setArchitecture(executorService.executeShellCommand("uname -m"));
      systemInfo.setSystemVendor(executorService.executeShellCommand("dmidecode -s system-manufacturer"));
      String systemRelease = executorService.executeShellCommand("cat /etc/system-release");
      String osFamily = getOsFamily(systemRelease);
      String releaseVersion = parseOsReleaseVersion(systemRelease);
      systemInfo.setOsFamily(osFamily);
      systemInfo.setDistribution(osFamily + releaseVersion);
      return systemInfo;
    } catch (Exception err) {
      log.error("get system info error:", err);
      return null;
    }
  }

  private String getOsFamily(String systemRelease) {
    String defaultOsFamily = "Others";
    if (systemRelease == null) {
      return defaultOsFamily;
    } else if (systemRelease.startsWith("CentOS")) {
      return "CentOS";
    } else if (systemRelease.startsWith("Red Hat")) {
      return "RedHat";
    } else {
      return defaultOsFamily;
    }
  }

  private String parseOsReleaseVersion(String systemRelease) {
    Matcher matcher = Pattern.compile("[\\d+.]+\\d+").matcher(systemRelease);
    if (matcher.find()) {
      return matcher.group(0);
    }
    return Strings.EMPTY;
  }
}
