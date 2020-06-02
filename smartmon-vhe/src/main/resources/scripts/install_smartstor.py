#!/usr/bin/env python
# coding:utf-8

import os
import sys
import json

CURRENT_PATH = os.path.dirname(os.path.realpath(__file__))
PARAMETER_TEMPLATE_FILEPATH = os.path.join(CURRENT_PATH, "args.json")
PARAMETER_PRECHECK_FILEPATH = os.path.join(CURRENT_PATH, "out.json")


def install_smartsotor():
    try:
        smartstor_installer_path, parameter_template = parse_params()
        save_template(parameter_template)
        if is_precheck():
            return execute_precheck(smartstor_installer_path)
        else:
            return execute_install(smartstor_installer_path)
    except Exception, err:
        return False, str(err), None


def parse_params():
    if len(sys.argv) <= 1 or not sys.argv[1]:
        raise Exception("missing smartstor installer path")
    smartstor_installer_path = sys.argv[1]
    parameter_key = "smartstor_parameter_template"
    parameter_value = os.environ.get(parameter_key)
    if not parameter_value:
        raise Exception("missing parameter template")
    parameter_template_value = json.loads(parameter_value)
    print_info("[INFO]parse parameters success")
    return smartstor_installer_path, parameter_template_value


def save_template(parameter_template):
    with open(PARAMETER_TEMPLATE_FILEPATH, "w") as f:
        json.dump(parameter_template, f)


def is_precheck():
    return len(sys.argv) > 2 and sys.argv[2].lower() == "precheck"


def execute_precheck(smartstor_installer_path):
    print_info("[INFO]smartstor precheck start")
    install_cmd = "%s --pre-check --args-file=%s --json-file=%s" % (
        smartstor_installer_path, PARAMETER_TEMPLATE_FILEPATH, PARAMETER_PRECHECK_FILEPATH)
    status = os.system(install_cmd)
    print_info("[INFO]smartstor precheck complete")
    success = (status >> 8) == 0
    message = "smartstor precheck success" if success else "smartstor precheck failed"
    if success:
        return success, message, load_precheck_result()
    else:
        return success, message, None


def load_precheck_result():
    with open(PARAMETER_PRECHECK_FILEPATH, "r") as f:
        return json.load(f)


def execute_install(smartstor_installer_path):
    stop_agent()
    print_info("[INFO]smartstor install start")
    install_cmd = "%s --args-file=%s" % (
        smartstor_installer_path, PARAMETER_TEMPLATE_FILEPATH)
    status = os.system(install_cmd)
    print_info("[INFO]smartstor install complete")
    success = (status >> 8) == 0
    message = "install smartstor success" if success else "install smartstor failed"
    restart_agent()
    return success, message, None

def stop_agent():
    print_info("[INFO]stop agent")
    cmd = "supervisorctl stop smtmon-agent smtmon-agent-pb smtmon-dbapi smtmon-jdbc > /dev/null 2>&1"
    os.system(cmd)

def restart_agent():
    print_info("[INFO]restart agent")
    cmd = "supervisorctl restart smtmon-agent smtmon-agent-pb smtmon-dbapi smtmon-jdbc > /dev/null 2>&1"
    os.system(cmd)

def print_info(info):
    print info
    sys.stdout.flush()


if __name__ == "__main__":
    ret = {"result": True, "message": "done", "metadata": {}}
    ret["result"], ret["message"], data = install_smartsotor()
    if data:
        ret["metadata"] = {"precheck_result": data}
    ret_json = json.dumps(ret)
    print_info('Smartmon-result: ' + ret_json)
