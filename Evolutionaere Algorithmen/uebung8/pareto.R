load("pareto.Rdata")

dominates <- function(j,i) {
	if(data[j,1] > data[i,1] && data[j,2] >= data[i,2]
		|| data[j,1] >= data[i,1] && data[j,2] > data[i,2]) {
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
		if(i != j && dominates(j,i)) {
			break
		}
		j <- j + 1
	}
	if(!dominates(j,i)) {
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
pareto <- matrix(nrow=length(newPop), ncol=ncol(data))
for(i in 1:length(newPop)) {
	pareto[i,] <- data[newPop[i],]
}

# plot
plot(data,col="black",pch=16)
points(pareto, col="red",pch=16)

