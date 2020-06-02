package smartmon.falcon.remote.snmp;

import java.io.IOException;
import java.util.Vector;

import org.snmp4j.*;
import org.snmp4j.event.*;
import org.snmp4j.mp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.*;

/**
 *
 * @author kim_hu
 * SNMP4j v3 trap sender example
 */
public class SnmpUtilSendTrapV3 {

  private Snmp snmp = null;
  private Address targetAddress = null;

  public void initComm() throws IOException {
    targetAddress = GenericAddress.parse("172.24.8.132/162");
    TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
    snmp = new Snmp(transport);
    snmp.listen();
  }

  /**
   * send trap
   *
   * @throws IOException
   */
  public void sendPDU() throws IOException {
    UserTarget target = new UserTarget();
    target.setAddress(targetAddress);
    target.setRetries(2);
    target.setTimeout(1500);
    // snmp version
    target.setVersion(SnmpConstants.version3);

    //target.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
    target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
    target.setSecurityName(new OctetString("myuser"));

    USM usm = new USM(SecurityProtocols.getInstance(),
      new OctetString("0x8000000001020304"), 500);
    usm.setEngineDiscoveryEnabled(true);
    SecurityModels.getInstance().addSecurityModel(usm);

    UsmUser user = new UsmUser(new OctetString("myuser"),
      AuthMD5.ID,
      new OctetString("mypassword"),
      Priv3DES.ID,
      new OctetString("mypassword1"));
    snmp.getUSM().addUser(new OctetString("myuser"),
      new OctetString("0x8000000001020304"), user);

    // create PDU
    ScopedPDU pdu = new ScopedPDU();
    pdu.setContextEngineID(new OctetString("0x8000000001020304"));
    pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.48183.3.0"),
      new OctetString("SnmpTrapv3")));
    pdu.add(new VariableBinding(new OID("1.3.6.1.4.1.48183.5.0"),
      new OctetString("JavaEE")));
    pdu.setType(PDU.NOTIFICATION);
    // send PDU to Agent and recieve Response
    ResponseEvent respEvnt = snmp.send(pdu, target);

    // analyze Response
    if (respEvnt != null && respEvnt.getResponse() != null) {
      Vector<VariableBinding> recVBs = (Vector<VariableBinding>) respEvnt.getResponse()
        .getVariableBindings();
      for (int i = 0; i < recVBs.size(); i++) {
        VariableBinding recVB = recVBs.elementAt(i);
        System.out.println(recVB.getOid() + " : " + recVB.getVariable());
      }
    }
    snmp.close();
  }

  public static void main(String[] args) {
    try {
      SnmpUtilSendTrapV3 util = new SnmpUtilSendTrapV3();
      util.initComm();
      util.sendPDU();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
