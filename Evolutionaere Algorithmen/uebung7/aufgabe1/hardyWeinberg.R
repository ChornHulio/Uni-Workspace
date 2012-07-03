maxCount <- 100
deathRate <- c(0.995,0.99,0.993)

hardyW <- function(curGen, count) {
	# plot
	print(curGen)
	points(count,curGen[1],col="green",pch=16)
	points(count,curGen[2],col="blue",pch=16)
	points(count,curGen[3],col="black",pch=16)
	# calc next generation
	if(count < maxCount) {
		alleles <- sum(curGen) * 2
		p <- (curGen[1] * 2 + curGen[2]) / alleles
		q <- (curGen[2] + curGen[3] * 2) / alleles
		nextGen <- c(0,0,0)
		nextGen[1] <- round(alleles * p^2 / 2 + curGen[1] * (1-deathRate[1]))
		nextGen[2] <- round(alleles * p * q + curGen[2] * (1-deathRate[2]))
		nextGen[3] <- round(alleles * q^2 / 2 + curGen[2] * (1-deathRate[3]))
		hardyW(nextGen, count+1)
	}
}

par(mfrow=c(2,3))
plot(0,0,xlim=c(1,maxCount),ylim=c(1,1000))
hardyW(c(300,400,300), 1)
plot(0,0,xlim=c(1,maxCount),ylim=c(1,1000))
hardyW(c(200,400,400), 1)
plot(0,0,xlim=c(1,maxCount),ylim=c(1,1000))
hardyW(c(100,400,600), 1)
plot(0,0,xlim=c(1,maxCount),ylim=c(1,1000))
hardyW(c(0,500,500), 1)
plot(0,0,xlim=c(1,maxCount),ylim=c(1,1000))
hardyW(c(500,0,500), 1)
plot(0,0,xlim=c(1,maxCount),ylim=c(1,1000))
hardyW(c(333,333,334), 1)
