#!/usr/bin/env python
# coding:utf-8

import os
import json
import collections
import commands
import re

NETWORK_SCRIPTS_PATH = "/etc/sysconfig/network-scripts"
BOND_MODULE_CONF_FILE = "/etc/modprobe.d/bond.conf"
NETWORK_CONFIG_PREFIX = "ifcfg-"
NETWORK_ROUTE_CONFIG_PREFIX = "route-"


def config_networks(network_params):
    try:
        network_params = json.loads(network_params)
        result, message = check_params(network_params)
        if not result:
            return result, message
        ssh = network_params.get("ssh")
        clear_net_config_without_ssh(ssh)
        network_params = network_params.get("devices")
        for network_param in network_params:
            if len(network_param.get("devices")) > 1:
                config_bond(network_param)
            else:
                config_network(network_param)
        restart_openibd_if_need(network_params)
    except Exception, err:
        return False, str(err)
    else:
        return True, "config networks success"


def clear_net_config_without_ssh(ssh):
    '''清除管理网卡之外的配置'''
    net_config_files = get_net_config_files()
    net_config_files = ignore_ssh_config_file(net_config_files, ssh)
    remove_net_config_files(net_config_files)


def get_net_config_files():
    files = os.listdir(NETWORK_SCRIPTS_PATH)
    net_config_files = []
    for file in files:
        if not file.lower().startswith(NETWORK_CONFIG_PREFIX):
            continue
        filepath = os.path.join(NETWORK_SCRIPTS_PATH, file)
        if os.path.isfile(filepath):
            net_config_files.append(filepath)
    return net_config_files


def ignore_ssh_config_file(net_config_files, ssh):
    ssh_device = ssh.get("device")
    ssh_devices = []
    if ssh.has_key("slaves") and ssh.get("slaves"):
        ssh_devices = ssh.get("slaves")
    ssh_devices.append(ssh_device)
    ssh_devices.append("lo")
    return filter(lambda file: not_match_ssh_devices(file, ssh_devices), net_config_files)


def not_match_ssh_devices(file, ssh_devices):
    return get_device_name_from_device_config_file_name(file) not in ssh_devices \
        and get_device_name_from_device_config_file(file) not in ssh_devices \
        and get_master_name_from_device_config_file(file) not in ssh_devices \
        and get_bridge_name_from_device_config_file(file) not in ssh_devices


def get_device_name_from_device_config_file_name(file):
    filename = os.path.basename(file)
    return filename.replace(NETWORK_CONFIG_PREFIX, "")


def get_device_name_from_device_config_file(file):
    return get_value_from_device_config_file_by_key(file, "DEVICE")


def get_master_name_from_device_config_file(file):
    return get_value_from_device_config_file_by_key(file, "MASTER")


def get_bridge_name_from_device_config_file(file):
    return get_value_from_device_config_file_by_key(file, "BRIDGE")


def get_value_from_device_config_file_by_key(file, key):
    status, value = commands.getstatusoutput(
        "cat %s | grep '^%s' | cut -d = -f 2" % (file, key))
    result = value.replace('"', "").replace("'", "") if status == 0 else None
    return result


def remove_net_config_files(net_config_files):
    for file in net_config_files:
        print "remove file:%s" % file
        os.remove(file)


def config_network(network_param):
    dev = network_param.get("devices")[0]
    print "config network: name=%s, ip=%s" % (dev, network_param.get("ip"))
    net_type = check_and_get_net_type(dev)
    net_info = collections.OrderedDict()
    net_info["TYPE"] = get_net_type_name(net_type)
    net_info["DEVICE"] = dev
    net_info["NAME"] = dev
    net_info["ONBOOT"] = "yes"
    net_info["BOOTPROTO"] = "static"
    net_info["IPADDR"] = network_param.get("ip")
    net_info["NETMASK"] = network_param.get("netmask")
    net_info["NM_CONTROLLED"] = "no"
    filepath = get_net_config_filepath(dev)
    save_network_config(net_info, filepath)


