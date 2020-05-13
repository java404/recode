package smartmon.falcon.remote.request;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import smartmon.falcon.remote.types.FalconResponseData;
import smartmon.falcon.remote.types.alarm.FalconEventCaseDeleteParam;
import smartmon.falcon.remote.types.alarm.FalconEventCases;
import smartmon.falcon.remote.types.alarm.FalconEventCasesQueryParam;
import smartmon.falcon.remote.types.alarm.FalconEventNoteHandleParam;
import smartmon.falcon.remote.types.alarm.FalconEventNoteQueryParam;
import smartmon.falcon.remote.types.alarm.FalconEventNotes;
import smartmon.falcon.remote.types.alarm.FalconEvents;
import smartmon.falcon.remote.types.alarm.FalconEventsQueryParam;

@Headers({"Content-Type: application/json"})
public interface FalconRequestAlarmManager {
  @RequestLine("POST /alarm/eventcases")
  FalconEventCases listEventCases(@RequestBody FalconEventCasesQueryParam eventCaseQueryParam,
                                  @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("GET /alarm/events")
  FalconEvents listEvents(@QueryMap FalconEventsQueryParam eventsQueryParam,
                          @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("DELETE /alarm/eventcases/{event_id}")
  FalconResponseData deleteEventCase(@Param("event_id") String eventId,
                                     @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("DELETE /alarm/eventcases")
  FalconResponseData batchDeleteEventCase(@RequestBody FalconEventCaseDeleteParam eventCaseDeleteParam,
                                          @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("GET /alarm/event_note")
  FalconEventNotes listEventNoteById(@QueryMap FalconEventNoteQueryParam eventNoteQueryParam,
                                     @HeaderMap Map<String, String> falconApiToken);

  @RequestLine("POST /alarm/event_note")
  FalconResponseData createEventNote(@RequestBody FalconEventNoteHandleParam eventNoteHandleParam,
                                     @HeaderMap Map<String, String> falconApiToken);
}
