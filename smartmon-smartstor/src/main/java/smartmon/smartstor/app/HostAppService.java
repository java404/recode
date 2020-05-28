package smartmon.smartstor.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.app.command.HostInitCommand;
import smartmon.smartstor.domain.service.HostInitService;

@Service
@Slf4j
public class HostAppService {
  @Autowired
  private HostInitService hostInitService;

  public void initHost(HostInitCommand hostInitCommand) {
    hostInitService.initHost(hostInitCommand);
  }
}
