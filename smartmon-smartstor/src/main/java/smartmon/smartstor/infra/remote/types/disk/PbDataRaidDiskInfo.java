package smartmon.smartstor.infra.remote.types.disk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import java.math.BigDecimal;
import lombok.Data;
import smartmon.smartstor.domain.model.HddDiskHealthInfo;
import smartmon.smartstor.domain.model.SsdDiskHealthInfo;
import smartmon.smartstor.domain.model.enums.IEnum;
import smartmon.smartstor.domain.model.enums.RaidTypeEnum;
import smartmon.utilities.misc.BeanConverter;


@Data
public class PbDataRaidDiskInfo {
  @JsonProperty("raid_type")
  private Integer raidType;
  private String ctl;
  private Integer eid;
  private Integer slot;
  @JsonProperty("drive_type")
  private String driveType;
  private String protocol;
  @JsonProperty("pci_addr")
  private String pciAddr;
  private String size;
  private String state;
  @JsonProperty("dev_name")
  private String devName;
  @JsonProperty("lht")
  private Long lastHeartbeatTime;
  private String sn;
  private String vendor;
  @JsonProperty("model_num")
  private String modelNum;

  private String model;
  private String health;
  @JsonProperty("ssd_diskhealth_info")
  private PbDataSsdDiskHealthInfo ssdDiskhealthInfo;
  @JsonProperty("hdd_diskhealth_info")
  private PbDataHddDiskHealthInfo hddDiskhealthInfo;

  public String getRaidDevName() {
    return String.format("%s:%s:%s", this.ctl, this.eid, this.slot);
  }

  public Long getRaidSize() {
    try {
      if (Strings.isNullOrEmpty(this.size)) {
        return 0L;
      }
      final String[] szInfo = this.size.split(" ");
      if (szInfo.length != 2) {
        return 0L;
      }

      final BigDecimal sz = BigDecimal.valueOf(Double.parseDouble(szInfo[0].trim()));
      final String unit = szInfo[1].toLowerCase().trim();

      final BigDecimal sector = BigDecimal.valueOf(512);
      final BigDecimal kb = BigDecimal.valueOf(1000);
      final BigDecimal mb = BigDecimal.valueOf(1000 * 1000);
      final BigDecimal gb = BigDecimal.valueOf(1000 * 1000 * 1000);
      final BigDecimal tb = gb.multiply(kb);
      switch (unit) {
        case "kb":
        case "k":
          return sz.multiply(kb).divide(sector, 0, BigDecimal.ROUND_UP).longValue();
        case "mb":
        case "m":
          return sz.multiply(mb).divide(sector, 0, BigDecimal.ROUND_UP).longValue();
        case "gb":
        case "g":
          return sz.multiply(gb).divide(sector, 0, BigDecimal.ROUND_UP).longValue();
        case "tb":
        case "t":
          return sz.multiply(tb).divide(sector, 0, BigDecimal.ROUND_UP).longValue();
        default:
          return 0L;
      }
    } catch (Exception err) {
      return 0L;
    }
  }

  public RaidTypeEnum getRaidType() {
    return IEnum.getByCode(RaidTypeEnum.class, this.raidType);
  }

  public SsdDiskHealthInfo getSsdDiskhealthInfo() {
    return BeanConverter.copy(ssdDiskhealthInfo, SsdDiskHealthInfo.class);
  }

  public HddDiskHealthInfo getHddDiskhealthInfo() {
    return BeanConverter.copy(ssdDiskhealthInfo, HddDiskHealthInfo.class);
  }
}