def config_bond(network_param):
    bond_name = network_param.get("bondOptions").get("name")
    bond_mode = network_param.get("bondOptions").get("mode")
    config_bond_master(bond_name, bond_mode, network_param)
    config_bond_module(bond_name, bond_mode)
    for dev in network_param.get("devices"):
        net_type = check_and_get_net_type(dev)
        config_bond_slave(dev, bond_name, network_param)
    config_route_if_need(network_param.get("ip"), network_param.get(
        "netmask"), network_param.get("gateway"), bond_name)


def config_bond_master(bond_name, bond_mode, network_param):
    print "config bond: name=%s, mode=%s, ip=%s" % (
        bond_name, bond_mode, network_param.get("ip"))
    bond_type = check_and_get_bond_type(network_param.get("devices"))
    net_info = collections.OrderedDict()
    net_info["DEVICE"] = bond_name
    net_info["TYPE"] = "Bond"
    net_info["BONDING_MASTER"] = "yes"
    net_info["BOOTPROTO"] = "none"
    net_info["DEFROUTE"] = "no"
    if bond_type == BOND_TYPE.IB_BOND:
        net_info["IPV4_FAILURE_FATAL"] = "yes"
        net_info["IPV6INIT"] = "no"
    net_info["NAME"] = bond_name
    net_info["ONBOOT"] = "yes"
    net_info["BONDING_OPTS"] = get_bonding_opts(bond_type, bond_mode)
    net_info["IPADDR"] = network_param.get("ip")
    net_info["NETMASK"] = network_param.get("netmask")
    net_info["NM_CONTROLLED"] = "no"
    filepath = get_net_config_filepath(bond_name)
    save_network_config(net_info, filepath)


def get_bonding_opts(bond_type, bond_mode):
    bonding_opts = "\"mode=%s miimon=100\"" % bond_mode
    if bond_type == BOND_TYPE.IB_BOND:
        bonding_opts = "\"mode=%s miimon=100 downdelay=0 updelay=0 num_grat_arp=100\"" % bond_mode
    else:
        if bond_mode in [2, 4]:
            bonding_opts = "\"mode=%s miimon=100 xmit_hash_policy=1\"" % bond_mode
    return bonding_opts


def config_bond_module(bond_name, bond_mode):
    '''配置bond.conf'''
    if not os.access(BOND_MODULE_CONF_FILE, os.F_OK):
        return

    if not os.access(BOND_MODULE_CONF_FILE, os.W_OK):
        raise Exception("%s access error" % BOND_MODULE_CONF_FILE)

    print "config bond.conf: name=%s, mode=%s" % (bond_name, bond_mode)
    with open(BOND_MODULE_CONF_FILE, "a") as f:
        f.write("\nalias %s bonding" % bond_name)
        f.write("\noptions bonding miimon=100 mode=%s" % bond_mode)


def config_bond_slave(dev, bond_name, network_param):
    print "config slave: name=%s, bond=%s" % (dev, bond_name)
    net_type = check_and_get_net_type(dev)
    net_info = collections.OrderedDict()
    net_info["TYPE"] = get_net_type_name(net_type)
    net_info["NAME"] = dev
    net_info["DEVICE"] = dev
    net_info["ONBOOT"] = "yes"
    net_info["MASTER"] = bond_name
    net_info["SLAVE"] = "yes"
    if net_type == NET_TYPE.IB:
        net_info["IPV6INIT"] = "no"
    net_info["NM_CONTROLLED"] = "no"
    filepath = get_net_config_filepath(dev)
    save_network_config(net_info, filepath)


def config_route_if_need(ip, netmask, gateway, bond_name):
    if not netmask or not gateway:
        return
    route_config = get_route_config(ip, netmask, gateway)
    if route_config:
        save_route_config(bond_name, route_config)


def get_route_config(ip, netmask, gateway):
    address = get_network_address(ip, netmask)
    prefix = get_network_prefix(ip, netmask)
    if not address or not prefix:
        print "[WARN]get network address or prefix failed by netmask [%s]" % netmask
        return None
    route_config = "%s/%s via %s" % (address, prefix, gateway)
    return route_config


def get_network_prefix(ip, netmask):
    cmd = "ipcalc -p %s %s" % (ip, netmask)
    status, output = commands.getstatusoutput(cmd)
    return output.replace("PREFIX=", "") if status == 0 else ""


