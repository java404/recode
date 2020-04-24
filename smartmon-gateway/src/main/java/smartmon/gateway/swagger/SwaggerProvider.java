package smartmon.gateway.swagger;

import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {
  public static final String API_URI = "/v2/api-docs";
  private final RouteLocator routeLocator;
  private final GatewayProperties gatewayProperties;

  public SwaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
    this.routeLocator = routeLocator;
    this.gatewayProperties = gatewayProperties;
  }

  @Override
  public List<SwaggerResource> get() {
    final List<SwaggerResource> resources = new ArrayList<>();
    final List<String> routes = new ArrayList<>();
    routeLocator.getRoutes().filter(route -> route.getId().contains("smartmon"))
      .subscribe(route -> routes.add(route.getId()));
    gatewayProperties.getRoutes().stream()
      .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
      .forEach(routeDefinition -> routeDefinition.getPredicates()
        .stream().filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
      .forEach(predicateDefinition -> resources.add(swaggerResource(routeDefinition.getId(),
        predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", API_URI)))));
    return resources;
  }

  private SwaggerResource swaggerResource(String name, String location) {
    final SwaggerResource swaggerResource = new SwaggerResource();
    swaggerResource.setName(name);
    swaggerResource.setLocation(location);
    swaggerResource.setSwaggerVersion("2.0");
    return swaggerResource;
  }
}
