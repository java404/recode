_rpm_build_top=${HOME}/rpmbuild
_rpm_build_src=${_rpm_build_top}/SOURCES
_rpm_build_spec_root=${_rpm_build_top}/SPECS
_rpm_build_rpms=${_rpm_build_top}/RPMS
_rpm_arc=`uname -p`
_tmpdir:= $(shell mktemp -d /tmp/smartmon.XXXXX)

_release=1
_version=1.0.0
_nexus_server="http://172.24.10.157:8081"

_smartmon_vhe_spec=smartmon-vhe.spec
_smartmon_vhe_version=1.0.0
_smartmon_vhe_release=1

vhe-package:
	echo "VHE Package"
	rm -Rvf $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)
	mkdir -pv $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)
	cp -v ../build/smartmon-gateway/libs/*.jar $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)/
	cp -v ../build/smartmon-core/libs/*.jar $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)/
	cp -v ../build/smartmon-smartstor/libs/*.jar $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)/
	cp -v ../build/smartmon-vhe/libs/*.jar $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)/
	curl -o $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)/nacos-server.tar.gz \
 		"${_nexus_server}/repository/packages/nacos/nacos-server-1.2.1.tar.gz"
	cp -rvf ./assets $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)/
	(cd $(_tmpdir)/smartmon-vhe-$(_smartmon_vhe_version)/ && tar zxvf nacos-server.tar.gz && rm -rvf nacos-server.tar.gz)
	(cd $(_tmpdir) && tar czvf $(_tmpdir)/smartmon-vhe.tar.gz smartmon-vhe-$(_smartmon_vhe_version))
	rm -vf ${_rpm_build_spec_root}/${_smartmon_vhe_spec}
	cp -pv ${_smartmon_vhe_spec} ${_rpm_build_spec_root}/
	rm -rvf ${_rpm_build_src}/*
	cp -pv $(_tmpdir)/smartmon-vhe.tar.gz ${_rpm_build_src}/smartmon-vhe-$(_smartmon_vhe_version).tar.gz
	rm -rvf $(_tmpdir)


vhe-rpm: vhe-package
	echo "VHE RPM"
	rm -vf ${_rpm_build_top}/SRPMS/smartmon-*.rpm
	rm -vf ${_rpm_build_top}/RPMS/noarch/smartmon-*.rpm
	rm -vf ${_rpm_build_top}/RPMS/${_rpm_arc}/smartmon-*.rpm
	rm -rvf ${_rpm_build_top}/BUILD/*
	(cd ${_rpm_build_top} &&  rpmbuild -bb SPECS/${_smartmon_vhe_spec})

vhe-rpm-publish: vhe-rpm
	echo "VHE RPM Publish"
	curl -u admin:admin123 --upload-file ${_rpm_build_top}/RPMS/noarch/smartmon-vhe-${_smartmon_vhe_version}-${_release}.noarch.rpm \
    	"${_nexus_server}/repository/packages/smartmon-vhe/latest/smartmon-vhe-${_smartmon_vhe_version}-${_release}.noarch.rpm"
