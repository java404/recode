#!/bin/bash

_base_dir=`cd "$(dirname "$0")"; pwd`
_misc_dir=${_base_dir}/../misc
. ${_misc_dir}/rpm_settings.sh

rm -rvf ${_output_dir}/smartmon-smartstor-*.tar.gz
rm -vf ${_rpm_build_spec_root}/smartmon-smartstor*.spec
rm -vf ${_rpm_build_top}/SRPMS/smartmon-smartstor-*.rpm
rm -vf ${_rpm_build_top}/RPMS/noarch/smartmon-smartstor-*.rpm
rm -vf ${_rpm_build_top}/RPMS/${_rpm_arc}/smartmon-smartstor-*.rpm
rm -rvf ${_rpm_build_top}/BUILD/*
