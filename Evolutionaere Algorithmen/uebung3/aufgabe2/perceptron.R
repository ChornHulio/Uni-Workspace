# settings
generations <- 100
sizeOfPopulation <- 10
gene <- 3
mutateRate <- 0.15
mutateOnlyOneGene <- FALSE
xMin <- -10 # for first initalisation and plot
xMax <-  15 # for first initalisation and plot
yMin <-  -5 # for plot
yMax <-  15 # for plot

# read data
data <- read.table("data1.txt")
x <- as.matrix(data[,1:(ncol(data)-1)])
y <- as.vector(as.matrix(data[,ncol(data)]))

# color points for ploting
p <- as.matrix(data[,1:(ncol(data)-2)])
p1_i <- 0
p1 <- matrix(ncol=2, nrow=nrow(p))
p2_i <- 0
p2 <- matrix(ncol=2, nrow=nrow(p))
for(i in 1:nrow(p)) {
	if(y[i] > 0) {
		p1_i <- p1_i + 1
		p1[p1_i,] <- p[i,]
	} else {
		p2_i <- p2_i + 1
		p2[p2_i,] <- p[i,]
	}
}

# define landscapes (fitness function)
landscape <- function(w) {
#	# sum of false classified points
#	# -> have a lot of local minimas
#	delta <- 0
#	for(i in 1:nrow(x)) {
#		if(y[i] != sign(x[i,] %*% w)) {
#			delta <- delta + 1
#		}
#	}

	# distances to the orthogonal vector of false classified point
	delta <- 0
	for(i in 1:nrow(x)) {
		if(y[i] != sign(x[i,] %*% w)) {
			delta <- delta + abs(x[i,] %*% w)
		}
	}
	return(delta)
}

# functions
initPopulation <- function(candidates,gene,min,max) {
	# each but the last column of the population presents one gene of each individual
	# the last column of the population show the fitness of each individual
	population <- matrix(nrow=candidates, ncol=(gene+1))
	for(i in 1:candidates) {
		population[i,] <- runif(gene+1,min,max)
		population[i,gene+1] <- 0
	}
	return(population)
}

evaluate <- function(landscape,population) {
	for(i in 1:nrow(population)) {
		population[i,4] <- landscape(population[i,1:3])
	}
	return(population)
}

mutateOffspring <- function(population,mutateRate,gene) {
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
		if(mutateOnlyOneGene) {
			for(j in 1:(ncol(population)-1)) {
				newPopulation[nrow(population)+i,j] <- population[i,j]
			}
			j <- sample(1:gene,1)
			newPopulation[nrow(population)+i,j] <- population[i,j] + mutateRate * sample(signs,1,replace=TRUE)
		} else {
			for(j in 1:(ncol(population)-1)) {
				newPopulation[nrow(population)+i,j] <- population[i,j] + mutateRate * sample(signs,1,replace=TRUE)
			}
		}
	}
	return(newPopulation)
}

fpsSelection <- function(population,newSize) {
	population <- population[order(population[,ncol(population)]),]
	return(population[1:newSize,])
}

mainEA <- function(landscape, generations, size, gene, mutateRate, xMin, xMax, yMin, yMax) {
	population <- initPopulation(size,gene,xMin,xMax)
	population <- evaluate(landscape,population)	
	generation = 1
	while(generations > generation) {		
		population <- mutateOffspring(population,mutateRate,gene)
		population <- evaluate(landscape,population)
		population <- fpsSelection(population,size)
		generation <- generation + 1
		
		# plot everything
		plot(p1, xlim=c(xMin,xMax), ylim=c(yMin,yMax), pch=20, col="green")
		points(p2, pch=20, col="blue")
		x = seq(xMin,xMax,0.1)
		for(i in 1:nrow(population)) {
			w <- population[i,]
			y = (w[3]-x*w[1])/w[2]   
			lines(x,y)
		}
	}
	return(population)
}

# and go for it
population <- mainEA(landscape, generations, sizeOfPopulation, gene, mutateRate, xMin, xMax, yMin, yMax)
