package smartmon.smartstor.domain.model;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;
import smartmon.smartstor.domain.exception.ApiVersionUnsupportedException;
import smartmon.smartstor.domain.share.ValueObject;

@Getter
@NoArgsConstructor
public class ApiVersion extends ValueObject implements Comparable<ApiVersion> {
  private static final String VERSION_PREFIX = "v";
  private static final ApiVersion V1_5 = new ApiVersion("v1.5");
  public static final ApiVersion V3_0 = new ApiVersion("v3.0");
  public static final ApiVersion V3_2 = new ApiVersion("v3", 2000);
  private static final ApiVersion LOWEST_API_VERSION = V1_5;

  @Setter
  private String version;
  private Integer subVersion;
  private Integer versionValue;

  private ApiVersion(String version) {
    this(version, 0);
  }

  private ApiVersion(String version, int subVersion) {
    this.version = version;
    this.subVersion = subVersion;
    this.versionValue = getVersionValue();
  }

  public int getVersionValue() {
    if (versionValue != null) {
      return versionValue;
    }
    if (StringUtils.isEmpty(this.getVersion())) {
      return 0;
    }
    String versionStr = this.version.toLowerCase().replaceAll(VERSION_PREFIX, Strings.EMPTY);
    double subVersion = 0;
    if (this.getSubVersion() != null) {
      subVersion = this.getSubVersion().doubleValue();
    }
    return (int) (Double.parseDouble(versionStr) * Math.pow(10, 5) + subVersion);
  }

  public void setSubVersion(String subVersion) {
    this.subVersion = Integer.valueOf(subVersion);
  }

  public void checkVersion() {
    if (compareTo(LOWEST_API_VERSION) < 0) {
      throw new ApiVersionUnsupportedException(String.format("Api version:[%s] too low", this.versionValue));
    }
  }

  public boolean hostIdEnabled() {
    return compareTo(V3_2) >= 0;
  }

  @Override
  public int compareTo(@NonNull ApiVersion o) {
    return this.getVersionValue() - o.getVersionValue();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ApiVersion)) {
      return false;
    }
    return compareTo((ApiVersion) obj) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.versionValue);
  }
}
