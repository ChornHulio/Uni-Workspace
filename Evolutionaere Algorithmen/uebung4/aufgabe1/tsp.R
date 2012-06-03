# settings
fileDistMatrix <- "dist3.txt"
filePoints <- "punkte3.txt"
generations <- 100
sizeOfPopulation <- 100
sizeOfParents <- 5
sizeOfOffsprings <- 5

# read data
distmatrix <- as.matrix(read.table(fileDistMatrix))
points <- as.matrix(read.table(filePoints))

# calc number of gene out of points
gene <- nrow(points)

# init population with random values
# @return population
initPopulation <- function() {
	population <- matrix(nrow=sizeOfPopulation, ncol=gene)
	for(i in 1:sizeOfPopulation) {
		population[i,] <- sample(1:gene,gene,replace=F)
	}
	return(population)
}

# landscape (fitness function)
# @return fitness (double value)
landscape <- function(individual) {
	# hole distance of the way
	distance <- 0.0
	from <- individual[1]
	for(i in 2:gene) {
		to <- individual[i]
		distance <- distance + distmatrix[min(from,to),max(from,to)]	
		from <- individual[i]
	}
	# and back to the start point
	to <- individual[1]
	distance <- distance + distmatrix[min(from,to),max(from,to)]
	return(distance)
}

# evaluate the population
# @return fitness vector
evaluate <- function(population) {
	fitness = vector("double",nrow(population))
	for(i in 1:nrow(population)) {
		fitness[i] <- landscape(population[i,])
	}
	return(fitness)
}

# select parents (pairs that are nominated for crossover)
# @return parents (pairs -> matrix)
selectParents <- function(population) {
	parents = matrix(ncol=2,nrow=sizeOfParents)
	for(i in 1:sizeOfParents) {
		parents[i,] <- sample(population,2,replace=F)
	}
	return(parents)
}

noCrossover <- function(population) {
	return(population)
}

edge3CreateLists <- function(parentA,parentB) {
	# lists of parentA
	listsA <- matrix(0,nrow=length(parentA),ncol=2)
	listsA[parentA[1],1] <- parentA[2]
	listsA[parentA[1],2] <- parentA[length(parentA)]
	for(i in 2:(length(parentA)-1)) {
		listsA[parentA[i],1] <- parentA[i-1]
		listsA[parentA[i],2] <- parentA[i+1]
	}
	listsA[parentA[length(parentA)],1] <-parentA[length(parentA)-1]
	listsA[parentA[length(parentA)],2] <- parentA[1]
	
	# lists of parentB
	listsB <- matrix(0,nrow=length(parentB),ncol=2)
	listsB[parentB[1],1] <- parentB[2]
	listsB[parentB[1],2] <- parentB[length(parentB)]
	for(i in 2:(length(parentB)-1)) {
		listsB[parentB[i],1] <- parentB[i-1]
		listsB[parentB[i],2] <- parentB[i+1]
	}
	listsB[parentB[length(parentB)],1] <-parentB[length(parentB)-1]
	listsB[parentB[length(parentB)],2] <- parentB[1]
	
	# concat lists
	lists <- matrix(0,nrow=length(parentA),ncol=4)
	for(i in 1:length(parentA)) {
		lists[i,] <- c(listsA[i,],listsB[i,])
	}
	return(lists)
}

edge3RemoveElementFromLists <- function(element,lists) {
	for(i in 1:nrow(lists)) {
		for(j in 1:ncol(lists)) {
			if(lists[i,j] == element) {
				lists[i,j] <- -1
			}
		}
	} 
	return(lists)
}

edge3IsEmpty <- function(list) {
	for(i in 1:length(list)) {
		if(list[i] > 0) {
			return(FALSE)
		}
	}
	return(TRUE)
}

edge3ElementOf <- function(element,offspring) {
	for(i in 1:length(offspring)) {
		if(offspring[i] == element) {
			return(TRUE)
		}
	}
	return(FALSE)
}

egde3NumberOfUniqueElements <- function(list) {
	counter <- 0
	# is there a common edge?
	for(i in 1:(length(list)-1)) {
		for(j in (i+1):length(list)) {
			if(list[i] == list[j]) {
				counter <- -1
			}
		}
	}
	# count
	for(i in 1:length(list)) {
		if(list[i] > 0) {
			counter <- counter + 1
		}
	}
	return(counter)
}

