data = read.csv('exportedData.csv', sep=',', stringsAsFactors = FALSE)
plot(as.numeric(data[data[,2] == 1, 5]))