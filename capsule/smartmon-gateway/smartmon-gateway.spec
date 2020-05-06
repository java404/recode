%define __jar_repack 0
%define debug_package %{nil}
%define _binaries_in_noarch_packages_terminate_build   0

Name:             smartmon-gateway
Version:          1.0.0
License:          SmartMon
Release:          1
Summary:          SmartMon Gateway
Group:            Development/Tools
BuildArch:        noarch
Source0:          %{name}-%{version}.tar.gz
BuildRequires:    systemd
Requires(post):   systemd
Requires(preun):  systemd
Requires(postun): systemd

%description
SmartMon Gateway

%prep
%setup -q

%build

%install
mkdir -pv $RPM_BUILD_ROOT/opt/smartmon/smartmon-gateway/
mkdir -pv $RPM_BUILD_ROOT/lib/systemd/system/
cp -rv $RPM_BUILD_DIR/%{name}-%{version}/* $RPM_BUILD_ROOT/opt/smartmon/smartmon-gateway/
cp -v  $RPM_BUILD_DIR/%{name}-%{version}/conf/smartmon-gateway.service \
  $RPM_BUILD_ROOT/lib/systemd/system/smartmon-gateway.service
exit 0

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(-,root,root,-)
%attr(0755,root,root) /opt/smartmon/smartmon-gateway/smartmon-gateway-startup.sh
%doc
/lib/systemd/system/smartmon-gateway.service
/opt/smartmon/smartmon-gateway

%changelog
