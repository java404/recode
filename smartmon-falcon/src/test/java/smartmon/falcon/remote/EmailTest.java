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
    //properties.put("mail.transport.protocol", "smtp");// 连接协议
    properties.put("mail.smtp.host", "localhost");// 主机名
    properties.put("mail.smtp.port", 465);// 端口号
    properties.put("mail.smtp.auth", false);
    //properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
    properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
    // 得到回话对象
    Session session = Session.getInstance(properties);
    // 获取邮件对象
    Message message = new MimeMessage(session);
    // 设置发件人邮箱地址
    try {
      message.setFrom(new InternetAddress("qingwen_wang@phegda.com"));
      // 设置收件人邮箱地址
      //如果使用setRecipient只能设置一个收件人，InternetAddress为参数
      //如果使用setRecipients可以设置多个收件人，InternetAddress为参数
      message.setRecipients(Message.RecipientType.TO,
        new InternetAddress[]{new InternetAddress("qingwen_wang@phegda.com")});
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
      transport.connect();
      //transport.connect("qingwen_wang@phegda.com", "qingwen4128..");// password为stmp授权码
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
    // 收件人电子邮箱
    String to = "qingwen_wang@phegda.com";

    // 发件人电子邮箱
    String from = "qingwen_wang@phegda.com";

    // 指定发送邮件的主机为 localhost
    String host = "smtp.phegda.com";

    // 获取系统属性
    Properties properties = System.getProperties();

    // 设置邮件服务器
    properties.setProperty("mail.smtp.host", host);
    properties.put("mail.debug", "true");

    // 获取默认session对象
    Session session = Session.getDefaultInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("qingwen_wang@phegda.com", "qingwen4128..");
      }
    });

    try{
      // 创建默认的 MimeMessage 对象
      MimeMessage message = new MimeMessage(session);

      // Set From: 头部头字段
      message.setFrom(new InternetAddress(from));

      // Set To: 头部头字段
      message.addRecipient(Message.RecipientType.TO,
        new InternetAddress(to));

      // Set Subject: 头部头字段
      message.setSubject("This is the Subject Line!");

      // 设置消息体
      message.setText("This is actual message");

      // 发送消息
      Transport.send(message);
      System.out.println("Sent message successfully....");
    }catch (MessagingException mex) {
      mex.printStackTrace();
    }
  }
}
