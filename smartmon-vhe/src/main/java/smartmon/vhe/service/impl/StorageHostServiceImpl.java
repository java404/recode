package smartmon.vhe.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.vhe.exception.FeignClientException;
import smartmon.vhe.exception.HostInitException;
import smartmon.vhe.service.StorageHostService;
import smartmon.vhe.service.dto.SmartmonCoreHostAddResDto;
import smartmon.vhe.service.dto.StorageHostDto;
import smartmon.vhe.service.feign.SmartStorFeignClient;
import smartmon.vhe.service.feign.SmartmonCoreFeignClient;
import smartmon.vhe.service.feign.types.SmartMonHostAddParam;

@Service
@Slf4j
public class StorageHostServiceImpl implements StorageHostService {
  @Autowired
  private SmartmonCoreFeignClient coreFeignClient;
  @Autowired
  private SmartStorFeignClient smartStorFeignClient;

  @Override
  public Boolean init(List<StorageHostDto> hosts) {
    List<SmartmonCoreHostAddResDto> coreHosts = addHostToCore(hosts);
    addHostToSmartStor(hosts, coreHosts);
    return true;
  }

  private void addHostToSmartStor(List<StorageHostDto> hosts,
                                  List<SmartmonCoreHostAddResDto> coreHosts) {
    try {
      generateHostUUid(coreHosts, hosts);
      smartStorFeignClient.saveHosts(hosts);
    } catch (FeignClientException e) {
      log.error("Add host to smartstor failed", e);
      throw new HostInitException(e.getMessage());
    }
  }

  private List<SmartmonCoreHostAddResDto> addHostToCore(List<StorageHostDto> hosts) {
    List<SmartmonCoreHostAddResDto> coreHosts;
    try {
      List<SmartMonHostAddParam> addParamList = hosts
        .stream()
        .map(StorageHostDto::getHostAddParam)
        .collect(Collectors.toList());
      coreHosts = coreFeignClient
        .addHosts(addParamList)
        .getContent();
      if (CollectionUtils.isEmpty(coreHosts)) {
        throw new HostInitException("Save to core failed: empty response");
      }
    } catch (FeignClientException e) {
      log.error("Add host to core failed", e);
      throw new HostInitException(e.getMessage());
    }
    return coreHosts;
  }

  private void generateHostUUid(List<SmartmonCoreHostAddResDto> coreHosts,
                                List<StorageHostDto> hosts) {
    Map<String, String> ipUuidMap = coreHosts
      .stream()
      .collect(Collectors.toMap(getGetManageIp(), getGetHostUuid()));
    hosts.forEach(host -> {
      String uuid = ipUuidMap.get(host.getListenIp());
      if (StringUtils.isBlank(uuid)) {
        return;
      }
      host.setGuid(uuid);
    });
  }

  private Function<SmartmonCoreHostAddResDto, String> getGetHostUuid() {
    return SmartmonCoreHostAddResDto::getHostUuid;
  }

  private Function<SmartmonCoreHostAddResDto, String> getGetManageIp() {
    return SmartmonCoreHostAddResDto::getManageIp;
  }

}
