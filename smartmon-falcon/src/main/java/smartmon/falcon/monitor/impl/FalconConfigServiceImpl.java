package smartmon.falcon.monitor.impl;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import smartmon.falcon.mapper.FalconConfigMapper;
import smartmon.falcon.monitor.FalconConfigService;
import smartmon.falcon.monitor.types.CallBackParam;
import smartmon.falcon.monitor.types.EmailConfigInfo;
import smartmon.falcon.monitor.types.FalconConfig;
import smartmon.falcon.monitor.types.SendEmailParam;
import smartmon.falcon.monitor.types.SnmpConfigInfo;
import smartmon.falcon.remote.exception.FalconSendEmailException;
import smartmon.falcon.strategy.model.StrategyPriorityEnum;
import smartmon.falcon.template.TemplateService;
import smartmon.falcon.user.Team;
import smartmon.falcon.user.TeamService;
import smartmon.falcon.user.User;
import smartmon.webtools.encryption.EncryptionService;

@Slf4j
@Service
public class FalconConfigServiceImpl implements FalconConfigService {
  @Autowired
  private FalconConfigMapper falconConfigMapper;
  @Autowired
  private TemplateService templateService;
  @Autowired
  private TeamService teamService;
  @Autowired
  private EncryptionService encryptionService;

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
  public SendEmailParam callBackParamToSendEmailParam(CallBackParam callBackParam) {
    SendEmailParam sendEmailParam = new SendEmailParam();
    if (callBackParam != null) {
      sendEmailParam.setReceiverMails(getReceiverEmailsByTplId(callBackParam.getTplId()));
      sendEmailParam.setSubject(formatEmailSubject(callBackParam));
      sendEmailParam.setText(formatEmailText(callBackParam));
    }
    return sendEmailParam;
  }

  @Override
  public SendEmailParam testSendEmailParam(String receiver) {
    SendEmailParam sendEmailParam = new SendEmailParam();
    sendEmailParam.setReceiverMails(Collections.singletonList(receiver));
    sendEmailParam.setSubject("告警通知（测试邮件）");
    sendEmailParam.setText("告警通知（测试邮件）");
    return sendEmailParam;
  }

  /**
   * 格式化邮件主题.
   * @param callBackParam 告警回调参数
   * @return 邮件主题
   */
  public String formatEmailSubject(CallBackParam callBackParam) {
    StringBuilder subject = new StringBuilder();
    if ("PROBLEM".equals(callBackParam.getStatus())) {
      subject.append("告警通知 - ");
    } else {
      subject.append("告警恢复通知 - ");
    }
    if (StringUtils.isNotEmpty(callBackParam.getPriority())) {
      final String priority = StrategyPriorityEnum.getByIndex(Integer.parseInt(callBackParam.getPriority())).getDesc();
      subject.append(String.format("[级别: %s] ", priority));
    }
    if (StringUtils.isNotEmpty(callBackParam.getTime())) {
      subject.append(String.format("[%s] ", callBackParam.getTime()));
    }
    if (StringUtils.isNotEmpty(callBackParam.getNote())) {
      subject.append(callBackParam.getNote());
    }
    return subject.toString();
  }

  /**
   * 格式化邮件发送内容.
   * @param callBackParam 告警回调参数
   * @return 邮件发送内容
   */
  public String formatEmailText(CallBackParam callBackParam) {
    StringBuilder text = new StringBuilder();
    if ("PROBLEM".equals(callBackParam.getStatus())) {
      text.append("告警通知 - ");
    } else {
      text.append("告警恢复通知 - ");
    }
    if (StringUtils.isNotEmpty(callBackParam.getEndpoint())) {
      text.append(String.format("主机告警: %s \n\n", callBackParam.getEndpoint()));
    }
    text.append(String.format("集群名称: %s", "cluster01 \n"));
    if (StringUtils.isNotEmpty(callBackParam.getNote())) {
      text.append(String.format("描述: %s \n", callBackParam.getNote()));
    }
    if (StringUtils.isNotEmpty(callBackParam.getMetric())) {
      text.append(String.format("监控项: %s \n", callBackParam.getMetric()));
    }
    if (StringUtils.isNotEmpty(callBackParam.getTime())) {
      text.append(String.format("恢复时间: %s \n", callBackParam.getTime()));
    }
    if (StringUtils.isNotEmpty(callBackParam.getPriority())) {
      final String priority = StrategyPriorityEnum.getByIndex(Integer.parseInt(callBackParam.getPriority())).getDesc();
      text.append(String.format("告警级别: %s \n", priority));
    }
    return text.toString();
  }

