package smartmon.falcon.remote.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.util.Date;

public class SnmpTest {
  public static void main(String[] args) {
    sendSnmpV2Trap();
  }

  public static void sendSnmpV2Trap()
  {
    String community = "public";
    String trapOid = ".1.3.6.1.4.1.48183";
    String trapOid1 = ".1.3.6.1.4.1.48183.1";
    String trapOid2 = ".1.3.6.1.4.1.48183.2";
    String trapOid3 = ".1.3.6.1.4.1.48183.3";

    String ipAddress = "172.24.8.135";
    int port = 162;
    try
    {
      //Create Transport Mapping
      TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
      transport.listen();

      //Create Target
      CommunityTarget comtarget = new CommunityTarget();
      comtarget.setCommunity(new OctetString(community));
      comtarget.setVersion(SnmpConstants.version2c);
      comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
      comtarget.setRetries(2);
      comtarget.setTimeout(5000);

      //Create PDU for V2
      PDU pdu = new PDU();

      // need to specify the system up time
      pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));
      pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
      pdu.add(new VariableBinding(new OID(trapOid1), new OctetString("smartmon")));
      pdu.add(new VariableBinding(new OID(trapOid2), new OctetString("host133")));
      pdu.add(new VariableBinding(new OID(trapOid3), new OctetString("172.24.8.133")));
      pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(ipAddress)));

      // variable binding for Enterprise Specific objects, Severity (should be defined in MIB file)
      pdu.add(new VariableBinding(new OID(trapOid), new OctetString("Major")));
      pdu.setType(PDU.NOTIFICATION);

      //Send the PDU
      Snmp snmp = new Snmp(transport);
      System.out.println("Sending V2 Trap to " + ipAddress + " on Port " + port);
      snmp.send(pdu, comtarget);
      snmp.close();
    }
    catch (Exception e)
    {
      System.err.println("Error in Sending V2 Trap to " + ipAddress + " on Port " + port);
      System.err.println("Exception Message = " + e.getMessage());
    }
  }
}
