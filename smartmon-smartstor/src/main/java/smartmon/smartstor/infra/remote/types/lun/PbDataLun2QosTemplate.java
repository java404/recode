package smartmon.smartstor.infra.remote.types.lun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PbDataLun2QosTemplate {
  @JsonProperty("qostemplate_id")
  private String qosTemplateId;
  @JsonProperty("node_id")
  private String nodeId;
  @JsonProperty("ext_qostemplate_name")
  private String extQosTemplateName;
  @JsonProperty("ext_node_name")
  private String extNodeName;
}
