initPopulation <- function(candidates,gene,min,max) {
    # each but the last column of the population presents one gene of each individual
    # the last column of the population show the fitness of each individual
    population <- matrix(nrow=candidates, ncol=(gene+1))
    for(i in 1:candidates) {
        population[i] <- runif(gene,min,max)
        population[i,gene+1] <- 0
    }
    return(population)
}

# attention: only for 2D landscapes
evaluate <- function(landscape,population) {
	population[,2] <- landscape(population[,1])
    return(population)
}

mutateOffspring <- function(population,mutateRate) {
    newPopulation <- matrix(nrow=nrow(population)*2, ncol=ncol(population))
    signs <- c(-1,1)
    # copy old population
    for(i in 1:nrow(population)) {
        for(j in 1:(ncol(population)-1)) {
            newPopulation[i,j] <- population[i,j]
        }
    }
    # get new population
    for(i in 1:nrow(population)) {
        for(j in 1:(ncol(population)-1)) {
            newPopulation[nrow(population)+i,j] <- population[i,j] + mutateRate * sample(signs,1,replace=TRUE)
        }
    }
    return(newPopulation)
}

fitnessSelection <- function(population,newSize) {
    population <- population[order(population[,ncol(population)]),]
    return(population[nrow(population):(nrow(population)-newSize),])
}

tournamentSelection <- function(population,newSize) {
	tournamentSize <- 2
	rows <- nrow(population)
	cols <- ncol(population)
	newPopulation <- matrix(nrow=newSize, ncol=cols)
	orderForTournament <- sample(1:rows,rows,replace=FALSE)
	tmpPopulation <- matrix(nrow=rows, ncol=cols)
	k <- 1
	for(i in orderForTournament) {
		tmpPopulation[k,] <- population[i,]
		k = k + 1 
	}
	k <- 1
	for(i in seq(1, rows, tournamentSize)) {
		winner <- tmpPopulation[i,]
		for(j in 1:(tournamentSize-1)) {
			if(tmpPopulation[i+j,cols] > winner[cols]) {
				winner <- tmpPopulation[i+j,]
			}
		}
		newPopulation[k,] <- winner
		k = k + 1 		
	}
	return(newPopulation)
}

sh <- function(d, alpha, sigma) {
	if(d <= sigma) {
		return(1-(d/sigma)^alpha)
	}
	return(0)
}

sharing <- function(population) {
	for(i in 1:nrow(population)) {
		shsum <- 0
		for(j in 1:nrow(population)) {
			d <- abs(population[i,1] - population[j,1])
			shsum <- shsum + sh(d,1,3)
		}
		if(shsum > 0.01) { # avoid dividing through zero
			population[i,2] <- population[i,2] / shsum
		}
	} 
	return (population)
}

crowding <- function(population) {
	rows <- nrow(population)
	cols <- ncol(population)
	newPopulation <- matrix(nrow=rows, ncol=cols)
	k <- 1
	for(i in 1:(rows/2)) {
		parentIndices <- sample(1:rows,2,replace=FALSE)
		p1 <- population[parentIndices[1],]
		p2 <- population[parentIndices[2],]
		o1 <- p1 + mutateRate * sample(c(-1,1),1,replace=TRUE)
		o2 <- p2 + mutateRate * sample(c(-1,1),1,replace=TRUE)
		if(abs(p1[1]-o1[1]) + abs(p2[1]-o2[1]) < abs(p1[1]-o2[1]) + abs(p2[1]-o1[1])) {
			if(o1[2] > p1[2]) {
				newPopulation[k,] <- o1
			} else {
				newPopulation[k,] <- p1
			}
			if(o2[2] > p2[2]) {
				newPopulation[k+1,] <- o2
			} else {
				newPopulation[k+1,] <- p2
			}
		} else {
			if(o2[2] > p1[2]) {
				newPopulation[k,] <- o2
			} else {
				newPopulation[k,] <- p1
			}
			if(o1[2] > p2[2]) {
				newPopulation[k+1,] <- o1
			} else {
				newPopulation[k+1,] <- p2
			}
		}
		k <- k + 2
	}
	return(newPopulation)
}

