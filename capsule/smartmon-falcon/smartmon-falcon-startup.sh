#!/bin/sh

_base_dir=`cd "$(dirname "$0")"; pwd`

error_exit ()
{
  echo "ERROR: $1 !!"
  exit 1
}

[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME
if [ -z "$JAVA_HOME" ]; then
  JAVA_PATH=`dirname $(readlink -f $(which java))`
  if [ "x$JAVA_PATH" != "x" ]; then
    export JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
  fi
fi

[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME
if [ -z "$JAVA_HOME" ]; then
  error_exit "Please set the JAVA_HOME variable in your environment. We need JDK8 or JRE8"
fi
export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"

SMARTMON_LOG_DIR=/var/log/smartmon
SMARTMON_DB_DIR=/var/smartmon
mkdir -pv ${SMARTMON_DB_DIR}
mkdir -pv ${SMARTMON_LOG_DIR}

export MALLOC_ARENA_MAX=2
JAVA_OPT="${JAVA_OPT} -XX:+UseSerialGC -Xms256m -Xmx512m -Xss512k -XX:PermSize=128m -XX:MaxPermSize=128m"
JAVA_OPT="${JAVA_OPT} -Dserver.tomcat.max-threads=30"
JAVA_OPT="${JAVA_OPT} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${_base_dir}/java_heapdump.hprof"

echo "$JAVA ${JAVA_OPT}"
echo "SmartMon-Falcon is starting"
touch "${SMARTMON_LOG_DIR}/smartmon-falcon-start.log"
date 2>&1 > ${SMARTMON_LOG_DIR}/smartmon-falcon-start.log
echo "$JAVA ${JAVA_OPT}" >> ${SMARTMON_LOG_DIR}/smartmon-falcon-start.log 2>&1 &
nohup $JAVA ${JAVA_OPT} -jar ./smartmon-falcon-1.0.jar
