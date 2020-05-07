package smartmon.falcon.remote.request;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.host.FalconHostGroup;
import smartmon.falcon.remote.types.host.FalconHostGroupCreateParam;
import smartmon.falcon.remote.types.host.FalconHostGroupQueryParam;
import smartmon.falcon.remote.types.host.FalconHostGroupUpdateParam;
import smartmon.falcon.remote.types.host.FalconHosts;

import java.util.List;
import java.util.Set;

public interface FalconRequestHostGroupManager {
  @RequestLine("GET /hostgroup")
  List<FalconHostGroup> listHostGroups();

  @RequestLine("GET /hostgroup")
  @Headers({"Content-Type: application/json"})
  List<FalconHostGroup> listHostGroupsByGroupRegex(@QueryMap FalconHostGroupQueryParam queryParam);

  @RequestLine("POST /hostgroup")
  @Headers({"Content-Type: application/json"})
  FalconHostGroup createHostGroup(@RequestBody FalconHostGroupCreateParam hostGroupCreateParam);

  @RequestLine("PUT /hostgroup")
  @Headers({"Content-Type: application/json"})
  FalconResponseData updataHostGroup(@RequestBody FalconHostGroupUpdateParam hostGroupUpdateParam);

  @RequestLine("DELETE /hostgroup/{id}")
  @Headers({"Content-Type: application/json"})
  FalconResponseData delHostGroup(@Param("id") Integer id);

  @RequestLine("GET /hostgroup/{groupId}")
  @Headers({"Content-Type: application/json"})
  FalconHosts getHostsByGroupId(@Param("groupId") Integer groupId);

  @RequestLine("PATCH /hostgroup/{groupId}/host")
  @Headers({"Content-Type: application/json"})
  FalconResponseData hostToHostGroupOpt(@RequestBody Set<String> hosts, @Param("groupId") Integer groupId);

}