def get_network_address(ip, netmask):
    cmd = "ipcalc -n %s %s" % (ip, netmask)
    status, output = commands.getstatusoutput(cmd)
    return output.replace("NETWORK=", "") if status == 0 else ""


def save_route_config(bond_name, route_config):
    filepath = get_route_config_filepath(bond_name)
    print "save %s: %s" % (filepath, route_config)
    with open(filepath, "w") as f:
        f.write(route_config)


def get_net_type_name(net_type):
    return "InfiniBand" if net_type == NET_TYPE.IB else "Ethernet"


def get_net_config_filepath(dev):
    return NETWORK_SCRIPTS_PATH + "/" + NETWORK_CONFIG_PREFIX + dev


def get_route_config_filepath(dev):
    return NETWORK_SCRIPTS_PATH + "/" + NETWORK_ROUTE_CONFIG_PREFIX + dev


def save_network_config(data, filepath):
    print "save %s" % filepath
    with open(filepath, "w") as f:
        first_line = True
        for key, value in data.items():
            if not first_line:
                f.write("\n")
            f.write("%s=%s" % (key, value))
            first_line = False


def check_params(network_params):
    print "check parameters"
    if not isinstance(network_params, dict) or not network_params.has_key("devices"):
        return False, "format error, not find devices key"

    if not network_params.has_key("ssh"):
        return False, "format error, not find ssh key"

    ssh = network_params.get("ssh")
    if not isinstance(ssh, dict) or not ssh.has_key("ip") or not ssh.has_key("device"):
        return False, "ssh value must be contains ip and device"

    network_params = network_params.get("devices")
    if not isinstance(network_params, list) or len(network_params) == 0:
        return False, "devices value must be list and not empty"

    ips_to_be_configured = []
    devices_to_be_configured = []
    for network_param in network_params:
        if not isinstance(network_param, dict):
            return False, "the parameter must be a dict in the devices list"

        if not network_param.has_key("ip") or not network_param.get("ip"):
            return False, "missing ip"
        ips_to_be_configured.append(network_param.get("ip"))

        if not network_param.has_key("netmask") or not network_param.get("netmask"):
            return False, "missing netmask"

        if not network_param.has_key("devices") or not network_param.get("devices") or \
                len(network_param.get("devices")) <= 0:
            return False, "missing devices"

        if len(network_param.get("devices")) > 1:
            if not network_param.has_key("bondOptions"):
                return False, "missing bondOptions"

            bond_type = check_and_get_bond_type(network_param.get("devices"))
            bondOptions = network_param.get("bondOptions")
            if not isinstance(bondOptions, dict) or not bondOptions.has_key("mode") or not bondOptions.has_key("name"):
                return False, "bondOptions format error"

            ret, msg = check_bond_name(bondOptions.get("name"), bond_type)
            if not ret:
                return False,  msg
        else:
            check_and_get_net_type(network_param.get("devices")[0])
        devices_to_be_configured.extend(network_param.get("devices"))

        if check_ssh_ip_change(ssh, network_param):
            return False, "ssh ip and devices must no be changed"

        ip = network_param.get("ip")
        if not check_ip_format(ip):
            return False, "ip %s format error" % ip

        if check_ip_in_use(ip) and not check_ip_is_local(ip):
            return False, "ip %s is in use" % ip

    judge_ip_repeated(ips_to_be_configured)
    judge_device_repeated(devices_to_be_configured)
    return True, "check parameters success"


def check_and_get_net_type(dev):
    if not dev:
        raise Exception("device can not be empty:%s" % dev)
    net_type = None
    if dev.startswith("ib"):
        net_type = NET_TYPE.IB
    else:
        net_type = NET_TYPE.ETH
    return net_type


def check_and_get_bond_type(devices):
    if len(devices) < 2:
        raise Exception(
            "bond slaves number must not less than two:%s" % devices)
    bond_type = None
    for dev in devices:
        temp_bond_type = None
        net_type = None
        try:
            net_type = check_and_get_net_type(dev)
        except:
            raise Exception("invalid device in bond slaves:%s" % devices)
        if net_type == NET_TYPE.ETH:
            temp_bond_type = BOND_TYPE.ETH_BOND
        elif net_type == NET_TYPE.IB:
            temp_bond_type = BOND_TYPE.IB_BOND
        if bond_type and bond_type != temp_bond_type:
            raise Exception(
                "bond slaves should not contains ib and eth together:%s" % devices)
        bond_type = temp_bond_type
    if not bond_type:
        raise Exception("invalid device in bond slaves:%s" % devices)
    return bond_type


