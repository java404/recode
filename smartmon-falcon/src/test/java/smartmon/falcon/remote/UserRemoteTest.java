package smartmon.falcon.remote;

import org.junit.Ignore;
import org.junit.Test;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.types.user.FalconUserCreateParam;
import smartmon.utilities.misc.TargetHost;

public class UserRemoteTest {

  @Ignore
  @Test
  public void userOptTest() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.55", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconUserCreateParam userCreateParam = new FalconUserCreateParam("w","w","w","w@qq.com");
    falconClient.createUser(null, null);
  }

  @Ignore
  @Test
  public void test() {
    System.out.println(System.currentTimeMillis()/1000);
  }
}
