package smartmon.falcon.remote;

import org.junit.Ignore;
import org.junit.Test;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.types.graph.FalconGraphHistoryQueryParam;
import smartmon.falcon.remote.types.graph.FalconGraphRecord;
import smartmon.utilities.misc.TargetHost;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphRemoteTest {

  @Ignore
  @Test
  public void getGraphHistory() {
    final TargetHost targetHost = TargetHost.builder("172.24.8.132", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    final FalconGraphHistoryQueryParam queryParam = new FalconGraphHistoryQueryParam();
    Set<String> hostnames = new HashSet<>();
    hostnames.add("host132");
    hostnames.add("host133");
    hostnames.add("host134");
    Set<String> counters = new HashSet<>();
    counters.add("cpu.busy");
    counters.add("mem.memused.percent");
    queryParam.setCounters(counters);
    queryParam.setHosts(hostnames);
    queryParam.setEndTime(1588867200L);
    queryParam.setStartTime(1588262400L);
    queryParam.setMergerType("AVERAGE");
    queryParam.setStep(60);
    final List<FalconGraphRecord> graphHistory = falconClient.getGraphHistory(queryParam);
    for (FalconGraphRecord falconGraphRecord : graphHistory) {
      System.out.println(falconGraphRecord);
    }
  }
}
