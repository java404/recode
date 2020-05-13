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
echo "[INFO]Install injector..."
yum install smartmon-injector --disablerepo=* --enablerepo=SMARTMON-REPO -y
if [ $? -ne 0 ]; then
  echo "[ERROR]Install injector failed"
  exit 1
fi
echo "[INFO]Install injector success"
echo "[INFO]Load injector service..."
systemctl daemon-reload > /dev/null
systemctl restart smartmon-injector
echo "[INFO]Load injector service success"
