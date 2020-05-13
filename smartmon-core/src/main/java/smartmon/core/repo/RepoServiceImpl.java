package smartmon.core.repo;

import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import smartmon.core.config.SmartMonErrno;
import smartmon.utilities.general.SmartMonException;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.ssh.ShellExecuteSync;
import smartmon.utilities.ssh.ShellResult;

@Service
public class RepoServiceImpl implements RepoService {
  private RepoConfig repoConfig;

  @Autowired
  public RepoServiceImpl(RepoConfig repoConfig) {
    this.repoConfig = repoConfig;
  }

  @Override
  public void configRepoClient(TargetHost targetHost) {
    String systemRelease = getSystemRelease(targetHost);
    String repoBaseUrlSuffix = getRepoBaseUrlSuffix(systemRelease);
    String repoBaseUrl = getRepoBaseUrl(repoBaseUrlSuffix);
    generateRepoConfig(targetHost, repoBaseUrl);
  }

  private String getSystemRelease(TargetHost targetHost) {
    ShellExecuteSync shellExecutor = new ShellExecuteSync(targetHost);
    try {
      ShellResult shellResult = shellExecutor.invoke("cat /etc/system-release");
      if (shellResult.isOk()) {
        return shellResult.getOutput();
      }
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, "get system version info error");
    } catch (SmartMonException err) {
      throw err;
    } catch (Exception err) {
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, err.getMessage());
    }
  }

  private String getRepoBaseUrlSuffix(String systemRelease) {
    String osType = StringUtils.EMPTY;
    String versionRegex = StringUtils.EMPTY;
    if (systemRelease.startsWith("Red Hat") || systemRelease.startsWith("CentOS")) {
      osType = "redhat";
      versionRegex = "\\d+.\\d+";
    }
    if (StringUtils.isNotEmpty(osType)) {
      Pattern pattern = Pattern.compile(versionRegex);
      Matcher matcher = pattern.matcher(systemRelease);
      if (matcher.find()) {
        return osType + matcher.group(0);
      }
    }
    return StringUtils.EMPTY;
  }

  private String getRepoBaseUrl(String suffix) {
    String url = Strings.nullToEmpty(repoConfig.repoBaseUrl);
    url = url.endsWith("/") ? url : url + "/";
    return url + suffix;
  }

  private void generateRepoConfig(TargetHost targetHost, String repoBaseUrl) {
    String logicalAnd = "&&";
    StringBuilder commandBuilder = new StringBuilder()
      .append("cd /etc/yum.repos.d/").append(logicalAnd)
      .append(String.format("touch %s", repoConfig.repoFilename)).append(logicalAnd)
      .append(String.format("cat /dev/null > %s", repoConfig.repoFilename));
    Arrays.asList(String.format("[%s]", repoConfig.repoId),
      String.format("name=%s", repoConfig.repoId.toLowerCase()),
      String.format("baseurl=%s", repoBaseUrl), "enabled=0", "gpgcheck=0"
    ).forEach(line -> {
      commandBuilder.append(logicalAnd).append(String.format("echo %s >> %s", line, repoConfig.repoFilename));
    });
    ShellExecuteSync shellExecutor = new ShellExecuteSync(targetHost);
    try {
      ShellResult shellResult = shellExecutor.invoke(commandBuilder.toString());
      if (!shellResult.isOk()) {
        throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, "generate repo config error");
      }
    } catch (SmartMonException err) {
      throw err;
    } catch (Exception err) {
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, err.getMessage());
    }
  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Component
  public static class RepoConfig {
    @Value("${smartmon.repo.filename:smartmon.repo}")
    private String repoFilename;
    @Value("${smartmon.repo.id:SMARTMON-REPO}")
    private String repoId;
    @Value("${smartmon.repo.baseUrl}")
    private String repoBaseUrl;
  }
}
