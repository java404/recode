#!/bin/bash

_base_dir=`cd "$(dirname "$0")"; pwd`
_misc_dir=${_base_dir}/../misc
. ${_misc_dir}/rpm_settings.sh

_pkg_ver=1.0.0
_release=1
_smartmon_nacos=smartmon-nacos-${_pkg_ver}-${_release}

echo "Publish SmartMon-NaCos"
curl -u admin:admin123 --upload-file \
  ${_rpm_build_top}/RPMS/noarch/${_smartmon_nacos}.noarch.rpm \
  "${_nexus_server}/repository/packages/smartmon/latest/${_smartmon_nacos}.noarch.rpm"