edge3GetNextElement <- function(lastElement,lists,offspring) {
	# if list is empty (take a random element which is not already in the offspring)
	if(edge3IsEmpty(lists[lastElement,])) {
		while(TRUE) {
			nextEl <- sample(1:nrow(lists),1)
			if(!edge3ElementOf(nextEl,offspring)) {
				return(nextEl)
			}
		}
	}
	
	# if there is a common edge take this element
	list <- lists[lastElement,]
	for(i in 1:(length(list)-1)) {
		if(list[i] > 0) {
			for(j in (i+1):length(list)) {
				if(list[i] == list[j]) {
					return(list[i])
				}
			}
		}
	}
	
	# if there is a shortest list take this element
	list <- lists[lastElement,]
	elements <- Inf
	unique <- FALSE
	index <- 0
	for(element in list) {
		if(element > 0) {
			numberOfElements <- egde3NumberOfUniqueElements(lists[element,])
			if(numberOfElements == elements) {
				unique <- FALSE
			} else if(numberOfElements > 0 && numberOfElements < elements) {
				elements <- elements
				unique <- TRUE
				index <- element
			}
		}
	}
	if(unique) {
		return(index)
	}
	
	# else (take a random element of the current list)
	while(TRUE) {
		element <- sample(lists[lastElement,],1)
		if(element > 0) {
			return(element)
		}
	}
}

edge3Algorithm <- function(parents) {
	parentA <- parents[1,]
	parentB <- parents[2,]
	offspring <- vector("double",gene)
	lists <- edge3CreateLists(parentA,parentB)
	offspring[1] <- sample(1:length(parentA),1)
	for(i in 2:length(parentA)) {
		lists <- edge3RemoveElementFromLists(offspring[i-1],lists)
		offspring[i] <- edge3GetNextElement(offspring[i-1],lists,offspring)
	}
	return(offspring)
}

edge3Crossover <- function(population) {
	parents <- selectParents(population) # select parents
	newPop <- matrix(nrow=sizeOfPopulation+sizeOfParents, ncol=gene)
	# copy current population
	for(i in 1:sizeOfPopulation) {
		newPop[i,] <- population[i,] 
	}
	# crossover
	for(i in 1:sizeOfParents) {
		newPop[sizeOfPopulation+i,] <- edge3Algorithm(population[parents[i,],])
	}
	return(newPop)
}

swapMutation <- function(population) {
	newPop <- matrix(nrow=sizeOfPopulation+sizeOfOffsprings, ncol=gene)
	# copy current population
	for(i in 1:sizeOfPopulation) {
		newPop[i,] <- population[i,] 
	}
	# mutation
	for(i in 1:sizeOfOffsprings) {
		toSwap <- sample(1:gene,2,replace=F)
		individual <- population[i,]
		tmpGene <- individual[toSwap[2]]
		individual[toSwap[2]] <- individual[toSwap[1]]
		individual[toSwap[1]] <- tmpGene
		newPop[sizeOfPopulation+i,] <- individual
	}
	return(newPop)
}

noMutation <- function(population) {
	return(population)
}

fpsSelection <- function(population,fitness,newSize) {
	population <- population[order(fitness),]
	return(population[1:newSize,])
}

mainEA <- function(crossoverFunc,mutationFunc,selectionFunc) {
	population <- initPopulation()
	fitness <- evaluate(population)
	generation = 1
	while(generations > generation) {
		population <- crossoverFunc(population)		
		population <- mutationFunc(population)
		fitness <- evaluate(population)
		population <- selectionFunc(population,fitness,sizeOfPopulation)
		generation <- generation + 1		
		points(generation,sort(fitness)[1]) # plot
	}
	return(population)
}

# plot settings
plot(-10,xlim=c(0,generations), ylim=c(0,170),xlab="Generation",ylab="Distance")
par(pch=16)

# and go for it
# first round
par(col="black")
population <- mainEA(edge3Crossover,noMutation,fpsSelection)
fitness <- evaluate(population)
cat("after GA with Swap Mutation: "
	,population[order(fitness),][1,],"\n")

# second round
par(col="blue")
population <- mainEA(noCrossover,swapMutation,fpsSelection)
fitness <- evaluate(population)
cat("after GA with Edge3 Crossover: "
	,population[order(fitness),][1,],"\n")
	
# third round
par(col="green")
population <- mainEA(edge3Crossover,swapMutation,fpsSelection)
fitness <- evaluate(population)
cat("after GA with Swap Mutation and Edge3 Crossover: "
	,population[order(fitness),][1,],"\n")

