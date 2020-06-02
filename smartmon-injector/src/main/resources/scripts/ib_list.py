#!/usr/bin/env python
# coding:utf-8

import os
import re
import json
import commands
import sys
import time

INFINIBAND_CONFIG_PATH = "/sys/class/infiniband"
INFINIBAND_CONFIG_NODE_GUID_PATH = INFINIBAND_CONFIG_PATH + "/{name}/node_guid"
INFINIBAND_CONFIG_PORTS_PATH = INFINIBAND_CONFIG_PATH + "/{name}/ports"
INFINIBAND_CONFIG_PORT_STATE_PATH = INFINIBAND_CONFIG_PORTS_PATH + "/{port}/state"
INFINIBAND_CONFIG_PORT_LINKLAYER_PATH = INFINIBAND_CONFIG_PORTS_PATH + "/{port}/link_layer"


def get_ib_state(check_ib_state=False):
    print_info("[INFO]get ib state")
    ib_state = []
    if os.access(INFINIBAND_CONFIG_PATH, os.F_OK):
        card_names = os.listdir(INFINIBAND_CONFIG_PATH)
        if not check_ib_state:
            ib_state = handle_ib_cards(card_names)
        else:
            ib_state = handle_ib_cards_with_retry(card_names)
        ib_name_addresses = get_ib_name_addresses()
        set_ib_name(ib_state, ib_name_addresses)
    return ib_state


def handle_ib_cards_with_retry(card_names):
    ib_state = handle_ib_cards(card_names)
    max_retry_times = 10
    retry_time = 0
    sleep_seconds = 10
    while retry_time < max_retry_times and not check_ib_state(ib_state):
        print_info("[WARN]check ib state failed, retry get ib state after %d seconds" % sleep_seconds)
        retry_time = retry_time + 1
        time.sleep(sleep_seconds)
        ib_state = handle_ib_cards(card_names)
    return ib_state


def handle_ib_cards(card_names):
    ib_state = []
    for card_name in card_names:
        handle_ib_card(card_name, ib_state)
    return ib_state


def check_ib_state(ib_state):
    for ib_info in ib_state:
        if ib_info["state"] != "ACTIVE":
            return False
    return True

def handle_ib_card(card_name, ib_state):
    node_guid = get_cmd_output(
        "cat " + INFINIBAND_CONFIG_NODE_GUID_PATH.format(name=card_name))
    node_guid = format_node_guid(node_guid)
    ports = os.listdir(INFINIBAND_CONFIG_PORTS_PATH.format(name=card_name))
    for port in ports:
        link_layer_file = INFINIBAND_CONFIG_PORT_LINKLAYER_PATH.format(
            name=card_name, port=port)
        link_layer = get_cmd_output("cat " + link_layer_file).strip().lower()
        if link_layer != "infiniband":
            continue
        state_file = INFINIBAND_CONFIG_PORT_STATE_PATH.format(
            name=card_name, port=port)
        state = get_cmd_output("cat " + state_file + " | cut -d : -f 2").strip()
        guid = get_port_guid(node_guid, port, len(ports))
        ib_state.append({"name": card_name, "port": "Port " + port, "state": state, "guid": guid})


def get_cmd_output(cmd):
    status, output = commands.getstatusoutput(cmd)
    return output if status == 0 else ""


def format_node_guid(node_guid):
    if node_guid:
        node_guid = "0x" + node_guid.replace(":", "")
    return node_guid


def get_port_guid(node_guid, port, ports_count):
    if ports_count == 1:
      return node_guid
    guid = ""
    if node_guid:
        guid = hex(int(node_guid, 16) + int(port))
        guid = guid.rstrip("L").rstrip("l")
        node_guid_len = len(node_guid)
        if len(guid) < node_guid_len:
            guid = "0x" + guid[2:].zfill(node_guid_len - 2)

    return guid


def get_ib_name_addresses():
    status, output = commands.getstatusoutput(
        "ip a | grep '\sib[0-9]*:' | cut -d : -f 2 | sed 's/ //g'")
    ib_names = output.split("\n") if status == 0 else []
    ib_name_addresses = []
    for ib_name in ib_names:
        status, address = commands.getstatusoutput(
            "cat /sys/class/net/%s/address" % ib_name)
        ib_name_addresses.append(
            {"name": ib_name, "address": address if status == 0 else ""})
    return ib_name_addresses


def set_ib_name(ib_state, ib_name_addresses):
    for ib_name_address in ib_name_addresses:
        address = ib_name_address["address"]
        guid = get_guid_from_address(address)
        for ib_info in ib_state:
            if ib_info.get("guid") == guid:
                ib_info["ib_name"] = ib_name_address["name"]


def get_guid_from_address(address):
    address = address.replace(":", "")
    return "0x" + address[-16:] if len(address) >= 16 else address


def print_info(info):
    print info
    sys.stdout.flush()


if __name__ == "__main__":
    ret = {"result": True, "message": "done",
           "metadata": {"ibstat": get_ib_state()}}
    ret_json = json.dumps(ret)
    print('Smartmon-result: ' + ret_json)
