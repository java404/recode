package smartmon.vhe.service.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.vhe.service.dto.StorageHostDto;

@FeignClient("smartmon-smartstor")
public interface SmartStorFeignClient {

  @PostMapping(value = "/api/v2/hosts/init", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse saveHosts(@RequestBody List<StorageHostDto> hosts);
}
