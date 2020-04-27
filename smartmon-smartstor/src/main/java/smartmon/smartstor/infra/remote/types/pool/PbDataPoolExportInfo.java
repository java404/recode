package smartmon.smartstor.infra.remote.types.pool;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PbDataPoolExportInfo {
  @JsonProperty("pool_name")
  private String exportPoolName;
  @JsonProperty("state_str")
  private String exportStateStr;
  private Long exportValid;
  @JsonProperty("p_valid")
  private Double exportPValid;
  private Long exportDirty;
  @JsonProperty("p_dirty")
  private Double exportPDirty;
  private Long exportError;
  @JsonProperty("lower_thresh")
  private Long exportLowerThresh;
  @JsonProperty("p_lower_thresh")
  private Integer exportPLowerThresh;
  @JsonProperty("upper_thresh")
  private Long exportUpperThresh;
  @JsonProperty("p_upper_thresh")
  private Integer exportPUpperThresh;
  private Long exportSize;
  @JsonProperty("max_size")
  private Long exportMaxSize;
  @JsonProperty("dev_name")
  private List<String> exportDevName;
  private Long exportExtent;
  private Long exportBucket;
  private Long exportSippet;
  private Long exportState;
  @JsonProperty("state_exp")
  private String stateExp;
  @JsonProperty("is_variable")
  private Boolean exportIsVariable;

  private Integer getExportState() {
    return this.exportState != null ? this.exportState.intValue() : null;
  }

  public Double getExportPLowerThresh() {
    return this.exportPLowerThresh != null ? this.exportPLowerThresh.doubleValue() : null;
  }

  public Double getExportPUpperThresh() {
    return this.exportPUpperThresh != null ? this.exportPUpperThresh.doubleValue() : null;
  }
}
