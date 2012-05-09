cat("------ one nearest neighbor with R ------\n")

# functions
euklid <- function(aData,bData) {
	sum = 0.0
	for(i in 1:(ncol(aData)-1)) {
		sum = sum + (aData[,i] - bData[,i]) ** 2
	}
	return(sqrt(sum))
}

# read data
train <- read.table("data/golub50.train")
test <- read.table("data/golub50.test")

# check data
if(ncol(train) != ncol(test)) {
	cat("error: wrong input data")
	q()
}

# find for every test frame...
errors = 0
for(i in 1:nrow(test)) {

	nearestTrain = -1
	distance = -1
	
	# ...the train frame...
	for(j in 1:nrow(train)) {
		
		# ...with the minimum distance...
		cur = euklid(train[j,],test[i,])
		if(distance > cur | distance < 0) {
			distance = cur
			nearestTrain = j
		}
	}
	
	# ...and check if their classes are the same
	if(test[i,ncol(test)] != train[j,ncol(train)]) {
		errors = errors + 1
	}
}

# print errors
cat("wrong classes: ", errors, "\n")
