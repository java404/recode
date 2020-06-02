package smartmon.agent.client;

import java.io.File;
import java.util.List;
import java.util.Map;

import smartmon.core.hosts.SmartMonHostInfo;
import smartmon.taskmanager.vo.TaskGroupVo;

public interface AgentClientService {
  Boolean isInjectorHealthy(String serviceIp);

  void uploadFiles(String serviceIp, Map<String, File> files);

  String executeShellCommand(String serviceIp, String command);

  TaskGroupVo getTaskResult(String serviceIp, String taskGroupId);

  SmartMonHostInfo getHostInfo(String serviceIp);

  String getNetInterfaces(String serviceIp);

  List<String> getMonitorInterfaces(String serviceIp);

  void configMonitorInterfaces(String serviceIp, List<String> monitorNetInterfaces);
}
