#!/bin/bash
# Basic while loop

sshpass -p morelitiot ssh winet@150.164.10.73 "cd IoT/tp3-grupo3/ && ./requestPhoto.sh"

sshpass -p morelitiot scp -r winet@150.164.10.73:/tmp/*.jpg /home/$USER/PhotosTP/

filename="$(ls /home/$USER/PhotosTP	-Art | tail -n1)"

MY_PATH="`dirname \"$0\"`"              # relative
MY_PATH="`( cd \"$MY_PATH\" && pwd )`"  # absolutized and normalized
if [ -z "$MY_PATH" ] ; then
  # error; for some reason, the path is not accessible
  # to the script (e.g. permissions re-evaled after suid)
  exit 1  # fail
fi
#echo "$MY_PATH"
#echo "${filename}"

mv /home/$USER/PhotosTP/$filename $MY_PATH/photo.jpg