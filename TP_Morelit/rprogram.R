rm(list=ls(all=TRUE))
setwd("/Users/nicholas/Desktop/IoT/TP_Morelit")
data = read.csv('exportedData.csv', sep=',', stringsAsFactors = FALSE)
data
jpeg("rplot.jpg")
plot(as.numeric(data[data[,2] == 1, 5]))
