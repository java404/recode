package smartmon.falcon.remote;

import org.junit.Ignore;
import org.junit.Test;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SnmpTest {

  @Ignore
  @Test
  public void test() throws IOException {
    //1.初始化ip地址和端口
    Address targetAddress = GenericAddress.parse("udp:127.0.0.1/162");

    //2.创建snmp
    TransportMapping transport = new DefaultUdpTransportMapping();
    Snmp snmp = new Snmp(transport);

    //3.添加User
    USM usm = new USM(SecurityProtocols.getInstance(),
      new OctetString(MPv3.createLocalEngineID()), 0);
    SecurityModels.getInstance().addSecurityModel(usm);

    snmp.getUSM().addUser(new OctetString("MD5DES"),
      new UsmUser(new OctetString("MD5DES"),
        AuthMD5.ID,
        new OctetString("MD5DESUserAuthPassword"),
        PrivDES.ID,
        new OctetString("MD5DESUserPrivPassword")));

    //4.创建target
    UserTarget target = new UserTarget();
    target.setAddress(targetAddress);
    target.setRetries(1);
    target.setTimeout(5000);
    target.setVersion(SnmpConstants.version3);
    target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
    target.setSecurityName(new OctetString("MD5DES"));

    //5.创建PDU
    PDU pdu = new ScopedPDU();
    pdu.add(new VariableBinding(new OID("1.3.6")));
    pdu.setType(PDU.GETNEXT);

    //6.send PDU
    //send the PDU
    VariableBinding v = new VariableBinding();
    v.setOid(new OID(new int[] { 1,3,6,1,4,1,48183,1 }));
    v.setVariable(new OctetString("Snmp Trap V3 Test sendV3Auth----------"));
    pdu.add(v);
    ResponseEvent response = snmp.send(pdu, target);
  }
}
