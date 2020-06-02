package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataHddDiskHealthInfo {
  @JsonProperty("dev_name")
  private String devName;
  @JsonProperty("last_heartbeat_time")
  private Long lastHeartbeatTime;
  @JsonProperty("verifies_gb")
  private String verifiesGb;
  @JsonProperty("life_left")
  private String lifeLeft;
  @JsonProperty("uncorrected_reads")
  private String uncorrectedReads;
  @JsonProperty("uncorrected_verifies")
  private String uncorrectedVerifies;
  @JsonProperty("corrected_reads")
  private String correctedReads;
  @JsonProperty("writes_gb")
  private String writesGb;
  @JsonProperty("load_cycle_pct_left")
  private String loadCyclePctLeft;
  @JsonProperty("load_cycle_count")
  private String loadCycleCount;
  @JsonProperty("corrected_writes")
  private String correctedWrites;
  @JsonProperty("reallocated_sector_ct")
  private String reallocatedSectorCt;
  @JsonProperty("power_on_hours")
  private String powerOnHours;
  @JsonProperty("non_medium_errors")
  private String nonMediumErrors;
  @JsonProperty("reads_gb")
  private String readsGb;
  @JsonProperty("load_cycle_spec")
  private String loadCycleSpec;
  @JsonProperty("start_stop_pct_left")
  private String startStopPctLeft;
  @JsonProperty("uncorrected_writes")
  private String uncorrectedWrites;
  @JsonProperty("start_stop_spec")
  private String startStopSpec;
  @JsonProperty("corrected_verifies")
  private String correctedVerifies;
  @JsonProperty("start_stop_cycles")
  private String startStopCycles;

}
