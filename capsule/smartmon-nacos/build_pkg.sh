#!/bin/bash

_base_dir=`cd "$(dirname "$0")"; pwd`
_misc_dir=${_base_dir}/../misc

. ${_misc_dir}/rpm_settings.sh

_pkg_ver=1.0.0
_tmpdir=`mktemp -d /tmp/smartmon.nacos.XXXXX`
_pkg_dir=${_tmpdir}/smartmon-nacos-${_pkg_ver}

# downloading the binary files
mkdir -pv ${_pkg_dir}
curl -o ${_tmpdir}/nacos-server.tar.gz \
  "${_nexus_server}/repository/packages/nacos/nacos-server-1.2.1.tar.gz"
tar zxvf ${_tmpdir}/nacos-server.tar.gz -C ${_pkg_dir} --strip-components 1
rm -rvf ${_tmpdir}/nacos-server.tar.gz

# updating the config and other settings
yes | cp -vf ${_base_dir}/application.properties ${_pkg_dir}/conf/application.properties
yes | cp -vf ${_base_dir}/logback-smartmon-nacos.xml ${_pkg_dir}/conf/logback-smartmon-nacos.xml
yes | cp -vf ${_base_dir}/smartmon-nacos.service ${_pkg_dir}/conf/smartmon-nacos.service
yes | cp -vf ${_base_dir}/smartmon-nacos-startup.sh ${_pkg_dir}/bin/smartmon-nacos-startup.sh
chmod a+x ${_pkg_dir}/bin/smartmon-nacos-startup.sh

# creating the output tar.gz
rm -rvf ${_output_dir}/smartmon-nacos-${_pkg_ver}.tar.gz
cd ${_tmpdir} && tar czvf ${_output_dir}/smartmon-nacos-${_pkg_ver}.tar.gz smartmon-nacos-${_pkg_ver}

rm -rvf ${_tmpdir}


