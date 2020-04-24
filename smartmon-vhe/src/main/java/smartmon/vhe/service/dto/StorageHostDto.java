package smartmon.vhe.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import smartmon.vhe.controller.vo.HostInitParamVo;
import smartmon.vhe.service.feign.types.SmartMonHostAddParam;

@Data
public class StorageHostDto {
  private String guid;
  private String hostId;
  private String listenIp;

  public static StorageHostDto convert(HostInitParamVo vo) {
    StorageHostDto dto = new StorageHostDto();
    try {
      BeanUtils.copyProperties(vo, dto);
      dto.setListenIp(vo.getServiceIp());
    } catch (Exception e) {
      dto = null;
    }
    return dto;
  }

  public static List<StorageHostDto> convertToList(List<HostInitParamVo> nodesVo) {
    List<StorageHostDto> hosts = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(nodesVo)) {
      nodesVo.forEach(node -> {
        StorageHostDto convertNode = convert(node);
        if (convertNode != null) {
          hosts.add(convertNode);
        }
      });
    }
    return hosts;
  }

  public static SmartMonHostAddParam getHostAddParam(StorageHostDto storageHostDto) {
    SmartMonHostAddParam param = new SmartMonHostAddParam();
    param.setManageIp(storageHostDto.getListenIp());
    return param;
  }
}
