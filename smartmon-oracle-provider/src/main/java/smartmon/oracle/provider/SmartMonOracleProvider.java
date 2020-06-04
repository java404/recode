package smartmon.oracle.provider;

import smartmon.utilities.misc.TargetHost;

public interface SmartMonOracleProvider {
  SmartMonOracleRemote getOracleRemote(TargetHost remote);
}
