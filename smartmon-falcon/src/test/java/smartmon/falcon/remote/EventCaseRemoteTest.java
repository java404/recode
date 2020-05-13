package smartmon.falcon.remote;

import org.junit.Ignore;
import org.junit.Test;
import smartmon.falcon.remote.client.FalconClient;
import smartmon.falcon.remote.types.FalconApiToken;
import smartmon.falcon.remote.types.alarm.FalconEventCasesQueryParam;
import smartmon.falcon.remote.types.alarm.FalconEventCases;
import smartmon.falcon.remote.types.alarm.FalconEvents;
import smartmon.falcon.remote.types.alarm.FalconEventsQueryParam;
import smartmon.utilities.misc.JsonConverter;
import smartmon.utilities.misc.TargetHost;

import java.util.HashMap;
import java.util.Map;

public class EventCaseRemoteTest {

  @Ignore
  @Test
  public void testListAlarmEvents() {
    final TargetHost targetHost = TargetHost.builder("172.24.12.8.55", 8080).build();
    final FalconClient falconClient = new FalconClient(targetHost);
    Map<String, String> map = new HashMap<>();
    final FalconApiToken root = new FalconApiToken("root", "default-token-used-in-server-side");
    map.put("ApiToken", JsonConverter.writeValueAsStringQuietly(root));
    final FalconEventsQueryParam param = new FalconEventsQueryParam("s_44_333f6a5ecd75f791cc9645c476a933d0");
    final FalconEvents falconEvents = falconClient.listEvents(param, map);
    System.out.println(falconEvents.getEvents());

    final FalconEventCasesQueryParam eventCaseQueryParam = new FalconEventCasesQueryParam();
    eventCaseQueryParam.setEndTime(System.currentTimeMillis() / 1000);
    final FalconEventCases falconEventAlarms1 = falconClient.listEventCases(eventCaseQueryParam, map);
    System.out.println(falconEventAlarms1);
  }
}
