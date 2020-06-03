#!/bin/bash

OPENSM_CONF=/etc/opensm/opensm.conf
START_OPENSM_SHELL=/etc/opensm/start_opensm.sh
STOP_OPENSM_SHELL=/etc/opensm/stop_opensm.sh
LOCAL_OPENSM_SERVICE=/usr/lib/systemd/system/localopensm.service

init_opensm_conf()
{
    opensm -c ${OPENSM_CONF}
}

config_opensm_conf()
{
    conf_code=${1}
    port_guid=${2}
    cp  ${OPENSM_CONF} ${OPENSM_CONF}.${conf_code}
    sed -i "s/^guid\s\w*/guid ${port_guid}/" ${OPENSM_CONF}.${conf_code}
    sed -i "s/^sminfo_polling_timeout\s\w*/sminfo_polling_timeout 1000/" ${OPENSM_CONF}.${conf_code}
    sed -i "s/^polling_retry_number\s\w*/polling_retry_number 3/" ${OPENSM_CONF}.${conf_code}
}

init_start_opensm_shell()
{
    echo "#!/bin/sh" > ${START_OPENSM_SHELL}
}

config_start_opensm_shell()
{
    conf_code=${1}
    ib_name=${2}
    echo "/usr/bin/mkdir  -p /var/cache/opensm-${ib_name}
export OSM_CACHE_DIR=/var/cache/opensm-${ib_name}
/usr/sbin/opensm  -F /etc/opensm/opensm.conf.${conf_code} &" >> ${START_OPENSM_SHELL}
}

config_stop_opensm_shell()
{
    echo '#!/bin/sh 
/usr/bin/kill -9 `ps -ef | grep "opensm -F" | grep -v grep | awk '"'"{print '$2'}"'"'`' > ${STOP_OPENSM_SHELL}
}

config_localopensm_service()
{
    echo "[Unit]
Description=local_opensm_services
After=network.target  

[Service]  
Type=forking  
ExecStart=/etc/opensm/start_opensm.sh
ExecReload=  
ExecStop=/etc/opensm/stop_opensm.sh
PrivateTmp=true  

[Install]  
WantedBy=multi-user.target" > ${LOCAL_OPENSM_SERVICE}
}

chmod_shell_script()
{
    chmod +x ${START_OPENSM_SHELL}
    chmod +x ${STOP_OPENSM_SHELL}
}

load_service()
{
    systemctl daemon-reload
    systemctl stop localopensm
    systemctl start localopensm
    systemctl enable localopensm
}

config_opensm()
{
    if [ $# -lt 2 ]
    then
        echo "[ERROR]parameter number is not enough, ib and guid can not be empty"
        return 1
    fi

    # convert param to array
    ib_names=(${1//,/ })
    port_guids=(${2//,/ })
    if [ ${#ib_names[@]} -ne ${#port_guids[@]} ]
    then
        echo "[ERROR]ib name number and port guid number must be equal"
    fi

    # config opensm
    param_count=${#ib_names[@]}
    echo "[INFO]init opensm conf"
    init_opensm_conf
    if [ $? -ne 0 ]
    then
        echo "[ERROR]init opensm failed"
        return 1
    fi
    echo "[INFO]init start opensm shell"
    init_start_opensm_shell
    for i in $(seq 1 ${param_count})
    do
        j=`expr ${i} - 1`
        ib_name=${ib_names[$j]}
        port_guid=${port_guids[$j]}
        config_code=${ib_name//ib/}
        echo "[INFO]ib:${ib_name} guid:${port_guid}"
        echo "[INFO]config opensm conf"
        config_opensm_conf ${config_code} ${port_guid}
        echo "[INFO]config start opensm shell"
        config_start_opensm_shell ${config_code} ${ib_name}
    done
    echo "[INFO]config stop opensm shell"
    config_stop_opensm_shell
    echo "[INFO]config localopensm service"
    config_localopensm_service
    echo "[INFO]chmod shell script"
    chmod_shell_script
    echo "[INFO]load service"
    load_service
    echo "[INFO]conifg opensm success"
}

config_opensm ${1} ${2}