  @Override
  public void sendEmail(EmailConfigInfo emailConfigInfo, SendEmailParam sendEmailParam) {
    Properties properties = readProperties("mail.properties");
    if (emailConfigInfo != null) {
      if (emailConfigInfo.getAddress() != null) {
        properties.setProperty("mail.smtp.host", emailConfigInfo.getAddress());
      }
      if (emailConfigInfo.getPort() != null) {
        properties.setProperty("mail.smtp.port", emailConfigInfo.getPort());
      }
      // 得到回话对象
      Session session = Session.getInstance(properties);
      // 获取邮件对象
      Message message = new MimeMessage(session);
      // 设置发件人邮箱地址
      try {
        message.setFrom(new InternetAddress(emailConfigInfo.getSenderEmail()));
        // 设置收件人邮箱地址
        //如果使用setRecipient只能设置一个收件人，InternetAddress为参数
        //如果使用setRecipients可以设置多个收件人，InternetAddress为参数
        final List<String> receiverMails = sendEmailParam.getReceiverMails();
        final InternetAddress[] internetAddresses = getInternetAddress(receiverMails);
        message.setRecipients(Message.RecipientType.TO, internetAddresses);
        // 设置邮件标题
        message.setSubject(sendEmailParam.getSubject());
        // 设置邮件内容
        message.setText(sendEmailParam.getText());
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        final String password = encryptionService.getEncryption().decrypt(emailConfigInfo.getPassword());
        transport.connect(emailConfigInfo.getUsername(), password);
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
      } catch (Exception err) {
        log.error("send email error: ", err);
        String errorMessage = String.format("%s send email to %s occur error", emailConfigInfo.getSenderEmail(),
          String.join(",", sendEmailParam.getReceiverMails()));
        throw new FalconSendEmailException(errorMessage);
      }
    }
  }

