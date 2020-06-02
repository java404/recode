package smartmon.vhe.deployment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.taskmanager.types.TaskGroup;
import smartmon.vhe.deployment.command.SmartstorPrecheckCommand;
import smartmon.vhe.deployment.service.SmartstorPrecheckService;
import smartmon.vhe.deployment.service.SmartstorTemplateService;

@Service
public class SmartstorService {
  @Autowired
  private SmartstorTemplateService smartstorTemplateService;
  @Autowired
  private SmartstorPrecheckService smartstorPrecheckService;

  public String getTemplate(Long fileId) {
    return smartstorTemplateService.getTemplate(fileId);
  }

  public TaskGroup precheck(List<SmartstorPrecheckCommand> commands) {
    return smartstorPrecheckService.precheck(commands);
  }
}
