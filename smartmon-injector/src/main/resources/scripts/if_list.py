#!/usr/bin/env python

import socket
import fcntl
import struct
import array
import os
import json
import commands
from ib_list import *

def modprobe_moudle_if_needed():
    if if_modprobe_moudle_needed():
        modprobe_moudle()

def if_modprobe_moudle_needed():
    parameter_key = "SMARTMON_JOB_OPTIONS"
    parameter_value = os.environ.get(parameter_key)
    if parameter_value:
        try:
            params = json.loads(parameter_value)
            return params.get("modprobe_moudle")
        except:
            print "[WARN]parse parameter error"
    return False

def modprobe_moudle():
    result = os.system("sh ./modprobe_moudle.sh")
    if (result >> 8) == 0:
        print "[INFO]modprobe moudle success"
    else:
        print "[WARN]modprobe moudle failed"

def format_ip(addr):
    return str(ord(addr[0])) + '.' + \
           str(ord(addr[1])) + '.' + \
           str(ord(addr[2])) + '.' + \
           str(ord(addr[3]))

def list_all_devices():
    return os.listdir('/sys/class/net')

def all_interfaces_ipv4():
    max_possible = 128  # arbitrary. raise if needed.
    bytes = max_possible * 32
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    names = array.array('B', '\0' * bytes)
    outbytes = struct.unpack('iL', fcntl.ioctl(
        s.fileno(),
        0x8912,  # SIOCGIFCONF
        struct.pack('iL', bytes, names.buffer_info()[0])
    ))[0]
    namestr = names.tostring()
    lst = {}
    for i in range(0, outbytes, 40):
        name = namestr[i:i+16].split('\0', 1)[0]
        ip = namestr[i+20:i+24]
        if not lst.get(name):
            lst[name] = []
        lst[name].append(format_ip(ip))
    return lst

def list_all():
    ifs_ipv4 = all_interfaces_ipv4()
    devices = {}
    devices_list = list_all_devices()
    for device in devices_list:
        if device == 'lo' or device.lower() == "bonding_masters":
            continue
        devices[device] = {"ip": ""}
        if device in ifs_ipv4:
            ip = get_dev_ip(device, ifs_ipv4[device])
            devices[device]["ip"] = ip
            set_netmask_and_gateway(devices[device], device, ip)

        devices[device]["state"] = get_net_state(device)
        slaves = get_bond_slaves(device)
        if slaves:
            devices[device]["slaves"] = slaves

        devices[device]["bond_flag"] = 1 if device.startswith("bond") or slaves else 0
        devices[device]["type"] = "ib" if device.startswith("ib") else "eth"
        devices[device]["speed"] = get_net_speed(device)
        devices[device]["supported_ports"] = get_supported_ports(device)
        devices[device]["group"] = get_device_group(device)
        set_bridge_info(devices[device], device, devices_list)

    return devices

def get_dev_ip(device, dev_ips):
    if not dev_ips:
        return ""
    if len(dev_ips) > 1:
        ip = get_value_from_device_config_file_by_key(device, "IPADDR")
        ip = trim_net_config_value(ip)
        if ip and ip in dev_ips:
            return ip
    return dev_ips[0]

def make_host_metadata():
    check_ib_state = if_modprobe_moudle_needed()
    return {
        'hostname': socket.gethostname(),
        'devices': list_all(),
        'ibstat': get_ib_state(check_ib_state)
    }

def get_net_state(dev):
    cmd = "cat /sys/class/net/%s/operstate" % dev
    return get_cmd_output(cmd).lower()

def get_bond_slaves(bond_name):
    cmd = "cat /sys/class/net/%s/bonding/slaves" % bond_name
    result = get_cmd_output(cmd)
    return result.split(" ") if result else []

def get_net_speed(dev):
    cmd = "cat /sys/class/net/%s/speed" % dev
    return get_cmd_output(cmd)

def get_supported_ports(dev):
    cmd = "r=`ethtool %s | grep 'Supported ports'` && echo ${r} | cut -d : -f 2" % dev
    return get_cmd_output(cmd)

def get_cmd_output(cmd):
    status, output = commands.getstatusoutput(cmd)
    return output if status == 0 else ""

def get_device_group(dev):
    if not dev.startswith("ib"):
        return ""
    index_str = dev[2:]
    try:
        index = int(index_str)
        return index - 1 if index % 2 == 1 else index 
    except:
        return ""

def set_netmask_and_gateway(device_info, device_name, ip):
    prefix = get_value_from_device_config_file_by_key(device_name, "PREFIX")
    if not prefix:
        netmask = get_value_from_device_config_file_by_key(device_name, "NETMASK")
        if netmask:
            prefix = get_cmd_output("ipcalc -p %s %s" % (ip, netmask)).replace("PREFIX=", "")
    if prefix:
        device_info["prefix"] = trim_net_config_value(prefix)
    gateway = get_value_from_device_config_file_by_key(device_name, "GATEWAY")
    if gateway:
        device_info["gateway"] = trim_net_config_value(gateway)

def set_bridge_info(device_info, device_name, devices_list):
    net_type = get_value_from_device_config_file_by_key(device_name, "TYPE")
    if not net_type or trim_net_config_value(net_type) != "Bridge":
        return
    device_info["bridge_flag"] = True
    device_info["bridge_devs"] = []
    for dev in devices_list:
        bridge = get_value_from_device_config_file_by_key(dev, "BRIDGE")
        if bridge and trim_net_config_value(bridge) == device_name:
            device_info["bridge_devs"].append(dev)

def get_value_from_device_config_file_by_key(dev, key):
    file = "/etc/sysconfig/network-scripts/ifcfg-" + dev
    status, value = commands.getstatusoutput(
        "cat %s | grep '^%s='" % (file, key))
    result = value.replace(key + "=", "") if status == 0 else None
    return result


def trim_net_config_value(value):
    return value.replace("\"", "").replace("'", "") if value else value

def print_smartmon_result(metadata):
    ret = { 'result': True, 'message': 'done', 'metadata': metadata} 
    ret_json = json.dumps(ret)
    print('Smartmon-result: ' + ret_json)

if __name__ == "__main__":
    modprobe_moudle_if_needed()
    print_smartmon_result(make_host_metadata())
