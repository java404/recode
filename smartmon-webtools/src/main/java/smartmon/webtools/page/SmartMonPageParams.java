package smartmon.webtools.page;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
  @ApiImplicitParam(paramType = "query", dataType = "int", required = false,
    name = "page-no", value = "page no", example = "1"),
  @ApiImplicitParam(paramType = "query", dataType = "int", required = false,
    name = "page-size", value = "page size", example = "1"),
  @ApiImplicitParam(paramType = "query", dataType = "string", required = false,
    name = "sort-direction", value = "sort direction (asc/desc)"),
  @ApiImplicitParam(paramType = "query", dataType = "string", required = false,
    name = "sort-property", value = "sort property"),
  @ApiImplicitParam(paramType = "query", dataType = "string", required = false,
    name = "filter", value = "filter"),
  @ApiImplicitParam(paramType = "query", dataType = "string", required = false,
    name = "filter-properties", value = "filter properties"),
  @ApiImplicitParam(paramType = "query", dataType = "string", required = false,
    name = "condition-property", value = "condition property"),
  @ApiImplicitParam(paramType = "query", dataType = "string", required = false,
    name = "condition", value = "condition value")
})
public @interface SmartMonPageParams {
}
