stop_services()
{
  echo "[INFO]Stop services..."
  systemctl stop smartmon-collector > /dev/null 2>&1
  systemctl stop smartmon-injector > /dev/null 2>&1
  echo "[INFO]Stop services success"
}

remove_rpms()
{
  echo "[INFO]Remove rpms..."
  yum remove smartmon-collector smartmon-injector -y
  echo "[INFO]Remove rpms success"
}

delete_dirs()
{
  echo "[INFO]Delete dirs..."
  rm -rvf /opt/smartmon/collector
  rm -rvf /opt/smartmon/smartmon-injector
  echo "[INFO]Delete dirs success"
}

stop_services
remove_rpms
delete_dirs
echo "[INFO]Uninstall agent success"