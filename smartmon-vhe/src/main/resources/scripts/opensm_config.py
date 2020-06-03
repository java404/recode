#!/usr/bin/env python
# coding:utf-8

import os
import re
import json
import commands


def config_opensm(ib_params):
    try:
        params = parse_params(ib_params)
        message = None
        if params.get("operation") == "config":
            result = call_config_opensm_shell(
                params.get("ib_names"), params.get("port_guids"))
            if result != 0:
                message = "config opensm falid"
                return False, message, []
            message = "config opensm success"
        opensm_configs = get_opensm_config()
        message = message if message is not None else "get opensm config success"
        return True, message, opensm_configs
    except Exception, err:
        return False, str(err), []


def parse_params(ib_params):
    result = {}
    ib_params = json.loads(ib_params)
    operation_key = "operation"
    operation = ib_params.get(operation_key)
    operation = "config" if operation is None else operation.lower()
    result[operation_key] = operation
    if operation == "get_config":
        return result
    parameter_key = "opensm_ibs"
    if not ib_params.has_key(parameter_key):
        raise Exception("format error, not find %s key" % parameter_key)
    ibs = ib_params.get(parameter_key)
    ib_names = []
    port_guids = []
    for ib in ibs:
        if not ib.get("ib_name") or not ib.get("port_guid"):
            raise Exception("ib name and port guid must not be empty")
        ib_names.append(ib.get("ib_name"))
        port_guids.append(ib.get("port_guid"))
    result["ib_names"] = ",".join(ib_names)
    result["port_guids"] = ",".join(port_guids)
    return result


def call_config_opensm_shell(ib_names, port_guids):
    cmd = "sh ./opensm_config.sh '%s' '%s'" % (ib_names, port_guids)
    return do_cmd(cmd)


def do_cmd(cmd):
    result = os.system(cmd)
    return result >> 8


OPENSM_CONF_PATH = "/etc/opensm"


def get_opensm_config():
    result = []
    start_opensm_shell = os.path.join(OPENSM_CONF_PATH, "start_opensm.sh")
    if not os.access(start_opensm_shell, os.F_OK):
        return result
    reg = re.compile("(opensm.conf.[0-9]*)")
    with open(start_opensm_shell, "r") as f:
        for line in f:
            search_result = reg.search(line)
            if search_result:
                conf_name = search_result.group(1)
                port_guid = get_port_guid_from_opensm_conf(conf_name)
                if port_guid and port_guid not in result:
                    result.append(port_guid)
    return result


def get_port_guid_from_opensm_conf(conf_name):
    opensm_config_file = os.path.join(OPENSM_CONF_PATH, conf_name)
    if not os.access(opensm_config_file, os.F_OK):
        return None
    cmd_str = "cat %s | grep '^guid\s' | cut -d ' ' -f 2" % opensm_config_file
    status, output = commands.getstatusoutput(cmd_str)
    return output


if __name__ == "__main__":
    ret = {"result": True, "message": "done", "metadata": {}}
    parameter_key = "SMARTMON_HOST_OPTIONS"
    parameter_value = os.environ.get(parameter_key)
    if not parameter_value:
        ret["result"] = False
        ret["message"] = "missing parameter"
    else:
        result, message, data = config_opensm(parameter_value)
        ret["result"] = result
        ret["message"] = message
        ret["metadata"] = {"opensm_configs": data}

    ret_json = json.dumps(ret)
    print('Smartmon-result: ' + ret_json)
