#!/bin/bash

_rpm_build_top=${HOME}/rpmbuild
_rpm_build_src=${_rpm_build_top}/SOURCES
_rpm_build_spec_root=${_rpm_build_top}/SPECS
_rpm_build_rpms=${_rpm_build_top}/RPMS
_rpm_arc=`uname -p`

_nexus_server="http://172.24.10.157:8081"

_output_dir=/tmp/smartmon.build
mkdir -pv ${_output_dir}

