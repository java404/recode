#!/bin/sh

_base_dir=`cd "$(dirname "$0")"; pwd`

[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

error_exit ()
{
  echo "ERROR: $1 !!"
  exit 1
}

if [ -z "$JAVA_HOME" ]; then
  JAVA_PATH=`dirname $(readlink -f $(which java))`
  if [ "x$JAVA_PATH" != "x" ]; then
    export JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
  fi
  if [ -z "$JAVA_HOME" ]; then
    error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
  fi
fi

export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=`cd $(dirname $0)/..; pwd`
export DEFAULT_SEARCH_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"

export MALLOC_ARENA_MAX=2
JAVA_OPT="${JAVA_OPT} -XX:+UseSerialGC -Xms256m -Xmx512m -Xss512k -XX:PermSize=128m -XX:MaxPermSize=128m"
JAVA_OPT="${JAVA_OPT} -Dserver.tomcat.max-threads=30"
JAVA_OPT="${JAVA_OPT} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${_base_dir}/java_heapdump.hprof"

mkdir -pv /var/smartmon/
echo "$JAVA ${JAVA_OPT}"
nohup $JAVA ${JAVA_OPT} -jar ./smartmon-vhe-1.0.jar
