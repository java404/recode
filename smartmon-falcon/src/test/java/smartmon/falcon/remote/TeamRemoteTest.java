package smartmon.falcon.remote;

import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.types.FalconApiToken;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.team.FalconTeamInfo;
import smartmon.falcon.remote.types.team.FalconTeamUpdateParam;
import smartmon.utilities.misc.JsonConverter;
import smartmon.utilities.misc.TargetHost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamRemoteTest {

  @Test
  @Ignore
  public void listTeamsTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.55", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    Map<String, String> map = new HashMap<>();
    final FalconApiToken root = new FalconApiToken("root", "default-token-used-in-server-side");
    map.put("ApiToken", JsonConverter.writeValueAsStringQuietly(root));
    final List<FalconTeamInfo> falconTeamInfos = falconClient.listTeams(map);
    for (FalconTeamInfo falconTeamInfo : falconTeamInfos) {
      System.out.println(falconTeamInfo);
    }
  }

  @Test
  @Ignore
  public void updateTeamTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.55", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconTeamUpdateParam teamUpdateParam = new FalconTeamUpdateParam();
    teamUpdateParam.setId(2);
    teamUpdateParam.setName("string");
    teamUpdateParam.setResume("123456");
    teamUpdateParam.setUsers(Lists.newArrayList());
    Map<String, String> map = new HashMap<>();
    final FalconApiToken root = new FalconApiToken("root", "default-token-used-in-server-side");
    map.put("ApiToken", JsonConverter.writeValueAsStringQuietly(root));
    final FalconResponseData responseData = falconClient.updateTeam(teamUpdateParam, map);
  }
}
