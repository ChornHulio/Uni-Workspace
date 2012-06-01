data <- read.table("data1.txt")
x <- as.matrix(data[,1:(ncol(data)-1)])
y <- as.vector(as.matrix(data[,ncol(data)]))
w <- vector("double",ncol(data)-1)

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

# train
L <- 1
while(L != 0) {
	L <- 0
	for(i in 1:nrow(data)) {
		delta <- y[i] - sign(x[i,] %*% w)
		if(delta != 0) {
			L <- L + 1
			w <- w + delta*x[i,]
		}
		# plot
		plot(p1, xlim=c(-7,20), ylim=c(-5,17), pch=20, col="green")
		points(p2, pch=20, col="blue")
		plotX <- seq(-7,20,0.1)
		plotY <- (w[3]-plotX*w[1])/w[2]   
		lines(plotX,plotY) # plot orthogonal vector
	}
	cat("wrong classes: ", L, "\n")
}

# set function
h <- function(u) {
	return(sign(u %*% w))
}
return(h)
