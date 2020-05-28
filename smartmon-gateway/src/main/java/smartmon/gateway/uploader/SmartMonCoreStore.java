package smartmon.gateway.uploader;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.core.store.SmartMonStoreFile;
import smartmon.core.store.SmartMonStoreInputFile;
import smartmon.utilities.general.SmartMonResponse;

@FeignClient(name = "smartmon-core", path = "${smartmon.api.prefix.core:/core/api/v2}")
public interface SmartMonCoreStore {
  @PostMapping(value = "/store", consumes = MediaType.APPLICATION_JSON_VALUE)
  SmartMonResponse<SmartMonStoreFile> put(@RequestBody SmartMonStoreInputFile inputFile);
}
