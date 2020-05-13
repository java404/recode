package smartmon.falcon.remote.request;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.host.FalconHostGroup;
import smartmon.falcon.remote.types.host.FalconHostGroupCreateParam;
import smartmon.falcon.remote.types.host.FalconHostGroupQueryParam;
import smartmon.falcon.remote.types.host.FalconHostGroupUpdateParam;
import smartmon.falcon.remote.types.host.FalconHosts;


@Headers({"Content-Type: application/json"})
public interface FalconRequestHostGroupManager {
  @RequestLine("GET /hostgroup")
  List<FalconHostGroup> listHostGroups(@HeaderMap Map<String, String> map);

  @RequestLine("GET /hostgroup")
  List<FalconHostGroup> listHostGroupsByGroupRegex(@QueryMap FalconHostGroupQueryParam queryParam,
                                                   @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("POST /hostgroup")
  FalconHostGroup createHostGroup(@RequestBody FalconHostGroupCreateParam hostGroupCreateParam,
                                  @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("PUT /hostgroup")
  FalconResponseData updataHostGroup(@RequestBody FalconHostGroupUpdateParam hostGroupUpdateParam,
                                     @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("DELETE /hostgroup/{id}")
  FalconResponseData delHostGroup(@Param("id") Integer id,
                                  @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("GET /hostgroup/{groupId}")
  FalconHosts getHostsByGroupId(@Param("groupId") Integer groupId,
                                @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("PATCH /hostgroup/{groupId}/host")
  FalconResponseData hostToHostGroupOpt(@RequestBody Set<String> hosts,
                                        @Param("groupId") Integer groupId,
                                        @HeaderMap Map<String, String> falconApiToken);
}
