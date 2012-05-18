hillClimbing <- function(fnc,start,weight,delta) {
	x <- vector("double",100)
	x[1] <- start
	for(i in 2:100) { # hundred steps
			k = x[i-1]
			x[i] <- k + weight*((fnc(k+delta) - fnc(k-delta)) / (2 * delta)) 
	}
	return(x)
}

g <- function(x) x
k <- function(x) sin(x)
h <- function(x) x*sin(x)
l <- function(x) 2+cos(x)+sin(2*x)

#choose what you want
myfunc <- k # function
start <- 0 # start value (x-value)
delta <- 0.1 # step width
learningRate <- 0.25 # learning rate

erg <- hillClimbing(myfunc, start, learningRate, delta)
seq <- seq(start,delta*99,by=delta)
plot(erg,myfunc(erg), pch=20, col="blue")
lines(seq,myfunc(seq))
