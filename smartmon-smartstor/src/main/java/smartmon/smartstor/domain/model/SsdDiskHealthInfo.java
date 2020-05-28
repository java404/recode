package smartmon.smartstor.domain.model;

import lombok.Data;

@Data
public class SsdDiskHealthInfo {
  private String devName;
  private Long lastHeartbeatTime;
  private String life;
  private String offlineUncorrectable;
  private String reallocatedEventCount;
  private String reallocatedSectorCt;
  private String powerOnHours;
  private String temperatureCelsius;
  private String rawReadErrorRate;
  private String totallife;
  private String mediaWearoutIndicator;
  private String spinRetryCount;
  private String commandTimeout;
  private String uncorrectableSectorCt;
  private String ssdLifeLeft;
}
