package smartmon.falcon.template;

import lombok.Data;

@Data
public class Action {
  private Long id;
  private String uic;
  private String url;
  private Long callback;
  private Integer beforeCallbackSms;
  private Integer beforeCallbackMail;
  private Integer afterCallbackSms;
  private Integer afterCallbackMail;
}
