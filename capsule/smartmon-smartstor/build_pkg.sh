#!/bin/bash

_base_dir=`cd "$(dirname "$0")"; pwd`
_misc_dir=${_base_dir}/../misc
_repo_root=${_base_dir}/../../
_build_root=${_base_dir}/../../build/

. ${_misc_dir}/rpm_settings.sh

_pkg_ver=1.0.0
_tmpdir=`mktemp -d /tmp/smartmon.smartstor.XXXXX`
_pkg_dir=${_tmpdir}/smartmon-smartstor-${_pkg_ver}

mkdir -pv ${_pkg_dir}/conf
cp -v ${_build_root}/smartmon-smartstor/libs/smartmon-smartstor-1.0.jar ${_pkg_dir}/
cp -v ${_base_dir}/smartmon-smartstor-startup.sh ${_pkg_dir}
cp -v ${_base_dir}/smartmon-smartstor.service ${_pkg_dir}/conf
cp -v ${_base_dir}/smartmon-smartstor-init.* ${_pkg_dir}/conf
cp -v ${_base_dir}/smartmon-smartstor-clean.* ${_pkg_dir}/conf

# creating the output tar.gz
rm -rvf ${_output_dir}/smartmon-smartstor-${_pkg_ver}.tar.gz
cd ${_tmpdir} && tar czvf ${_output_dir}/smartmon-smartstor-${_pkg_ver}.tar.gz smartmon-smartstor-${_pkg_ver}

rm -rvf ${_tmpdir}

