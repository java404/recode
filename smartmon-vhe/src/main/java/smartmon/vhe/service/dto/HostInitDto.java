package smartmon.vhe.service.dto;

import lombok.Data;

@Data
public class HostInitDto {
  private String hostId;
  private String listenIp;
  private String sysUsername;
  private String sysPassword;
  private Integer sshPort;
  private String ipmiAddress;
  private String ipmiUsername;
  private String ipmiPassword;
  private Integer size;
  private String idcName;

  public HostRegistrationDto getHostRegistrationDto() {
    HostRegistrationDto dto = new HostRegistrationDto();
    dto.setManageIp(this.getListenIp());
    dto.setSysUsername(this.getSysUsername());
    dto.setSysPassword(this.getSysPassword());
    dto.setSshPort(this.getSshPort());
    dto.setIpmiAddress(this.getIpmiAddress());
    dto.setIpmiUsername(this.getIpmiUsername());
    dto.setIpmiPassword(this.getIpmiPassword());
    return dto;
  }

  public StorageHostInitDto getStorageHostRegistrationDto() {
    StorageHostInitDto dto = new StorageHostInitDto();
    dto.setListenIp(this.getListenIp());
    dto.setHostId(this.getHostId());
    return dto;
  }

  public HostAddToRackDto getHostAddToRackDto() {
    HostAddToRackDto dto = new HostAddToRackDto();
    dto.setIdcName(this.getIdcName());
    dto.setSize(this.getSize());
    return dto;
  }
}
