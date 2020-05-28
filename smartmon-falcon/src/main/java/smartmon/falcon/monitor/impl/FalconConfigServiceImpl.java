package smartmon.falcon.monitor.impl;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import smartmon.falcon.mapper.FalconConfigMapper;
import smartmon.falcon.monitor.FalconConfigService;
import smartmon.falcon.monitor.types.CallBackParam;
import smartmon.falcon.monitor.types.EmailConfigInfo;
import smartmon.falcon.monitor.types.FalconConfig;
import smartmon.falcon.user.TeamService;
import smartmon.falcon.user.User;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class FalconConfigServiceImpl implements FalconConfigService {
  @Autowired
  private FalconConfigMapper falconConfigMapper;
  @Autowired
  private TeamService teamService;

  @Override
  public List<FalconConfig> findItems(String key) {
    if (Strings.isNullOrEmpty(key)) {
      return falconConfigMapper.findAll();
    } else {
      return falconConfigMapper.findItems("%" + Strings.nullToEmpty(key) + "%");
    }
  }

  @Override
  public FalconConfig findItem(String name) {
    return falconConfigMapper.findItem(name);
  }

  @Override
  public void put(List<FalconConfig> items) {
    for (final FalconConfig item : items) {
      log.debug("Adding falcon-config: {}", Strings.nullToEmpty(item.getName()));
      if (Strings.isNullOrEmpty(item.getName())) {
        continue;
      }
      try {
        falconConfigMapper.put(item);
      } catch (Exception err) {
        log.error("Cannot put falcon-config {}", item.getName(), err);
      }
    }
  }

  @Override
  public CallBackParam handleCallBackParam(ServerHttpRequest request) {
    CallBackParam callBackParam = new CallBackParam();
    final MultiValueMap<String, String> queryParams = request.getQueryParams();
    callBackParam.setEndpoint(queryParams.getFirst("endpoint"));
    callBackParam.setMetric(queryParams.getFirst("metric"));
    callBackParam.setStep(queryParams.getFirst("step"));
    callBackParam.setPriority(queryParams.getFirst("priority"));
    callBackParam.setTime(queryParams.getFirst("time"));
    callBackParam.setNote(queryParams.getFirst("note"));
    callBackParam.setStatus(queryParams.getFirst("status"));
    callBackParam.setTplId(queryParams.getFirst("tpl_id"));
    callBackParam.setExpId(queryParams.getFirst("exp_id"));
    callBackParam.setStrategyId(queryParams.getFirst("stra_id"));
    callBackParam.setTags(queryParams.getFirst("tags"));
    callBackParam.setLeftValue(queryParams.getFirst("left_value"));
    return callBackParam;
  }

  @Override
  public void snmpTrap(){

  }

  @Override
  public void sendEmail(EmailConfigInfo email, CallBackParam callBackParam) {
    Properties properties = new Properties();
    properties.put("mail.transport.protocol", "smtp");// 连接协议
    properties.put("mail.smtp.host", email.getAddress());// 主机名
    properties.put("mail.smtp.port", email.getPort());// 端口号
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

  public List<String> getReceiverEmails(String tplId) {
    final List<User> usersByTeamId = teamService.getUsersByTeamId(Integer.parseInt(""));
    List<String> receiverEmails = new ArrayList<>();
    for (User user : usersByTeamId) {
      String email = user.getEmail();
      receiverEmails.add(email);
    }
    return null;
  }
}
