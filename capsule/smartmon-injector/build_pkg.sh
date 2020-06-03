#!/bin/bash

_base_dir=`cd "$(dirname "$0")"; pwd`
_misc_dir=${_base_dir}/../misc
_repo_root=${_base_dir}/../../
_build_root=${_base_dir}/../../build/
_injector_resource_root=${_base_dir}/../../smartmon-injector/src/main/resources

. ${_misc_dir}/rpm_settings.sh

_pkg_ver=1.0.0
_tmpdir=`mktemp -d /tmp/smartmon.injector.XXXXX`
_pkg_dir=${_tmpdir}/smartmon-injector-${_pkg_ver}

mkdir -pv ${_pkg_dir}/conf
cp -v ${_build_root}/smartmon-injector/libs/smartmon-injector-1.0.jar ${_pkg_dir}/
cp -v ${_build_root}/smartmon-oracle-cli/libs/smartmon-oracle-cli-1.0.jar ${_pkg_dir}/
cp -v ${_base_dir}/smartmon-injector-startup.sh ${_pkg_dir}/
cp -v ${_base_dir}/smartmon-injector.service ${_pkg_dir}/conf/

mkdir -pv ${_pkg_dir}/scripts
cp -rv ${_injector_resource_root}/scripts/* ${_pkg_dir}/scripts

# creating the output tar.gz
rm -rvf ${_output_dir}/smartmon-injector-${_pkg_ver}.tar.gz
cd ${_tmpdir} && tar czvf ${_output_dir}/smartmon-injector-${_pkg_ver}.tar.gz smartmon-injector-${_pkg_ver}

rm -rvf ${_tmpdir}

