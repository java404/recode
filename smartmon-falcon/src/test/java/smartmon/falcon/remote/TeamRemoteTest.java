package smartmon.falcon.remote;

import org.junit.Ignore;
import org.junit.Test;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.types.team.FalconTeamInfo;
import smartmon.utilities.misc.TargetHost;

import java.util.List;

public class TeamRemoteTest {

  @Test
  @Ignore
  public void listTeamsTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.55", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final List<FalconTeamInfo> falconTeamInfos = falconClient.listTeams();
    for (FalconTeamInfo falconTeamInfo : falconTeamInfos) {
      System.out.println(falconTeamInfo);
    }
  }
}
