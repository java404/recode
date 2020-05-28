package smartmon.smartstor.interfaces.web.representation;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartmon.smartstor.domain.gateway.repository.StorageHostRepository;
import smartmon.smartstor.domain.model.Group;
import smartmon.smartstor.domain.model.GroupNode;
import smartmon.smartstor.domain.model.Lun;
import smartmon.smartstor.domain.model.StorageHost;
import smartmon.smartstor.domain.model.enums.SysModeEnum;
import smartmon.smartstor.infra.cache.CachedData;
import smartmon.smartstor.infra.cache.DataCacheManager;
import smartmon.smartstor.interfaces.web.representation.dto.GroupDto;
import smartmon.smartstor.interfaces.web.representation.dto.GroupNodeDto;
import smartmon.utilities.misc.BeanConverter;

@Slf4j
@Service
public class GroupsRepresentationService {
  @Autowired
  private DataCacheManager dataCacheManager;
  @Autowired
  private StorageHostRepository storageHostRepository;

  public CachedData<GroupDto> getGroups() {
    List<StorageHost> hosts = getGroupHosts();
    if (CollectionUtils.isEmpty(hosts)) {
      return null;
    }
    List<GroupDto> groupsViewDatas = new ArrayList<>();
    CachedData<GroupDto> result = new CachedData<>(groupsViewDatas, false, "");
    Map<String, String> hostIdIpMap = hosts
      .stream()
      .collect(Collectors.toMap(StorageHost::getHostId, StorageHost::getListenIp, (a, b) -> b));
    hosts.forEach(h -> {
      try {
        String listenIp = h.getListenIp();
        CachedData<Group> groupCachedData = getGroupCachedData(result, listenIp);
        if (groupCachedData == null) {
          return;
        }
        CachedData<Lun> lunCachedData = getLunCachedData(result, listenIp);
        List<Group> groups = groupCachedData.getData();
        groups.forEach(g -> generateGroupDto(groupsViewDatas, h, lunCachedData, g, hostIdIpMap));
      } catch (Exception e) {
        log.warn("Get group failed:{}", h.getListenIp(), e);
      }
    });
    return result;
  }

  private CachedData<Lun> getLunCachedData(CachedData<GroupDto> result, String listenIp) {
    CachedData<Lun> lunCachedData = dataCacheManager.gets(listenIp, Lun.class);
    if (lunCachedData != null && lunCachedData.isExpired()) {
      result.setExpired(lunCachedData.isExpired());
      result.setError(lunCachedData.getError());
    }
    return lunCachedData;
  }

  private CachedData<Group> getGroupCachedData(CachedData<GroupDto> result, String listenIp) {
    CachedData<Group> groupCachedData = dataCacheManager.gets(listenIp, Group.class);
    if (groupCachedData == null || CollectionUtils.isEmpty(groupCachedData.getData())) {
      return null;
    }
    if (groupCachedData.isExpired()) {
      result.setExpired(groupCachedData.isExpired());
      result.setError(groupCachedData.getError());
    }
    return groupCachedData;
  }

  private void generateGroupDto(List<GroupDto> groupsViewDatas,
                                StorageHost h,
                                CachedData<Lun> lunCachedData,
                                Group g, Map<String, String> hostIdIpMap) {
    List<GroupNode> nodes = CollectionUtils.isEmpty(g.getNodes()) ? Lists.newArrayList() : g.getNodes();
    GroupDto groupDto = new GroupDto();
    groupDto.setGroupName(g.getGroupName());
    groupDto.setServiceIp(h.getListenIp());
    groupDto.setHostName(h.getHostname());
    groupDto.setNodeName(h.getNodeName());
    groupDto.setNodeCount(CollectionUtils.isEmpty(nodes) ? 0L : (long)nodes.size());
    groupDto.setLunCount(getLunCount(lunCachedData, g.getGroupName()));
    List<GroupNodeDto> nodeDtos = new ArrayList<>();
    nodes.forEach(node -> {
      GroupNodeDto nodeDto = BeanConverter.copy(node, GroupNodeDto.class);
      String listenIp = hostIdIpMap.get(node.getHostId());
      nodeDto.setListenIp(StringUtils.isNotBlank(listenIp) ? listenIp : node.getHostId());
      nodeDtos.add(nodeDto);
    });
    List<GroupNodeDto> groupNodeDtos = new ArrayList<>(nodeDtos);
    groupDto.setGroupNodes(groupNodeDtos);

    groupsViewDatas.add(groupDto);
  }

  private Long getLunCount(CachedData<Lun> lunCachedData, String groupName) {
    if (lunCachedData == null || CollectionUtils.isEmpty(lunCachedData.getData())) {
      return 0L;
    }
    try {
      List<Lun> data = lunCachedData.getData();
      return data.stream().filter(d -> groupName.equalsIgnoreCase(d.getGroupName())).count();
    } catch (Exception e) {
      return 0L;
    }
  }

  private List<StorageHost> getGroupHosts() {
    List<StorageHost> hosts = storageHostRepository.getAll();
    if (CollectionUtils.isEmpty(hosts)) {
      return null;
    }
    hosts = hosts
      .stream()
      .filter(h -> SysModeEnum.isIos(h.getSysMode()))
      .collect(Collectors.toList());
    return hosts;
  }
}
