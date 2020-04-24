package smartmon.smartstor.infra.remote.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Data
@ToString
public class PbDataApiVersion {
  private String version;
  @JsonProperty("sub-version")
  private BigInteger subVersion;

  /** Get version value. */
  public long getVersionValue() {
    String version = this.getVersion();
    if (StringUtils.isBlank(version)) {
      return 0L;
    }
    String versionStr = version.toLowerCase().replaceAll("v", "");
    double subVersion = 0;
    if (this.getSubVersion() != null) {
      subVersion = this.getSubVersion().doubleValue();
    }
    return (long) (Math.pow(10, 5) * Double.parseDouble(versionStr) + subVersion);
  }

  public Boolean isApiVer30() {
    final long apiVer = getVersionValue();
    return apiVer <= 300000;
  }
}
