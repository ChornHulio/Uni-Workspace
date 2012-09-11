perfA <- function(r) {
	return(sqrt(r))
}

perfB <- function(r) {
	return(r^(1./3))
}

speedup <- function(f,n,r,perf) {
	return(1. / (((1.-f)/perf(r)) + (f/(perf(r)+n-r))))
}

r <- 1:1024
n <- 1024

png()
par(mfrow= c(2,2))
f <- 0.8
plot(r,speedup(f,n,r,perfA),pch=16,ylab='speedup',main='perf(r) = r^(1/2), f = 0.8')
cat("r=",678,": ",max(speedup(f,n,r,perfA)),"\n")
f <- 0.5
plot(r,speedup(f,n,r,perfA),pch=16,ylab='speedup',main='perf(r) = r^(1/2), f = 0.5')
cat("r=",835,": ",max(speedup(f,n,r,perfA)),"\n")
f <- 0.8
plot(r,speedup(f,n,r,perfB),pch=16,ylab='speedup',main='perf(r) = r^(1/3), f = 0.8')
cat("r=",748,": ",max(speedup(f,n,r,perfB)),"\n")
f <- 0.5
plot(r,speedup(f,n,r,perfB),pch=16,ylab='speedup',main='perf(r) = r^(1/3), f = 0.5')
cat("r=",875,": ",max(speedup(f,n,r,perfB)),"\n")
dev.off() 
