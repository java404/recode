package smartmon.utilities.misc;

import com.google.common.base.Strings;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;

public abstract class SmartMonSwaggerConfig {
  private static final String SUPPORT_NAME = "support";
  private static final String SUPPORT_SITE = "http://www.phegda.com/";
  private static final String SUPPORT_MAIL = "support@phegda.com";
  private static final Contact contact = new Contact(SUPPORT_NAME, SUPPORT_SITE, SUPPORT_MAIL);

  private Parameter makeContentType() {
    return new ParameterBuilder().name("Content-Type")
      .modelRef(new ModelRef("string"))
      .parameterType("header").required(true)
      .defaultValue("application/json;charset=UTF-8").build();
  }

  protected List<Parameter> makeGlobalParameters() {
    return Collections.singletonList(makeContentType());
  }

  protected ApiInfo makeApiInfo(String title, String version) {
    return new ApiInfoBuilder().title(title).contact(contact).version(version)
      .termsOfServiceUrl(SUPPORT_SITE).license("SmartMon")
      .licenseUrl(SUPPORT_SITE).build();
  }

  protected Predicate<String> makePaths(String apiPrefix) {
    return PathSelectors.ant(String.format("%s/**", Strings.nullToEmpty(apiPrefix)));
  }
}
