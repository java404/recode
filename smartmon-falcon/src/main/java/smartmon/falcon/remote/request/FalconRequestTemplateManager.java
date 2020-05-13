package smartmon.falcon.remote.request;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.template.FalconActionUpdateParam;
import smartmon.falcon.remote.types.template.FalconHostGroupTemplate;
import smartmon.falcon.remote.types.template.FalconTemplateInfo;
import smartmon.falcon.remote.types.template.FalconTemplates;

@Headers({"Content-Type: application/json"})
public interface FalconRequestTemplateManager {
  @RequestLine("GET /template")
  FalconTemplates listTemplates(@HeaderMap Map<String, String> falconApiToken);

  @RequestLine("GET /hostgroup/{groupId}/template")
  FalconHostGroupTemplate getTemplatesByGroupId(@Param("groupId") Integer groupId,
                                                @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("GET /template/{templateId}")
  @Headers({"Content-Type: application/json"})
  FalconTemplateInfo getTemplateInfoById(@Param("templateId") Integer templateId,
                                         @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("PUT /template/action")
  @Headers({"Content-Type: application/json"})
  FalconResponseData updateTemplateAction(@RequestBody FalconActionUpdateParam falconActionUpdateParam,
                                          @HeaderMap Map<String, String> falconApiToken);

}
