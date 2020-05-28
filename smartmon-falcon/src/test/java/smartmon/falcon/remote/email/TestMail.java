package smartmon.falcon.remote.email;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: testMail
 * @author: ykgao
 * @description:
 * @date: 2017-10-11 下午02:13:02
 *
 */

public class TestMail {
  public static void main(String[] args) throws Exception {
    /** 创建一个邮件发送者*/
    SimpleMailSender simpleMailSender=new SimpleMailSender("1416412681@qq.com", "skiukqtinujzieag");

    /** 创建一封邮件*/
    SimpleMail mail=new SimpleMail();
    mail.setContent("This is a test email.");
    mail.setSubject("We want to invite you to our home.");

    List<String> recipients=new ArrayList<String>();
    recipients.add("1416412681@qq.com");
    simpleMailSender.send(recipients, mail);

 }
 }
