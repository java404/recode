package smartmon.falcon.remote.types.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FalconActionUpdateParam {
  private Long id;
  private String uic;
  private String url;
  private Long callback;
  @JsonProperty("before_callback_sms")
  private Integer beforeCallbackSms;
  @JsonProperty("before_callback_mail")
  private Integer beforeCallbackMail;
  @JsonProperty("after_callback_sms")
  private Integer afterCallbackSms;
  @JsonProperty("after_callback_mail")
  private Integer afterCallbackMail;
}
