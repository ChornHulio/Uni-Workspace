filename <- "ea017"
dataRaw <- as.matrix(read.table(paste(filename,".csv",sep=""), sep=";"))

# convert data
data <- matrix(nrow=nrow(dataRaw),ncol=ncol(dataRaw))
for(i in 1:nrow(data)) {
	for(j in 1:ncol(data)) {
		if(j == 4) { # hamming or hann (isn't interesting now)
			data[i,j] <- 0.0 
		} else {
			data[i,j] <- as.double(dataRaw[i,j])
		}
	}
}

# plot settings
pdf(paste(filename,".pdf",sep=""))
par(mfrow= c(2,3)) # subplots

#############################
# plot accuracy points
plot(1:nrow(data),data[,11],main='accuracy of the individuals',xlab='individual',ylab='accuracy')

#############################
# plot duration points
plot(1:nrow(data),data[,10],main='duration of the individuals',xlab='individual',ylab='duration')

#############################
# plot fitness points
plot(1:nrow(data),data[,12],main='fitness of the individuals',xlab='individual',ylab='fitness')

#############################
# pareto front (with data points)
dominates <- function(durationJ,durationI,accuracyJ,accuracyI) {
	if(durationJ < durationI && accuracyJ >= accuracyI
		|| durationJ <= durationI && accuracyJ > accuracyI) {
		return(TRUE)
	}
	return(FALSE)
}

# create tmp population to avoid dynamic allocating
tmpPop <- rep(0,nrow(data))
tmpPopIndex <- 0

# process
i <- 1
while(i < nrow(data)) {
	j <- 1
	while(j < nrow(data)) {
		if(i != j && dominates(data[j,10],data[i,10],data[j,11],data[i,11])) {
			break
		}
		j <- j + 1
	}
	if(!dominates(data[j,10],data[i,10],data[j,11],data[i,11])) {
		tmpPopIndex <- tmpPopIndex + 1
		tmpPop[tmpPopIndex] <- i
	}
	i <- i + 1
}

# copy tmp population
newPop <- rep(0,tmpPopIndex)
for(i in 1:tmpPopIndex) {
	newPop[i] <- tmpPop[i]
}

# save pareto front (for plotting)
pareto <- matrix(nrow=length(newPop), ncol=2)
for(i in 1:length(newPop)) {
	pareto[i,1] <- data[newPop[i],10]
	pareto[i,2] <- data[newPop[i],11]
	print(data[newPop[i],])
}

# plot
plot(data[,10],data[,11],col="black",pch=16,main='pareto front',xlab='duration',ylab='accuracy')
points(pareto, col="green",pch=16)

#############################
# pareto front (without data points)
plot(pareto,col="black",pch=16,main='pareto front (without rest)',xlab='duration',ylab='accuracy')

# close pdf device
dev.off()
