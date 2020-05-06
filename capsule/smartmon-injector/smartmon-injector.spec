%define __jar_repack 0
%define debug_package %{nil}
%define _binaries_in_noarch_packages_terminate_build   0

Name:             smartmon-injector
Version:          1.0.0
License:          SmartMon
Release:          1
Summary:          SmartMon Injector
Group:            Development/Tools
BuildArch:        noarch
Source0:          %{name}-%{version}.tar.gz
BuildRequires:    systemd
Requires(post):   systemd
Requires(preun):  systemd
Requires(postun): systemd

%description
SmartMon Injector

%prep
%setup -q

%build

%install
mkdir -pv $RPM_BUILD_ROOT/opt/smartmon/smartmon-injector/
mkdir -pv $RPM_BUILD_ROOT/lib/systemd/system/
cp -rv $RPM_BUILD_DIR/%{name}-%{version}/* $RPM_BUILD_ROOT/opt/smartmon/smartmon-injector/
cp -v  $RPM_BUILD_DIR/%{name}-%{version}/conf/smartmon-injector.service \
  $RPM_BUILD_ROOT/lib/systemd/system/smartmon-injector.service
exit 0

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(-,root,root,-)
%attr(0755,root,root) /opt/smartmon/smartmon-injector/smartmon-injector-startup.sh
%doc
/lib/systemd/system/smartmon-injector.service
/opt/smartmon/smartmon-injector

%changelog
