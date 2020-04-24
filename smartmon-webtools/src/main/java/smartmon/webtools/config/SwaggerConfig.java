package smartmon.webtools.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import smartmon.utilities.misc.SmartMonSwaggerConfig;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Component
@EnableSwagger2WebFlux
public class SwaggerConfig extends SmartMonSwaggerConfig {
  @Value("${smartmon.api.prefix:/api/v2}")
  private String apiPrefix;
  @Value("${smartmon.api.infoName:base}")
  private String apiInfoName;
  @Value("${smartmon.api.infoVer:1.0.0}")
  private String apiInfoVer;

  @Bean
  UiConfiguration uiConfig() {
    return UiConfigurationBuilder.builder().build();
  }

  @Bean
  Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(makeApiInfo(apiInfoName, apiInfoVer)).select()
      .apis(RequestHandlerSelectors.any())
      .paths(makePaths(apiPrefix)).build()
      .globalOperationParameters(makeGlobalParameters());
  }
}
