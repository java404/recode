package smartmon.smartstor.infra.remote.requests;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.smartstor.infra.remote.types.PbDataResponseCode;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupAddNodeParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupAddParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupConfigParam;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupInfo;
import smartmon.smartstor.infra.remote.types.group.PbDataGroupInfos;

public interface PbDataRequestGroupManager {

  @RequestLine("GET /instances/initgroups")
  PbDataGroupInfos listGroups();

  @RequestLine("GET /instances/initgroups/{initgroup_name}")
  @Headers({"Content-Type: application/json"})
  PbDataGroupInfo getGroupInfoByName(@Param("initgroup_name") String groupName);

  @RequestLine("GET /instances/initgroups/id/{initgroup_id}")
  @Headers({"Content-Type: application/json"})
  PbDataGroupInfo getGroupInfoById(@Param("initgroup_id") String groupId);

  @RequestLine("PATCH /instances/initgroups/{initgroup_name}")
  @Headers({"Content-Type: application/json", "Accept: application/json"})
  PbDataResponseCode groupConfig(@RequestBody PbDataGroupConfigParam groupConfigParam,
                                 @Param("initgroup_id") String groupId);

  @RequestLine("POST /instances/initgroups")
  @Headers({"Content-Type: application/json"})
  PbDataGroupInfo groupAdd(@RequestBody PbDataGroupAddParam groupAddParam);

  @RequestLine("DELETE /instances/initgroups/{initgroup_name}")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode groupDel(@Param("initgroup_name") String groupName);

  @RequestLine("POST /instances/initgroups/{initgroup_name}/nodes")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode groupAddNode(@RequestBody PbDataGroupAddNodeParam groupAddNodeParam,
                                  @Param("initgroup_name") String groupName);

  @RequestLine("DELETE /instances/initgroups/{initgroup_name}/nodes/{node_name}")
  @Headers({"Content-Type: application/json"})
  PbDataResponseCode groupDelNode(@Param("initgroup_name") String groupName,
                                  @Param("node_name") String nodeName);
}
