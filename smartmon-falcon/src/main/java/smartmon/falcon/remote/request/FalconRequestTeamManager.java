package smartmon.falcon.remote.request;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.team.FalconTeamCreateParam;
import smartmon.falcon.remote.types.team.FalconTeamInfo;
import smartmon.falcon.remote.types.team.FalconTeamUpdateParam;
import smartmon.falcon.remote.types.team.FalconTeamUserInfo;

@Headers({"Content-Type: application/json"})
public interface FalconRequestTeamManager {
  @RequestLine("GET /team")
  List<FalconTeamInfo> listTeams(@HeaderMap Map<String, String> falconApiToken);

  @RequestLine("POST /team")
  FalconResponseData createTeam(@RequestBody FalconTeamCreateParam createParam,
                                @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("PUT /team")
  FalconResponseData updateTeam(@RequestBody FalconTeamUpdateParam updateParam,
                                @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("DELETE /team/{id}")
  FalconResponseData deleteTeam(@Param("id") Integer id,
                                @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("GET /team/t/{teamId}")
  FalconTeamUserInfo getTeamInfoByTeamId(@Param("teamId") Integer teamId,
                                         @HeaderMap Map<String, String> falconApiToken);


}
