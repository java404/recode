package smartmon.falcon.remote.request;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import smartmon.falcon.remote.types.template.FalconHostGroupTemplate;
import smartmon.falcon.remote.types.template.FalconTemplates;

public interface FalconRequestTemplateManager {
  @RequestLine("GET /template")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  FalconTemplates listTemplates();

  @RequestLine("GET /hostgroup/{groupId}/template")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"default-token-used-in-server-side\"}"})
  FalconHostGroupTemplate getTemplatesByGroupId(@Param("groupId") Integer groupId);
}
