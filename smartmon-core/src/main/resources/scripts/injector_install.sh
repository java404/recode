install_jdk()
{
  which java
  if [ $? -ne 0 ]; then
    echo "[INFO]Install jdk..."
    yum install jdk --disablerepo=* --enablerepo=SMARTMON-REPO -y
    if [ $? -ne 0 ]; then
      echo "[ERROR]Install jdk failed"
      exit 1
    fi
    echo "[INFO]Install jkd success"
    $(which java) -version
  fi
}

install_injector()
{
  echo "[INFO]Install injector..."
  yum install smartmon-injector smartmon-tools --disablerepo=* --enablerepo=SMARTMON-REPO -y
  if [ $? -ne 0 ]; then
    echo "[ERROR]Install injector failed"
    exit 1
  fi
  echo "[INFO]Install injector success"
}

start_injector_service()
{
  echo "[INFO]Start injector service..."
  systemctl daemon-reload > /dev/null
  systemctl restart smartmon-injector
  sleep 30 # wait for smartmon-injector ready
  echo "[INFO]Start injector service success"
}

install_jdk
install_injector
start_injector_service