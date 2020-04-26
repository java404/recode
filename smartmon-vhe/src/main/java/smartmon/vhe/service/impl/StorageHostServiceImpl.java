package smartmon.vhe.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.core.hosts.SmartMonHost;
import smartmon.core.racks.vo.IdcRackAllocateVo;
import smartmon.core.racks.vo.RackAllocationVo;
import smartmon.smartstor.web.dto.StorageHostDto;
import smartmon.utilities.misc.BeanConverter;
import smartmon.vhe.exception.FeignClientException;
import smartmon.vhe.exception.HostInitException;
import smartmon.vhe.service.StorageHostService;
import smartmon.vhe.service.dto.VheStorageHostDto;
import smartmon.vhe.service.dto.VheStorageHostInitDto;
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
  public Boolean init(List<VheStorageHostInitDto> hosts) {
    List<SmartMonHost> coreHosts = addHostToCore(hosts);
    addHostToSmartStor(hosts, coreHosts);
    addRackInfo(hosts);
    return true;
  }

  @Override
  public List<VheStorageHostDto> listAll() {
    List<VheStorageHostDto> hosts = new ArrayList<>();
    List<StorageHostDto> smartstorHosts = smartStorFeignClient.getStorageHosts().getContent();
    List<RackAllocationVo> racks = coreFeignClient.getRacks().getContent();
    Map<String, RackAllocationVo> hostIdRackMap = racks
      .stream()
      .collect(Collectors.toMap(RackAllocationVo::getHostUuid, Function.identity(), (oldValue, newValue) -> newValue));
    smartstorHosts.forEach(h -> {
      VheStorageHostDto storageHostDto = BeanConverter.copy(h, VheStorageHostDto.class);
      if (storageHostDto == null) {
        return;
      }
      RackAllocationVo rackAllocationVo = hostIdRackMap.get(storageHostDto.getGuid());
      if (rackAllocationVo != null) {
        storageHostDto.setRackInfo(rackAllocationVo);
      }
      hosts.add(storageHostDto);
    });
    return hosts;
  }

  private void addRackInfo(List<VheStorageHostInitDto> hosts) {
    try {
      List<IdcRackAllocateVo> vos = new ArrayList<>();
      hosts.forEach(h -> {
        IdcRackAllocateVo rackInfoVo = BeanConverter.copy(h, IdcRackAllocateVo.class);
        rackInfoVo.setHostUuid(h.getGuid());
        vos.add(rackInfoVo);
      });
      coreFeignClient.addHostToRackBatch(vos);
    } catch (FeignClientException e) {
      log.error("Save rack info failed");
      throw new HostInitException(e.getMessage());
    }
  }

  private void addHostToSmartStor(List<VheStorageHostInitDto> hosts,
                                  List<SmartMonHost> coreHosts) {
    try {
      generateHostUUid(coreHosts, hosts);
      smartStorFeignClient.saveHosts(hosts);
    } catch (FeignClientException e) {
      log.error("Add host to smartstor failed", e);
      throw new HostInitException(e.getMessage());
    }
  }

  private List<SmartMonHost> addHostToCore(List<VheStorageHostInitDto> hosts) {
    List<SmartMonHost> coreHosts;
    try {
      List<SmartMonHostAddParam> addParamList = hosts
        .stream()
        .map(VheStorageHostInitDto::getHostAddParam)
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

  private void generateHostUUid(List<SmartMonHost> coreHosts,
                                List<VheStorageHostInitDto> hosts) {
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

  private Function<SmartMonHost, String> getGetHostUuid() {
    return SmartMonHost::getHostUuid;
  }

  private Function<SmartMonHost, String> getGetManageIp() {
    return SmartMonHost::getManageIp;
  }

}
