#!/usr/bin/python
# -*- coding: iso-8859-15 -*-

import os, sys

fileNodes = open("listNodes.txt", "r+")
fileAddr = open("addrList.txt", "w+")
nodesReq = fileNodes.read(); #Le todos os dados para separar os enderecos dos nos visiveis

#print nodesReq.split("|-", 8)

for line in nodesReq.split("|-"):
	#print line
	if line.startswith('0013'): # verifica se a linha e comeca com o endereço
		#print line[0:15]
		fileAddr.write(line[0:16] + " ")

fileNodes.close()
fileAddr.close()