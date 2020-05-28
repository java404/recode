install_collector()
{
  echo "[INFO]Install collector..."
  yum install smartmon-collector --disablerepo=* --enablerepo=SMARTMON-REPO -y
  if [ $? -ne 0 ]; then
    echo "[ERROR]Install collector failed"
    exit 1
  fi
  echo "[INFO]Install collector success"
}

config_agent_service()
{
  echo "[INFO]Config agent service..."
  cd /opt/smartmon/collector && sh ./install.sh $*
  if [ $? -ne 0 ]; then
    echo "[ERROR]Config agent service failed"
    exit 1
  fi
  echo "[INFO]Config agent service success"
}

start_agent_service()
{
  echo "[INFO]Start agent service..."
  systemctl daemon-reload > /dev/null
  systemctl restart smartmon-collector
  echo "[INFO]Start agent service success"
}

install_collector
config_agent_service $*
start_agent_service