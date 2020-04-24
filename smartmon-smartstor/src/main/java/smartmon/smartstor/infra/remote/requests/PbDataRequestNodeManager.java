package smartmon.smartstor.infra.remote.requests;

import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeConfigParam;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeInfo;
import smartmon.smartstor.infra.remote.types.node.PbDataNodeInfos;

public interface PbDataRequestNodeManager {
  @RequestLine("GET /instances/node/list")
  PbDataNodeInfos listNodes();

  @RequestLine("GET /instances/node")
  PbDataNodeInfo getNodeInfo();

  @RequestLine("PATCH /instances/node")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode nodeConfig(@RequestBody PbDataNodeConfigParam param);
}
