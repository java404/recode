#!/bin/bash

_base_dir=`cd "$(dirname "$0")"; pwd`
_misc_dir=${_base_dir}/../misc
_repo_root=${_base_dir}/../../
_build_root=${_base_dir}/../../build/
_database_resource_root=${_base_dir}/../../smartmon-database/src/main/resources

. ${_misc_dir}/rpm_settings.sh

_pkg_ver=1.0.0
_tmpdir=`mktemp -d /tmp/smartmon.database.XXXXX`
_pkg_dir=${_tmpdir}/smartmon-database-${_pkg_ver}

mkdir -pv ${_pkg_dir}/conf
cp -v ${_build_root}/smartmon-database/libs/smartmon-database-1.0.jar ${_pkg_dir}/
cp -v ${_base_dir}/smartmon-database-startup.sh ${_pkg_dir}/
cp -v ${_base_dir}/smartmon-database.service ${_pkg_dir}/conf/
cp -v ${_base_dir}/smartmon-database-init.* ${_pkg_dir}/conf/
cp -v ${_base_dir}/smartmon-database-clean.* ${_pkg_dir}/conf/

mkdir -pv ${_pkg_dir}/scripts
cp -rv ${_database_resource_root}/scripts/* ${_pkg_dir}/scripts

# creating the output tar.gz
rm -rvf ${_output_dir}/smartmon-database-${_pkg_ver}.tar.gz
cd ${_tmpdir} && tar czvf ${_output_dir}/smartmon-database-${_pkg_ver}.tar.gz smartmon-database-${_pkg_ver}

rm -rvf ${_tmpdir}

