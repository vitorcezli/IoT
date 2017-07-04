#!/usr/bin/python
# -*- coding: iso-8859-15 -*-

import os, sys

fdataNodes = open("requestAllNode.txt", "r+")
csvData = open("datacsv.txt", "w+")
dataNodes = fdataNodes.read(); #Le todos os dados para separar os enderecos dos nos visiveis

#print dataNodes.split(": ")
csvData.write("Endereco_No;Tipo_Dado;Data;Hora;Valor;\n")
for line in dataNodes.split(": "):
	#rint line
	#print line[len(line) - 3]
	if line.startswith('0013'): # verifica se a linha e comeca com o endereço
		#print line[0:16]
		csvData.write('\"' + line[0:16] + '\",')
	elif line.startswith('x:'): # valor tipo 14
		subLine = line.split()
		#print subLine[0:3]
		csvData.write( '\"' + (' '.join(  subLine[0:3])) + '\" \n')
	elif line.endswith('Data'): # Pega o Codigo da grandeza 
		subLine = line.split()
		#print subLine[0]
		csvData.write( '\"' + subLine[0] + '\",')
	elif line.endswith('Hora'): # Pega a Data
		#print line[0:10]
		csvData.write( '\"' + line[0:10] + '\",')
	elif line.endswith('Valor'): # pega a Data
		#print line[0:8]
		csvData.write( '\"' + line[0:8]  + '\",')
	elif (line.endswith('origem') & ~line.startswith('Requisicao')):
		subLine = line.split()
		#print subLine[0]
		csvData.write( '\"' + subLine[0] + '\" \n')
	elif (line.endswith('\n')):
		#print line # ultimo valor
		csvData.write( '\"' + line[0:len(line)-1] + '\",')	

fdataNodes.close()
csvData.close()