#!/bin/bash
echo "---------------> Start Nohup metrics starts."
cd
pwd
whoami
nohup collectl -sTNcmdib -oT -P --rawnetfilt lo --tcpfilt It | rotatelogs -n 1 stat.log 100M &
# nohup collectl -sTCMD -oT -P | rotatelogs -n 1 mylog.log 100M &
# nohup collectl | rotatelogs -n 1 mylog.log 1M &
# collectl | rotatelogs -n 1 -c mylog.log 1M
echo "---------------> Start Nohup metrics finished."
