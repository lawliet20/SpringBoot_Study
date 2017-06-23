tpid=`cat .gateway.tpid | awk '{print $1}'`
tpid=`ps -aef | grep $tpid | awk '{print $2}' |grep $tpid`
if [ ${tpid} ]; then
    echo "gateway service is running, PID : ${tpid}"
else
	echo "gateway service is stoped."
fi
