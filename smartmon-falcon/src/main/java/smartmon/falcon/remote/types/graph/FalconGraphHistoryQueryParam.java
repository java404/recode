package smartmon.falcon.remote.types.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import smartmon.falcon.graph.model.MergerTypeEnum;

import java.util.Set;

@Data
public class FalconGraphHistoryQueryParam {
  @JsonProperty("hostnames")
  private Set<String> hosts;
  @JsonProperty("counters")
  private Set<String> counters;
  @JsonProperty("consol_fun")
  private String mergerType;
  @JsonProperty("start_time")
  private Long startTime;
  @JsonProperty("end_time")
  private Long endTime;
  private Integer step;
  private Long range;

  public FalconGraphHistoryQueryParam handleParam() {
    if (this.range != null && this.range > 0 ) { // 取值范围优先级最高
      this.endTime = System.currentTimeMillis() / 1000;
      this.startTime = this.endTime - this.range;
    } else { // 结束时间为空或非法的情况
      if (this.endTime != null && this.endTime >0) {
        this.endTime = this.endTime / 1000;
      } else {
        this.endTime = System.currentTimeMillis() / 1000;
      }
      if (this.startTime != null && this.startTime > 0) {
        this.startTime = this.startTime / 1000;
      } else { // 开始时间为空或非法的情况，默认向前推一小时
        this.startTime = this.endTime - 3600;
      }
      this.range = this.endTime - this.startTime;
    }

    if (StringUtils.isEmpty(this.mergerType)) {
      this.mergerType = MergerTypeEnum.AVERAGE.toString();
    }

    if (this.range > 3600 * 12) {
      this.mergerType = MergerTypeEnum.MAX.toString();
    }

    if (this.step == null) {
      this.step = 30;
    }
    return this;
  }
}
