package smartmon.core.hosts;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HardwareInfo {
  private CpuInfo cpuInfo;
  private MemInfo memInfo;
  private MainboardInfo mainboardInfo;
  private List<MountInfo> mountInfos;

  @Getter
  @Setter
  public static class CpuInfo {
    private String processorModel;
    private Integer processorCores;
    private Integer processorCount;
    private Integer threadsPerCore;
    private Integer logicProcessorCount;
  }

  @Getter
  @Setter
  public static class MemInfo {
    private Long memoryTotal;
    private Long swapTotal;
  }

  @Getter
  @Setter
  public static class MainboardInfo {
    private String biosVersion;
  }

  @Getter
  @Setter
  public static class MountInfo {
    private String device;
    private String fsType;
    private String mountPoint;
    private Long totalSize;
    private Long availableSize;
  }
}
