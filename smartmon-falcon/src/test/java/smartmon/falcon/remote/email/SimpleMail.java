package smartmon.falcon.remote.email;

import lombok.Data;

@Data
public class SimpleMail {
  /** 邮件主题 */
  public String Subject;

  /** 邮件内容 */
  public String Content;
}
