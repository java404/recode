package smartmon.vhe.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import smartmon.vhe.controller.vo.HostInitParamVo;
import smartmon.vhe.service.feign.types.SmartMonHostAddParam;

@Data
public class VheStorageHostInitDto {
  private String guid;
  private String hostId;
  private String listenIp;
  private String sysUsername;
  private String sysPassword;
  private Integer size;
  private String idcName;

  public static VheStorageHostInitDto convert(HostInitParamVo vo) {
    VheStorageHostInitDto dto = new VheStorageHostInitDto();
    try {
      BeanUtils.copyProperties(vo, dto);
      dto.setListenIp(vo.getServiceIp());
    } catch (Exception e) {
      dto = null;
    }
    return dto;
  }

  public static List<VheStorageHostInitDto> convertToList(List<HostInitParamVo> nodesVo) {
    List<VheStorageHostInitDto> hosts = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(nodesVo)) {
      nodesVo.forEach(node -> {
        VheStorageHostInitDto convertNode = convert(node);
        if (convertNode != null) {
          hosts.add(convertNode);
        }
      });
    }
    return hosts;
  }

  public static SmartMonHostAddParam getHostAddParam(VheStorageHostInitDto vheStorageHostInitDto) {
    SmartMonHostAddParam param = new SmartMonHostAddParam();
    param.setManageIp(vheStorageHostInitDto.getListenIp());
    param.setSysUsername(vheStorageHostInitDto.getSysUsername());
    param.setSysPassword(vheStorageHostInitDto.getSysPassword());
    return param;
  }
}
