package smartmon.examples.controller;

import feign.Client;
import feign.FeignException;
import feign.RequestLine;
import feign.Response;
import feign.codec.Decoder;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmon.utilities.general.SmartMonResponse;
import smartmon.utilities.misc.TargetHost;
import smartmon.utilities.remote.RemoteApiBuilder;
import smartmon.utilities.remote.RemoteApiClientBuilder;

@Api(tags = "remote")
@RestController
@RequestMapping("${smartmon.api.prefix:/api/v2}/remote")
@Slf4j
public class RemoteApi {
  public static class TestDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws FeignException, IOException {
      if (type.equals(String.class)) {
        log.debug("string class");
      }
      return "abc";
    }
  }

  interface TestApi {
    @RequestLine("GET /")
    String test();
  }

  @GetMapping
  public SmartMonResponse<String> test() {
    final Client client = new RemoteApiClientBuilder()
      .withConnectTimeout(20).withRequestTimeout(30).build();
    final TargetHost targetHost = TargetHost.builder("172.24.8.193", 8880).build();
    final TestApi testApi = new RemoteApiBuilder(targetHost)
      .withClient(client)
      .withDecoder(new TestDecoder()).build(TestApi.class);
    testApi.test();
    return SmartMonResponse.OK;
  }
}
