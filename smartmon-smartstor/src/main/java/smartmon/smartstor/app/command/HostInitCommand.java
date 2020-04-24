package smartmon.smartstor.app.command;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import smartmon.smartstor.app.Command;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.interfaces.web.controller.vo.HostInitVo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class HostInitCommand extends Command {
  private List<StorageHost> hosts = new ArrayList<>();

  public static HostInitCommand convertToCommand(List<HostInitVo> hostsVo) {
    HostInitCommand command = new HostInitCommand();
    List<StorageHost> hosts = command.getHosts();
    hostsVo.forEach(vo -> {
      StorageHost host = new StorageHost();
      try {
        BeanUtils.copyProperties(vo, host);
        hosts.add(host);
      } catch (Exception e) {
        log.warn("Copy failed", e);
      }
    });
    return command;
  }
}
