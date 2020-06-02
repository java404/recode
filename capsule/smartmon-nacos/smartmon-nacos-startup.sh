#!/bin/sh

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

export SERVER="nacos-server"
export MODE="standalone"
export FUNCTION_MODE="all"

export BASE_DIR=`cd $(dirname $0)/..; pwd`
export DEFAULT_SEARCH_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
export CUSTOM_SEARCH_LOCATIONS=${DEFAULT_SEARCH_LOCATIONS},file:${BASE_DIR}/conf/

export MALLOC_ARENA_MAX=2
JAVA_OPT="${JAVA_OPT} -Xms512m -Xmx512m -Xmn256m"
JAVA_OPT="${JAVA_OPT} -Dnacos.standalone=true"

export SMARTMON_LOG_DIR="/var/log/smartmon"
if [ ! -d "${SMARTMON_LOG_DIR}" ]; then
  mkdir -p "${SMARTMON_LOG_DIR}"
fi

JAVA_OPT="${JAVA_OPT} -Dloader.path=${BASE_DIR}/plugins/health,${BASE_DIR}/plugins/cmdb,${BASE_DIR}/plugins/mysql"
JAVA_OPT="${JAVA_OPT} -Dnacos.home=${BASE_DIR}"
JAVA_OPT="${JAVA_OPT} -jar ${BASE_DIR}/target/${SERVER}.jar"

JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"
JAVA_OPT="${JAVA_OPT} --spring.config.location=${CUSTOM_SEARCH_LOCATIONS}"
JAVA_OPT="${JAVA_OPT} --server.max-http-header-size=524288"
JAVA_OPT="${JAVA_OPT} --smartmon.common.logger.prefix=smartmon-nacos"
JAVA_OPT="${JAVA_OPT} --logging.config=${BASE_DIR}/conf/logback-smartmon-nacos.xml"

echo "$JAVA ${JAVA_OPT}"
echo "Nacos (smartmon) is starting with standalone"

touch "${SMARTMON_LOG_DIR}/smartmon-nacos-start.log"
date 2>&1 > ${SMARTMON_LOG_DIR}/smartmon-nacos-start.log
echo "$JAVA ${JAVA_OPT}" >> ${SMARTMON_LOG_DIR}/smartmon-nacos-start.log 2>&1 &
nohup $JAVA ${JAVA_OPT} nacos.nacos

