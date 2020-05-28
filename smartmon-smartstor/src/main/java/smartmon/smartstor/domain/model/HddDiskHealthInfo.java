package smartmon.smartstor.domain.model;

import lombok.Data;

@Data
public class HddDiskHealthInfo {
  private String devName;
  private Long lastHeartbeatTime;
  private String verifiesGb;
  private String lifeLeft;
  private String uncorrectedReads;
  private String uncorrectedVerifies;
  private String correctedReads;
  private String writesGb;
  private String loadCyclePctLeft;
  private String loadCycleCount;
  private String correctedWrites;
  private String reallocatedSectorCt;
  private String powerOnHours;
  private String nonMediumErrors;
  private String readsGb;
  private String loadCycleSpec;
  private String startStopPctLeft;
  private String uncorrectedWrites;
  private String startStopSpec;
  private String correctedVerifies;
  private String startStopCycles;

}
