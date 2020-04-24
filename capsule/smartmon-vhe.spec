%define __jar_repack 0
%define debug_package %{nil}
%define _binaries_in_noarch_packages_terminate_build   0

Name:             smartmon-vhe
Version:          1.0.0
License:          SmartMon
Release:          1
Summary:          SmartMon VHE
Group:            Development/Tools
BuildArch:        noarch
Source0:          %{name}-%{version}.tar.gz
BuildRequires:    systemd
Requires(post):   systemd
Requires(preun):  systemd
Requires(postun): systemd

%description
SmartMon VHE Server

%prep
%setup -q

%build

%install
mkdir -pv $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
mkdir -pv $RPM_BUILD_ROOT/lib/systemd/system/

cp -v $RPM_BUILD_DIR/%{name}-%{version}/smartmon-gateway-1.0.jar $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-gateway-startup.sh $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v  $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-gateway.service $RPM_BUILD_ROOT/lib/systemd/system/smartmon-gateway.service

cp -v $RPM_BUILD_DIR/%{name}-%{version}/smartmon-core-1.0.jar $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-core-startup.sh $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v  $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-core.service $RPM_BUILD_ROOT/lib/systemd/system/smartmon-core.service

cp -v $RPM_BUILD_DIR/%{name}-%{version}/smartmon-smartstor-1.0.jar $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-smartstor-startup.sh $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v  $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-smartstor.service $RPM_BUILD_ROOT/lib/systemd/system/smartmon-smartstor.service

cp -v $RPM_BUILD_DIR/%{name}-%{version}/smartmon-vhe-1.0.jar $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-vhe-startup.sh $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v  $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-vhe.service $RPM_BUILD_ROOT/lib/systemd/system/smartmon-vhe.service

cp -rv $RPM_BUILD_DIR/%{name}-%{version}/nacos $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/
cp -v  $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-nacos-startup.sh $RPM_BUILD_ROOT/opt/smartmon/smartmon-vhe/nacos/bin/
cp -v  $RPM_BUILD_DIR/%{name}-%{version}/assets/smartmon-nacos.service $RPM_BUILD_ROOT/lib/systemd/system/smartmon-nacos.service

exit 0

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(-,root,root,-)
%attr(0755,root,root) /opt/smartmon/smartmon-vhe/nacos/bin/smartmon-nacos-startup.sh
%attr(0755,root,root) /opt/smartmon/smartmon-vhe/smartmon-gateway-startup.sh
%attr(0755,root,root) /opt/smartmon/smartmon-vhe/smartmon-smartstor-startup.sh
%attr(0755,root,root) /opt/smartmon/smartmon-vhe/smartmon-vhe-startup.sh
%attr(0755,root,root) /opt/smartmon/smartmon-vhe/smartmon-core-startup.sh
%doc
/lib/systemd/system/smartmon-vhe.service
/lib/systemd/system/smartmon-smartstor.service
/lib/systemd/system/smartmon-core.service
/lib/systemd/system/smartmon-gateway.service
/lib/systemd/system/smartmon-nacos.service
/opt/smartmon/smartmon-vhe

%changelog
