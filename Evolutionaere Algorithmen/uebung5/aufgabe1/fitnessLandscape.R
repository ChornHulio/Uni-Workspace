# settings
load("landscape.Rdata")
generations <- 100
sizeOfPopulation <- 5
gene <- 2
mutationRate <- rep(5,sizeOfPopulation) # integer numbers
min <- 1 # borders of landscape
max <- 150
learnOverall <- 0.5
learnCoord <- 0.6
mutationMin <- 0
mutationMax <- 10
prop <- 0.5 # for playing with mutation

# init population with random values
# @return population
initPopulation <- function() {
	population <- matrix(nrow=sizeOfPopulation, ncol=gene)
	for(i in 1:sizeOfPopulation) {
		population[i,] <- runif(gene,min,max)
	}
	return(population)
}

# adapt the mutation rate
# @return mutation rate
adaptMutationRate <- function(mutationRate) {
	forAll <- prop*learnOverall*rnorm(1)
	for(i in 1:sizeOfPopulation) {
		forThis <- prop*learnCoord*rnorm(1)
		mutationRate[i] <- mutationRate[i] * exp(forAll + forThis)
		mutationRate[i] <- min(mutationMax,mutationRate[i])
		mutationRate[i] <- max(mutationMin,mutationRate[i])
	}
	return(mutationRate)
}

# mutate a individual
# @return pair of parent and child
mutation <- function(individual,mutationStep) {
	pair <- matrix(nrow=2, ncol=gene)
	signs <- c(-1,1)
	# copy parent
	pair[1,] <- individual
	# mutate parent -> child
	for(j in 1:gene) {
		newValue <- round(individual[j] + mutationStep *rnorm(1))
		newValue <- min(max,newValue)
		newValue <- max(min,newValue)
		pair[2,j] <- newValue
	}
	return(pair)
}

# evaluate the population
# @return fitness vector
evaluate <- function(pair) {
	fitness = vector("double",nrow(pair))
	for(i in 1:nrow(pair)) {
		fitness[i] <- landscape[pair[i,1],pair[i,2]]
	}
	return(fitness)
}

# select the fitter of a pair
# @return the fitter individual
selection <- function(pair,fitness) {
	pair <- pair[order(fitness,decreasing=TRUE),]
	return(pair[1,])
}

mainEA <- function() {
	x <- 1:150
	y <- 1:150
	contour(x,y,landscape)	
	population <- initPopulation()
	generation = 1
	while(generations > generation) {
		mutationRate <- adaptMutationRate(mutationRate)
		for(i in 1:sizeOfPopulation) {
			pair <- mutation(population[i,],mutationRate[i])
			fitness <- evaluate(pair)
			population[i,] <- selection(pair,fitness)
		}
		generation <- generation + 1
		
		# plot	
		points(population[,1],population[,2],col="blue",pch=16)
	}
	return(population)
}

# and go for it
population <- mainEA()
points(population[,1],population[,2],col="green",pch=16)


