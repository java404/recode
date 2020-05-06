#!/bin/sh

_db_host=${smartmon_db_host:-127.0.0.1}
_db_port=${smartmon_db_port:-3306}
_db_user=${smartmon_db_user:-root}
_db_password=${smartmon_db_password:-root}

echo "Dropping database (${_db_host}:${_db_port},${_db_user}) smartmon_smartstor service."

mysql -h ${_db_host} -P ${_db_port} -u${_db_user} -p${_db_password} \
  -e "DROP DATABASE IF EXISTS smartmon_smartstor; "
_sql_result=$?

if [ ${_sql_result} -ne 0 ]; then
  echo "ERROR: SmartMon SmartStor Service DB drop error."
  exit 1
fi

echo "SmartMon SmartStor Service DB is dropped."
exit 0