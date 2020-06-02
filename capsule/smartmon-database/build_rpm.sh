#!/bin/bash

_base_dir=`cd "$(dirname "$0")"; pwd`
_misc_dir=${_base_dir}/../misc
. ${_misc_dir}/rpm_settings.sh

_pkg_ver=1.0.0
_build_spec=smartmon-database.spec

rpmdev-setuptree
rm -vf ${_rpm_build_spec_root}/${_build_spec}
cp -pv ${_base_dir}/${_build_spec} ${_rpm_build_spec_root}/
rm -rvf ${_rpm_build_top}/SOURCES/*
cp -pv ${_output_dir}/smartmon-database-${_pkg_ver}.tar.gz ${_rpm_build_src}/

rm -vf ${_rpm_build_top}/SRPMS/smartmon-database-*.rpm
rm -vf ${_rpm_build_top}/RPMS/noarch/smartmon-database-*.rpm
rm -vf ${_rpm_build_top}/RPMS/${_rpm_arc}/smartmon-database-*.rpm
rm -rvf ${_rpm_build_top}/BUILD/*

cd ${_rpm_build_top} &&  rpmbuild -bb SPECS/${_build_spec}