  @Override
  public void sendEmailNoAuth(EmailConfigInfo emailConfigInfo, SendEmailParam sendEmailParam) {
    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", emailConfigInfo.getAddress());
    properties.put("mail.smtp.port", emailConfigInfo.getPort());
    final Session session = Session.getInstance(properties);
    try {
      final Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(emailConfigInfo.getSenderEmail()));
      final List<String> receiverMails = sendEmailParam.getReceiverMails();
      final InternetAddress[] internetAddresses = getInternetAddress(receiverMails);
      message.setRecipients(Message.RecipientType.TO, internetAddresses);
      message.setSubject(sendEmailParam.getSubject());
      message.setText(sendEmailParam.getText());
      Transport.send(message);
    } catch (Exception err) {
      String errorMessage = String.format("%s send email to %s occur error", emailConfigInfo.getSenderEmail(),
        String.join(",", sendEmailParam.getReceiverMails()));
      throw new FalconSendEmailException(errorMessage);
    }
  }

  /**
   * get internet address.
   */
  public InternetAddress[] getInternetAddress(List<String> receiverMails) {
    List<InternetAddress> internetAddressList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(receiverMails)) {
      for (String receiverMail : receiverMails) {
        try {
          internetAddressList.add(new InternetAddress(receiverMail));
        } catch (AddressException err) {
          log.warn("get internet address error: ", err);
        }
      }
    }
    return internetAddressList.toArray(new InternetAddress[]{});
  }

  /**
   * 读取配置文件.
   * @param fileName 文件名
   * @return properties文件信息
   */
  public Properties readProperties(String fileName) {
    Properties props = new Properties();
    try {
      props.load(FalconConfigServiceImpl.class.getClassLoader()
        .getResourceAsStream(fileName));
    } catch (IOException err) {
      log.warn("read mail properties error: ", err);
    }
    return props;
  }

  /**
   * 根据模板id获取用户的邮件列表.
   * @param tplId 模板id
   * @return 邮件列表
   */
  public List<String> getReceiverEmailsByTplId(String tplId) {
    List<String> receiverEmails = new ArrayList<>();
    if (StringUtils.isNotEmpty(tplId)) {
      final List<Team> teams = templateService.getTeamsByTemplateId(Integer.parseInt(tplId));
      List<User> users = new ArrayList<>();
      if (CollectionUtils.isNotEmpty(teams)) {
        for (Team team : teams) {
          users.addAll(teamService.getUsersByTeamId(team.getId()));
        }
      }
      receiverEmails = users.stream().map(User::getEmail).collect(Collectors.toList());
    }
    return receiverEmails;
  }

  @Override
  public void snmpV2CTrap(SnmpConfigInfo snmpConfigInfo, CallBackParam callBackParam) {
    TransportMapping<UdpAddress> transport;
    String address = snmpConfigInfo.getSnmpTrapAddress();
    String port = snmpConfigInfo.getSnmpTrapPort();
    String community = snmpConfigInfo.getCommunity();
    try {
      //Create Transport Mapping
      transport = new DefaultUdpTransportMapping();
      transport.listen();

      //Create Target
      CommunityTarget comtarget = new CommunityTarget();
      comtarget.setCommunity(new OctetString(community));
      comtarget.setVersion(SnmpConstants.version2c);
      comtarget.setAddress(new UdpAddress(address + "/" + port));
      comtarget.setRetries(2);
      comtarget.setTimeout(5000);

      //Create PDU for V2
      PDU pdu = new PDU();
      snmpTrapSetPdu(pdu, callBackParam, snmpConfigInfo.getObjectIdentifiers());
      pdu.setType(PDU.NOTIFICATION);

      //Send the PDU
      Snmp snmp = new Snmp(transport);
      snmp.send(pdu, comtarget);
      snmp.close();
    } catch (IOException err) {
      log.error("send v2 trap error: ", err);
    }
  }

  @Override
  public void snmpV3Trap(SnmpConfigInfo snmpConfigInfo, CallBackParam callBackParam) {

  }

  /**
   * snmp trap set pdu param.
   */
  public void snmpTrapSetPdu(PDU pdu,CallBackParam callBackParam, String trapOid) {
    pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));
    pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
    final List<OID> oidList = formatOid(trapOid);
    pdu.add(new VariableBinding(new OID(oidList.get(0)), new OctetString("smartmon-vhe")));
    pdu.add(new VariableBinding(new OID(oidList.get(1)), new OctetString(callBackParam.getEndpoint())));
    pdu.add(new VariableBinding(new OID(oidList.get(2)), new OctetString(callBackParam.getTime())));
    pdu.add(new VariableBinding(new OID(oidList.get(3)), new OctetString(callBackParam.getMetric())));
    pdu.add(new VariableBinding(new OID(oidList.get(4)), new OctetString(callBackParam.getStep())));
    pdu.add(new VariableBinding(new OID(oidList.get(5)), new OctetString(callBackParam.getPriority())));
    pdu.add(new VariableBinding(new OID(oidList.get(6)), new OctetString(callBackParam.getLeftValue())));
    pdu.add(new VariableBinding(new OID(oidList.get(7)), new OctetString(callBackParam.getNote())));
  }

  /**
   * get oid list.
   */
  public List<OID> formatOid(String trapOid) {
    List<OID> oidList = new ArrayList<>();
    oidList.add(new OID(trapOid + ".1")); // smartmon-x 软件描述
    oidList.add(new OID(trapOid + ".2")); // endpoint 告警主机名
    oidList.add(new OID(trapOid + ".3")); // timestamp 告警时间
    oidList.add(new OID(trapOid + ".4")); // metric  度量指标
    oidList.add(new OID(trapOid + ".5")); // step 告警次数
    oidList.add(new OID(trapOid + ".6")); // priority 告警级别
    oidList.add(new OID(trapOid + ".7")); // left-value  时间阈值
    oidList.add(new OID(trapOid + ".8")); // note 事件描述
    oidList.add(new OID(trapOid + ".9"));
    return oidList;
  }
}
