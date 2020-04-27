package smartmon.smartstor.infra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.domain.model.Group;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.domain.model.Pool;
import smartmon.smartstor.infra.remote.SmartstorApiProxy;
import java.util.List;

@Controller
@RequestMapping("api/v1")
public class PbDataTest {

  @Autowired
  private SmartstorApiProxy apiProxy;

  @GetMapping("disk/list")
  @ResponseBody
  public List<Disk> getDisks() {
    return apiProxy.getDisks("172.24.12.218");
  }

  @GetMapping("disk/info")
  @ResponseBody
  public Disk getDiskInfo() {
    return apiProxy.getDiskInfo("172.24.12.218", "hd01");
  }

  @GetMapping("group/list")
  @ResponseBody
  public List<Group> getGroups() {
    return apiProxy.getGroups("172.24.12.218");
  }

  @GetMapping("pool/list")
  @ResponseBody
  public List<Pool> getPools() {
    return apiProxy.getPools("172.24.12.218");
  }

  @GetMapping("lun/list")
  @ResponseBody
  public List<Lun> getLuns() {
    return apiProxy.getLuns("172.4.12.218");
  }
}
