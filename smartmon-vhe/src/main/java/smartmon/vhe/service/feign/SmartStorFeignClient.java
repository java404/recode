package smartmon.vhe.service.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.smartstor.web.dto.StorageHostDto;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.service.dto.VheStorageHostInitDto;

@Component
@FeignClient(name = "smartmon-smartstor", path = "${smartmon.api.prefix.smartstor:/smartstor/api/v2}")
public interface SmartStorFeignClient {

  @PostMapping(value = "/hosts/init", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse saveHosts(@RequestBody List<VheStorageHostInitDto> hosts);

  @GetMapping(value = "/hosts")
  SmartMonResponse<List<StorageHostDto>> getStorageHosts();
}
