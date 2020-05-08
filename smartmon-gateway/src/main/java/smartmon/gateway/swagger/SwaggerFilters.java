package smartmon.gateway.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import smartmon.utilities.misc.JsonConverter;

@Slf4j
@Component
public class SwaggerFilters {
  @Value("${server.port:18080}")
  private String gatewayPort;

  private String gatewayHostInfo;

  @PostConstruct
  void init() throws UnknownHostException {
    gatewayHostInfo = String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(), gatewayPort);
  }

  private String convertSwaggerApiDoc(String source) {
    try {
      final JsonNode srcRoot = JsonConverter.readTree(source);
      ((ObjectNode)srcRoot).put("host", gatewayHostInfo);
      return JsonConverter.writeValueAsString(srcRoot);
    } catch (Exception err) {
      log.warn("Cannot convert Swagger API doc");
      return source;
    }
  }

  private RouteLocator swaggerRoute(RouteLocatorBuilder builder, String name) {
    return builder.routes().route(name + "-api-doc", r -> r.path("/" + name + "/v2/api-docs")
      .filters(f -> f.stripPrefix(1).modifyResponseBody(String.class, String.class, (exchange, s) -> {
        return Mono.just(convertSwaggerApiDoc(s));
      })).uri("lb://smartmon-" + name)).build();
  }

  @Bean
  public RouteLocator swaggerVhe(RouteLocatorBuilder builder) {
    return swaggerRoute(builder, "vhe");
  }

  @Bean
  public RouteLocator swaggerCore(RouteLocatorBuilder builder) {
    return swaggerRoute(builder, "core");
  }

  @Bean
  public RouteLocator swaggerSmartStor(RouteLocatorBuilder builder) {
    return swaggerRoute(builder, "smartstor");
  }

  @Bean
  public RouteLocator swaggerFalcon(RouteLocatorBuilder builder) {
    return swaggerRoute(builder, "falcon");
  }
}