mainEA <- function(landscape, generations, candidates, gene, mutateRate, xMin, xMax, yMin, yMax, mode) {
    population <- initPopulation(candidates,gene,xMin,xMax)
    population <- evaluate(landscape,population)
    generation = 1
    while(generations > generation) {	
    	 if(mode == 0) { # tournament selection
    	 	population <- mutateOffspring(population,mutateRate)
        	population <- evaluate(landscape,population)    	
        	population <- tournamentSelection(population,candidates)
        } else if(mode == 1) { # sharing
        	population <- mutateOffspring(population,mutateRate)
        	population <- evaluate(landscape,population)  
        	population <- sharing(population)
        	population <- fitnessSelection(population,candidates)
        } else if(mode == 2) { # crowding
        	population <- evaluate(landscape,population)  
        	population <- crowding(population)
        }
        generation <- generation + 1
    }
    population <- evaluate(landscape,population)
    return(population)
}

# define functions (landscapes)
borders <- function(x,y) {
    if(x > 30 || x < 0) {
        return(-Inf)
    }
    return(y)
}

# settings
generations <- 50
sizeOfPopulation <- 10
gene <- 1
mutateRate <- 0.1
xMin <- 0 # for first initalisation
xMax <- 30 # for first initalisation
yMin <- -30 # for plot
yMax <- 30 # for plot
par(mfrow= c(3,2))
seq <- seq(xMin,xMax,by=0.1)

#### without crowding or sharing
# sin(x)
landscape <- function(x) borders(x,sin(x))
population <- mainEA(landscape, generations, sizeOfPopulation, gene, mutateRate, xMin, xMax, yMin, yMax, 0)
plot(population[,1],population[,2],xlim=c(xMin,xMax),ylim=c(yMin,yMax),main='sin(x)',xlab='',ylab='tournament')
lines(seq,landscape(seq))
# x * sin(x)
landscape <- function(x) borders(x,x*sin(x))
population <- mainEA(landscape, generations, sizeOfPopulation, gene, mutateRate, xMin, xMax, yMin, yMax, 0)
plot(population[,1],population[,2],xlim=c(xMin,xMax),ylim=c(yMin,yMax),main='x * sin(x)',xlab='',ylab='')
lines(seq,landscape(seq))

#### with sharing
# sin(x)
landscape <- function(x) borders(x,sin(x))
population <- mainEA(landscape, generations, sizeOfPopulation, gene, mutateRate, xMin, xMax, yMin, yMax, 1)
plot(population[,1],population[,2],xlim=c(xMin,xMax),ylim=c(yMin,yMax),main='',xlab='',ylab='sharing')
lines(seq,landscape(seq))
# x * sin(x)
landscape <- function(x) borders(x,x*sin(x))
population <- mainEA(landscape, generations, sizeOfPopulation, gene, mutateRate, xMin, xMax, yMin, yMax, 1)
plot(population[,1],population[,2],xlim=c(xMin,xMax),ylim=c(yMin,yMax),main='',xlab='',ylab='')
lines(seq,landscape(seq))

#### with crowding
# sin(x)
landscape <- function(x) borders(x,sin(x))
population <- mainEA(landscape, generations, sizeOfPopulation, gene, mutateRate, xMin, xMax, yMin, yMax, 2)
plot(population[,1],population[,2],xlim=c(xMin,xMax),ylim=c(yMin,yMax),main='',xlab='',ylab='crowding')
lines(seq,landscape(seq))
# x * sin(x)
landscape <- function(x) borders(x,x*sin(x))
population <- mainEA(landscape, generations, sizeOfPopulation, gene, mutateRate, xMin, xMax, yMin, yMax, 2)
plot(population[,1],population[,2],xlim=c(xMin,xMax),ylim=c(yMin,yMax),main='',xlab='',ylab='')
lines(seq,landscape(seq))
