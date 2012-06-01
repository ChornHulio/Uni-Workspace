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
	for(i in 1:nrow(population)) {
		population[i,2] <- landscape(population[i,1])
	}
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

selectNextGen <- function(population,newSize) {
	population <- population[order(population[,ncol(population)]),]
	return(population[nrow(population):(nrow(population)-newSize),])
}

mainEA <- function(landscape, generations, candidates, gene, mutateRate, xMin, xMax, yMin, yMax) {
	population <- initPopulation(candidates,gene,xMin,xMax)
	population <- evaluate(landscape,population)
	
	# plot
	plot(population[,1],population[,2],xlim=c(xMin,xMax),ylim=c(yMin,yMax))
	seq <- seq(xMin,xMax,by=0.1) 
	lines(seq,landscape(seq))
	
	generation = 1
	while(generations > generation) {		
		population <- mutateOffspring(population,mutateRate)
		population <- evaluate(landscape,population)
		population <- selectNextGen(population,candidates)
		generation <- generation + 1
		
		# plot
		plot(population[,1],population[,2],xlim=c(xMin,xMax),ylim=c(yMin,yMax))
		seq <- seq(xMin,xMax,by=0.1) 
		lines(seq,landscape(seq))
	}
	return(population)
}

# define functions (landscapes)
borders <- function(x,y) {
	if(x > 10 || x < 0) {
		return(-Inf)
	}
	return(y)
}

g <- function(x) borders(x,x)
k <- function(x) borders(x,sin(x))
h <- function(x) borders(x,x*sin(x))
l <- function(x) borders(x,2+cos(x)+sin(2*x))

# settings
landscape <- l
generations <- 100
sizeOfPopulation <- 10
gene <- 1
mutateRate <- 0.1
xMin <-  0 # for first initalisation
xMax <- 10 # for first initalisation
yMin <- -10 # for plot
yMax <-  10 # for plot

# and go for it
population <- mainEA(landscape, generations, sizeOfPopulation, gene, mutateRate, xMin, xMax, yMin, yMax)
