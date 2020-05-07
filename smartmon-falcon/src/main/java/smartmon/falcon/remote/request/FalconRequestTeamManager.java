package smartmon.falcon.remote.request;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.team.FalconTeamCreateParam;
import smartmon.falcon.remote.types.team.FalconTeamInfo;
import smartmon.falcon.remote.types.team.FalconTeamUpdateParam;

import java.util.List;

public interface FalconRequestTeamManager {
  @RequestLine("GET /team")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"g\"}"})
  List<FalconTeamInfo> listTeams();

  @RequestLine("POST /team")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"g\"}"})
  FalconResponseData createTeam(@RequestBody FalconTeamCreateParam createParam);

  @RequestLine("PUT /team")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"g\"}"})
  FalconResponseData updateTeam(@RequestBody FalconTeamUpdateParam updateParam);

  @RequestLine("DELETE /team/{id}")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"g\"}"})
  FalconResponseData deleteTeam(@Param("id") Integer id);

  @RequestLine("GET /team/t/{teamId}")
  @Headers({"Content-Type: application/json", "ApiToken: { \"name\":\"root\" , \"sig\":\"g\"}"})
  FalconTeamInfo getTeamInfoByTeamId(@Param("teamId") Integer teamId);
}
