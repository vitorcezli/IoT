#!/bin/bash
# Basic while loop

if [ $1 -eq 1 ]
then
	sshpass -p morelitiot ssh winet@150.164.10.73 "cd IoT/tp3-grupo3/ && ./genInterfaceData.sh 2" # Executa comandos para executar leitura e formatação dos dados
	#lidos dos sensores

	#echo $PWD
	sshpass -p morelitiot scp -r winet@150.164.10.73:/home/winet/IoT/tp3-grupo3/outDataAuto/datacsv.txt $PWD # Trasfere arquivo CSV lido para maquina local
elif [ $1 -eq 2 ]
then
	let i=0
    while [ $i -lt $2 ]; do
        sshpass -p morelitiot ssh winet@150.164.10.73 "cd IoT/tp3-grupo3/ && ./genInterfaceData.sh 1" # Executa comandos para executar leitura e formatação dos dados 
    	#lidos dos sensores
        wait 1m
        let i++
    done 
    sshpass -p morelitiot ssh winet@150.164.10.73 "cd IoT/tp3-grupo3/ && ./genInterfaceData.sh 3"

    #echo $PWD
    sshpass -p morelitiot scp -r winet@150.164.10.73:/home/winet/IoT/tp3-grupo3/outDataAuto/datacsv.txt $PWD # Trasfere arquivo CSV lido para maquina local
fi
