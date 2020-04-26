package smartmon.webtools.config;

import com.google.common.base.Strings;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Component
@EnableSwagger2WebFlux
public class SmartMonSwaggerConfig {
  private static final String SUPPORT_NAME = "support";
  private static final String SUPPORT_SITE = "http://www.phegda.com/";
  private static final String SUPPORT_MAIL = "support@phegda.com";
  private static final Contact contact = new Contact(SUPPORT_NAME, SUPPORT_SITE, SUPPORT_MAIL);

  @Value("${smartmon.api.prefix:/api/v2}")
  private String apiPrefix;
  @Value("${smartmon.api.infoName:base}")
  private String apiInfoName;
  @Value("${smartmon.api.infoVer:1.0.0}")
  private String apiInfoVer;

  private Parameter makeContentType() {
    return new ParameterBuilder().name("Content-Type")
      .modelRef(new ModelRef("string"))
      .parameterType("header").required(true)
      .defaultValue("application/json;charset=UTF-8").build();
  }

  private List<Parameter> makeGlobalParameters() {
    return Collections.singletonList(makeContentType());
  }

  private ApiInfo makeApiInfo(String title, String version) {
    return new ApiInfoBuilder().title(title).contact(contact).version(version)
      .termsOfServiceUrl(SUPPORT_SITE).license("SmartMon")
      .licenseUrl(SUPPORT_SITE).build();
  }

  private Predicate<String> makePaths(String apiPrefix) {
    return PathSelectors.ant(String.format("%s/**", Strings.nullToEmpty(apiPrefix)));
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
