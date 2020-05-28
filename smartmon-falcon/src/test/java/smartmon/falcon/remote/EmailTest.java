package smartmon.falcon.remote;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailTest {

  //@Ignore
  @Test
  public void sendQqEmail() {
    Properties properties = new Properties();
    properties.put("mail.transport.protocol", "smtp");// 连接协议
    properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
    properties.put("mail.smtp.port", 465);// 端口号
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
    properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
    // 得到回话对象
    Session session = Session.getInstance(properties);
    // 获取邮件对象
    Message message = new MimeMessage(session);
    // 设置发件人邮箱地址
    try {
      message.setFrom(new InternetAddress("1416412681@qq.com"));
      // 设置收件人邮箱地址
      //如果使用setRecipient只能设置一个收件人，InternetAddress为参数
      //如果使用setRecipients可以设置多个收件人，InternetAddress为参数
      message.setRecipients(Message.RecipientType.TO,
        new InternetAddress[]{new InternetAddress("1416412681@qq.com")});
      // 设置邮件标题
      message.setSubject(String.format("SmartMon-X 告警恢复通知 - [级别:严重] [2020-05-28 17:23:00] 内存使用率过高"));
      // 设置邮件内容
      message.setText("告警通知 - 主机告警: host132\n" +
        "\n" +
        "集群名称: cluster-3e3486e8\n" +
        "描述: 内存使用率过高\n" +
        "监控项: mem.memused.percent\n" +
        "告警时间: 2020-05-28 17:24:00\n" +
        "告警级别: 严重");
      // 得到邮差对象
      Transport transport = session.getTransport();
      // 连接自己的邮箱账户
      transport.connect("1416412681@qq.com", "skiukqtinujzieag");// password为stmp授权码
      // 发送邮件
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @Ignore
  @Test
  public void sendPhegdataEmail() {
    /**
     * 1.配置发件人邮箱信息以及创建一个Java 配置类存放SMTP服务器地址等参数。
     */
    // 发件人邮箱
    String sendEmailAccount = "qingwen_wang@phegda.com";
    // 发件人密码
    String sendEmailPassword = "skiukqtinujzieag";
    // 发件人邮箱的 SMTP 服务器地址, 此处为Outlook邮箱的SMTP服务器
    String sendEmailSMTPHost = "smtp.phegda.com";
    // 收件人邮箱
    String receiveMailAccount = "qingwen_wang@phegda.com";
    // 使用Java配置类进行配置
    Properties props = new Properties();
    // 使用的协议（JavaMail规范要求）
    props.setProperty("mail.transport.protocol", "smtp");
    // 发件人的邮箱的 SMTP 服务器地址
    props.setProperty("mail.smtp.host", sendEmailSMTPHost);
    // 需要请求认证
    //props.setProperty("mail.smtp.auth", "true");
    // 默认端口号设置为587，也可以设置为465，具体取决于SMTP服务器要求的端口号
    final String smtpPort = "465";
    props.setProperty("mail.smtp.port",smtpPort );
    props.setProperty("mail.smtp.socketFactory.fallback", "false");
    props.setProperty("mail.smtp.starttls.enable", "true");
    props.setProperty("mail.smtp.socketFactory.port", smtpPort );

    /**
     * 2.创建一个同邮件服务器交互的session
     */
    Session session = Session.getDefaultInstance(props);
    session.setDebug(true);
    // 1. 创建一封邮件
    try {
      MimeMessage message = new MimeMessage(session);
      // 2. From: 发件人
      message.setFrom(new InternetAddress(sendEmailAccount, "ExampleFrom", "UTF-8"));
      // 3. To: 收件人
      message.setRecipient(MimeMessage.RecipientType.TO,
        new InternetAddress(receiveMailAccount, "ExampleUser", "UTF-8"));
      // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
      message.setSubject("test", "UTF-8");
      // 5. Content: 邮件正文
      message.setContent("<h3>This is a test email.</h3>", "text/html;charset=UTF-8");
      // 6. 设置邮件发件时间
      message.setSentDate(new Date());
      // 7. 保存设置
      message.saveChanges();

      /**
       * 3.创建一封格式化的邮件
       */
      // 1. 根据 Session 获取邮件传输对象
      Transport transport = session.getTransport();
      // 2. 使用 邮箱账号 和 密码 连接邮件服务器
      //transport.connect(sendEmailAccount, sendEmailPassword);
      // 3. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人,
      transport.sendMessage(message, message.getAllRecipients());
      // 4. 关闭连接
      transport.close();
    } catch (Exception e) {

    }
  }
}
