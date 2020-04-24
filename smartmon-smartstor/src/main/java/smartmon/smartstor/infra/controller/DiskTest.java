package smartmon.smartstor.infra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartmon.smartstor.domain.model.Disk;
import smartmon.smartstor.infra.remote.SmartstorApiProxy;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("api/v1")
public class DiskTest {

  @Autowired
  private SmartstorApiProxy apiProxy;

  @GetMapping("disk/list")
  @ResponseBody
  public List<Disk> getDisks() {
    return apiProxy.getDisks(Collections.singletonList("172.24.12.218"));
  }

  @GetMapping("disk/info")
  @ResponseBody
  public Disk getDiskInfo() {
    return apiProxy.getDiskInfo("172.24.12.218", "hd01");
  }
}
