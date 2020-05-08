package smartmon.smartstor.interfaces.web.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.exception.ApiVersionNotFoundException;
import smartmon.smartstor.domain.exception.ApiVersionUnsupportedException;
import smartmon.smartstor.domain.gateway.SmartstorApiService;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.ApiVersion;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.domain.model.StorageNode;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.interfaces.web.representation.dto.HostsScanDto;
import smartmon.smartstor.web.dto.StorageHostDto;
import smartmon.utilities.misc.BeanConverter;

@Slf4j
@Service
public class HostRepresentationService {
  @Autowired
  private StorageHostRepository storageHostRepository;
  @Autowired
  private SmartstorApiService smartstorApiService;

  public List<StorageHostDto> getStorageHosts() {
    List<StorageHost> storageHosts = storageHostRepository.getAll();
    List<StorageHostDto> dtos = new ArrayList<>();
    storageHosts.forEach(h -> {
      StorageHostDto hostDto = BeanConverter.copy(h, StorageHostDto.class);
      if (hostDto == null) {
        return;
      }
      hostDto.setSysModeDesc(h.getSysMode() != null ? h.getSysMode().getName() : "");
      hostDto.setNodeStatusDesc(h.getNodeStatus() != null ? h.getNodeStatus().getName() : "");
      hostDto.setTransModeDesc(h.getTransMode() != null ? h.getTransMode().getName() : "");
      dtos.add(hostDto);
    });
    return dtos;
  }

  public List<HostsScanDto> scanHosts(Set<String> serviceIps) {
    Map<String, HostsScanDto> hostsScanDtoMap = serviceIps.parallelStream().map(serviceIp -> {
      checkVersion(serviceIp);
      StorageNode storageNode = smartstorApiService.getNodeInfo(serviceIp);
      HostsScanDto hostsScanDto = new HostsScanDto();
      hostsScanDto.setServiceIp(serviceIp);
      if (storageNode != null) {
        hostsScanDto.setHostId(storageNode.getHostId());
        hostsScanDto.setSysMode(storageNode.getSysMode());
      }
      return hostsScanDto;
    }).collect(Collectors.toMap(HostsScanDto::getHostId, Function.identity(), (oldValue, newValue) -> newValue));
    for (Map.Entry<String, HostsScanDto> entry : hostsScanDtoMap.entrySet()) {
      HostsScanDto value = entry.getValue();
      if (StringUtils.isNotEmpty(value.getClusterName()) || !SysModeEnum.isIos(value.getSysMode())) {
        continue;
      }
      List<StorageHost> storageHosts = smartstorApiService.getHosts(value.getServiceIp());
      for (StorageHost storageHost : storageHosts) {
        HostsScanDto hostsScanDto = hostsScanDtoMap.get(storageHost.getHostId());
        if (hostsScanDto != null) {
          hostsScanDto.setHostname(storageHost.getHostname());
          hostsScanDto.setClusterName(storageHost.getClusterName());
        }
      }
    }
    return new ArrayList<>(hostsScanDtoMap.values());
  }

  private void checkVersion(String serviceIp) {
    ApiVersion apiVersion = smartstorApiService.getApiVersion(serviceIp);
    if (apiVersion == null) {
      throw new ApiVersionNotFoundException(String.format("Get api version failed:[%s]", serviceIp));
    }
    if (!apiVersion.hostIdEnabled()) {
      throw new ApiVersionUnsupportedException(
        String.format("Scan hosts unsupported for version:[%s]", apiVersion.getVersionValue()));
    }
  }
}
