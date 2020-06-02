package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataSsdDiskHealthInfo {
  @JsonProperty("dev_name")
  private String devName;
  @JsonProperty("last_heartbeat_time")
  private Long lastHeartbeatTime;
  @JsonProperty("life")
  private String life;
  @JsonProperty("offline_uncorrectable")
  private String offlineUncorrectable;
  @JsonProperty("reallocated_event_count")
  private String reallocatedEventCount;
  @JsonProperty("reallocated_sector_ct")
  private String reallocatedSectorCt;
  @JsonProperty("power_on_hours")
  private String powerOnHours;
  @JsonProperty("temperature_celsius")
  private String temperatureCelsius;
  @JsonProperty("raw_read_error_rate")
  private String rawReadErrorRate;
  @JsonProperty("totallife")
  private String totallife;
  @JsonProperty("media_wearout_indicator")
  private String mediaWearoutIndicator;
  @JsonProperty("spin_retry_count")
  private String spinRetryCount;
  @JsonProperty("command_timeout")
  private String commandTimeout;
  @JsonProperty("uncorrectable_sector_ct")
  private String uncorrectableSectorCt;
  @JsonProperty("ssd_life_left")
  private String ssdLifeLeft;
}
