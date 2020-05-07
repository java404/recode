package smartmon.falcon.remote.request;

import feign.Param;
import feign.RequestLine;
import smartmon.falcon.remote.types.template.FalconHostGroupTemplate;
import smartmon.falcon.remote.types.template.FalconTemplates;

public interface FalconRequestTemplateManager {
  @RequestLine("GET /template")
  FalconTemplates listTemplates();

  @RequestLine("GET /hostgroup/{groupId}/template")
  FalconHostGroupTemplate getTemplatesByGroupId(@Param("groupId") Integer groupId);
}
