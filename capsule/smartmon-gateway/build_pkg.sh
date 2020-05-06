#!/bin/bash

_base_dir=`cd "$(dirname "$0")"; pwd`
_misc_dir=${_base_dir}/../misc
_repo_root=${_base_dir}/../../
_build_root=${_base_dir}/../../build/

. ${_misc_dir}/rpm_settings.sh

_pkg_ver=1.0.0
_tmpdir=`mktemp -d /tmp/smartmon.gateway.XXXXX`
_pkg_dir=${_tmpdir}/smartmon-gateway-${_pkg_ver}

mkdir -pv ${_pkg_dir}/conf
cp -v ${_build_root}/smartmon-gateway/libs/smartmon-gateway-1.0.jar ${_pkg_dir}/
cp -v ${_base_dir}/smartmon-gateway-startup.sh ${_pkg_dir}/
cp -v ${_base_dir}/smartmon-gateway.service ${_pkg_dir}/conf/

# creating the output tar.gz
rm -rvf ${_output_dir}/smartmon-gateway-${_pkg_ver}.tar.gz
cd ${_tmpdir} && tar czvf ${_output_dir}/smartmon-gateway-${_pkg_ver}.tar.gz smartmon-gateway-${_pkg_ver}

rm -rvf ${_tmpdir}