def check_ssh_ip_change(ssh, network_param):
    '''检查管理IP是否被修改'''
    net_ip = network_param.get("ip")
    net_devices = network_param.get("devices")
    ssh_ip = ssh.get("ip")
    ssh_device = ssh.get("device")
    if net_ip == ssh_ip:
        return True

    ssh_devices = []
    if ssh.has_key("slaves"):
        ssh_devices = ssh.get("slaves")
    else:
        ssh_devices.append(ssh_device)
    for dev in net_devices:
        if dev in ssh_devices:
            return True

    return False


def check_ip_format(ip):
    '''检查IP格式是否正确'''
    return check_ipv4_format(ip)


def check_ipv4_format(ip):
    '''检查IPv4格式是否正确'''
    parts = ip.split('.')
    if len(parts) != 4:
        return False

    for x in iter(parts):
        try:
            x = int(x)
            if x < 0 or x > 255:
                return False
        except ValueError, e:
            return False

    return True


def check_ip_in_use(ip):
    '''检查IP是否在使用'''
    status, output = commands.getstatusoutput("ping -c 2 -w 2 %s" % ip)
    return status == 0


def check_ip_is_local(ip):
    '''检查是否本机IP'''
    cmd = "ip a | grep '\\b%s\\b'" % str(ip)
    status, output = commands.getstatusoutput(cmd)
    return status == 0


def check_bond_name(bond_name, bond_type):
    '''检查bond name是否合法'''
    if not bond_name or len(bond_name) > 16:
        return False, "bond name[%s]:length must not be greater than 16" % bond_name
    bond_name_prefix = "bond" if bond_type == BOND_TYPE.ETH_BOND else "bondib"
    valid_reg = "^%s\d+$" % bond_name_prefix
    matchObj = re.search(valid_reg, bond_name)
    return matchObj is not None, "bond name [%s] format error, valid format [%s]" % (bond_name, bond_name_prefix + "\d+")


def judge_ip_repeated(ips_to_be_configured):
    ips_repeated = get_repeated(ips_to_be_configured)
    if ips_repeated:
        raise Exception("ip should not be repeated:%s" % ips_repeated)


def judge_device_repeated(devices_to_be_configured):
    devices_repeated = get_repeated(devices_to_be_configured)
    if devices_repeated:
        raise Exception("device should not be repeated:%s" % devices_repeated)


def get_repeated(data):
    repeated = []
    [repeated.append(item) for item in data if data.count(
        item) > 1 and item not in repeated]
    return repeated


def restart_openibd_if_need(network_params):
    if not need_restart_openibd(network_params):
        return
    print "[INFO]restart openibd"
    restart_openibd_cmd = 'systemctl restart openibd && echo "[INFO]restart openibd success" || echo "[ERROR]restart openibd failed"'
    _, output = commands.getstatusoutput(restart_openibd_cmd)
    if output:
        print output


def need_restart_openibd(network_params):
    for network_param in network_params:
        dev = network_param.get("devices")[0]
        net_type = check_and_get_net_type(dev)
        if net_type == NET_TYPE.IB:
            return True
    return False


class BOND_TYPE():
    ETH_BOND = "eth_bond"
    IB_BOND = "ib_bond"


class NET_TYPE():
    ETH = "eth"
    IB = "ib"


if __name__ == "__main__":
    ret = {"result": True, "message": "done", "metadata": {}}
    parameter_key = "SMARTMON_HOST_OPTIONS"
    parameter_value = os.environ.get(parameter_key)
    if not parameter_value:
        ret["result"] = False
        ret["message"] = "missing parameter"
    else:
        result, message = config_networks(parameter_value)
        ret["result"] = result
        ret["message"] = message

    ret_json = json.dumps(ret)
    print('Smartmon-result: ' + ret_json)
