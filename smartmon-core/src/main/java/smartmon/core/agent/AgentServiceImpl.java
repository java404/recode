package smartmon.core.agent;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import java.io.File;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import smartmon.core.config.SmartMonErrno;
import smartmon.core.mapper.SmartMonHostMapper;
import smartmon.core.hosts.types.SmartMonHost;
import smartmon.core.repo.RepoService;
import smartmon.taskmanager.TaskManagerService;
import smartmon.taskmanager.types.TaskContext;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.utilities.general.SmartMonException;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.ssh.SecureCopy;
import smartmon.utilities.ssh.SecureCopyEvent;
import smartmon.utilities.ssh.SecureCopyParameters;
import smartmon.utilities.ssh.ShellExecute;
import smartmon.utilities.ssh.ShellExecuteEvent;

@Slf4j
@Service
public class AgentServiceImpl implements AgentService {
  @Value("${smartmon.batch.sourceRoot:}")
  private String sourceRoot;
  @Value("${smartmon.batch.targetFolder:}")
  private String targetFolder;

  @Autowired
  private SmartMonHostMapper smartMonHostMapper;
  @Autowired
  private TaskManagerService taskManagerService;
  @Autowired
  private RepoService repoService;

  @PostConstruct
  public void init() {
    if (StringUtils.isEmpty(sourceRoot)) {
      try {
        sourceRoot = new File(ResourceUtils.getURL("classpath:").getPath())
          .getParentFile().getParentFile().getParent();
      } catch (Exception ignored) {
      }
    }
    log.info(sourceRoot);
  }

  @Override
  public TaskGroup installInjector(String hostUuid) {
    SmartMonHost smartMonHost = smartMonHostMapper.selectByPrimaryKey(hostUuid);
    TargetHost targetHost = TargetHost.builder(smartMonHost.getManageIp(), smartMonHost.getSshPort())
      .username(smartMonHost.getSysUsername()).password(smartMonHost.getSysPassword()).build();
    return installInjector(targetHost);
  }

  @Override
  public TaskGroup installInjector(TargetHost targetHost) {
    Runnable configRepoClient = () -> repoService.configRepoClient(targetHost);
    Runnable copyPackages = () -> {
      TaskContext currentContext = TaskContext.getCurrentContext();
      SecureCopyEvent secureCopyEvent = (
        SecureCopyParameters options, String source, String target, long sourceSize, long count, boolean ended) -> {
        String message = String.format("From: %s, to: %s (%d/%d)", source, target, count, sourceSize);
        currentContext.getCurrentStep().setDetail(message);
      };
      copyPackages(targetHost, secureCopyEvent);
    };
    Runnable installSoftware = () -> {
      TaskContext currentContext = TaskContext.getCurrentContext();
      ShellExecuteEvent shellExecuteEvent = (message) -> {
        String text = Objects.toString(currentContext.getCurrentStep().getDetail(), StringUtils.EMPTY);
        currentContext.getCurrentStep().setDetail(text + message);
      };
      installSoftware(targetHost, shellExecuteEvent);
    };
    return taskManagerService.invokeTask("InstallInjector", configRepoClient, copyPackages, installSoftware);
  }

  private void copyPackages(TargetHost targetHost, SecureCopyEvent secureCopyEvent) {
    final SecureCopyParameters parameters = new SecureCopyParameters();
    parameters.setSourceRoot(sourceRoot);
    final Set<String> sources = Sets.newHashSet();
    sources.add("scripts/injector_install.sh");
    parameters.setSourceFile(sources);
    parameters.setTargetFolder(getWorkFolder());
    parameters.setRecreateTargetFolder(true);
    final SecureCopy scp = new SecureCopy(targetHost);
    scp.setCopyEvent(secureCopyEvent);
    try {
      scp.copy(parameters);
    } catch (Exception err) {
      throw new SmartMonException(SmartMonErrno.SSH_COPY_FILE_ERROR, err.getMessage());
    }
  }

  private void installSoftware(TargetHost targetHost, ShellExecuteEvent shellExecuteEvent) {
    int code = 0;
    try {
      ShellExecute shellExecutor = new ShellExecute(targetHost);
      shellExecutor.setExecuteEvent(shellExecuteEvent);
      String command = String.format("cd %s/scripts && ./injector_install.sh 2>&1", getWorkFolder());
      code = shellExecutor.run(command);
    } catch (Exception err) {
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, err.getMessage());
    }
    if (!Objects.equals(0, code)) {
      throw new SmartMonException(SmartMonErrno.SSH_COMMAND_ERROR, "install injector error");
    }
  }

  private String getWorkFolder() {
    String folder = Strings.nullToEmpty(targetFolder).endsWith("/") ? targetFolder :
      Strings.nullToEmpty(targetFolder) + "/";
    return folder + "task-" + TaskContext.getCurrentContext().getTaskId();
  }
}
