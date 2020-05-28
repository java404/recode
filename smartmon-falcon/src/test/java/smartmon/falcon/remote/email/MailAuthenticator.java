package smartmon.falcon.remote.email;

import lombok.Data;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

@Data
public class MailAuthenticator extends Authenticator {

  /**
   * 用户名（登录邮箱）
   */
  private String username;
  /**
   * 密码
   */
  private String password;

  /**
   * 初始化邮箱和密码
   *
   * @param username 邮箱
   * @param password 密码
   */
  public MailAuthenticator(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(username, password);
  }
}

