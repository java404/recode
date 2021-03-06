package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PbDataLunDelParam {
  @JsonProperty("ignore_asmstatus")
  private Boolean asmStatus;
  @JsonProperty("ignore_vote")
  private Boolean vote;
}
