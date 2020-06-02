#!/usr/bin/env python
# coding:utf-8

import os
import re
import json
import commands


def get_network_info():
    interfaces = get_network_devices()
    return {"interfaces": interfaces}


def get_network_devices():
    virtual_network_devices = os.listdir("/sys/devices/virtual/net")
    virtual_network_devices = filter(not_lo_and_bond, virtual_network_devices)
    network_devices = os.listdir("/sys/class/net/")
    network_devices = set(network_devices).difference(set(virtual_network_devices))
    return filter(not_vf_device, list(network_devices))


def not_lo_and_bond(device):
    return not is_lo(device) and not is_bond(device)


def is_lo(device):
    return device == "lo"


def is_bond(device):
    return os.path.exists(os.path.join("/sys/class/net",  device, "bonding"))


def not_vf_device(device):
    return not is_vf_device(device)


def is_vf_device(device):
    return device == "bonding_masters" or os.path.exists(os.path.join("/sys/class/net",  device, "device/physfn"))


if __name__ == "__main__":
    print json.dumps(get_network_info())
