#!/bin/bash

# Run this file from your mac to install the nessisary software

if [ -z $1 ]
then
    echo "Usage: $0 <roborio-hostname> <port>"
    exit -1;
fi;

ssh -p $2 admin@$1 "rm -rf ~/*"

FILES_TO_COPY="\
./install_mosquitto.roborio.sh \
./libwebsockets.so \
./libzlib.so \
./mosquitto \
./mosquitto.init.sh \
./mosquitto.roborio.conf"

scp -P $2 $FILES_TO_COPY admin@$1:~/

ssh -p $2 admin@$1 "chmod +x ~/install_mosquitto.roborio.sh;./install_mosquitto.roborio.sh"
