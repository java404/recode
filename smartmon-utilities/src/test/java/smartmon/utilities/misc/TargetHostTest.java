package smartmon.utilities.misc;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;


public class TargetHostTest {
  @Test
  public void testBuilder() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.8", 22).build();
    Assert.assertEquals(targetHost.getAddress(), "172.24.12.8");
    Assert.assertEquals(targetHost.getPort(), 22);

    final Map<TargetHost, String> hosts = new HashMap<>();
    hosts.put(targetHost, "targetHost1");
    final TargetHost targetHost2 = TargetHost.builder("172.24.12.8", 22).build();
    final String result = hosts.getOrDefault(targetHost2, "bad-data");
    Assert.assertEquals(result, "targetHost1");
    Assert.assertEquals(targetHost2, targetHost);
  }
}