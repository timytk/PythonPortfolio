# 3) a)
unif <- c(runif(100000, 0, 1))
fInv <- tan(pi * unif - (pi/2))


optM <- function(x){
  xCau <- 1/(pi*(1+x^2))
  xNorm <- dnorm(x)
  xRet <- xNorm/xCau
  return(xRet)
}

optMax <- optimize(optM, c(-10,10), maximum = TRUE, tol = 1e-6)
optMax$objective

x <- seq(-5, 5, 0.001)
y <- dnorm(x)
plot(x,y)
max(y)


# b)


hist(fInv[truInd], freq = FALSE, breaks = 31, 
     main = "Histogram of Inverse Transform Method for Cauchy Distribution", 
     ylab = "Frequency", xlab = "x", sub = "Cauchy distribution shown with line")


xVal <- seq(-6, 6, 0.001)
yCau <- 1/(pi*(1 + xVal^2))

yNorm <- dnorm(xVal)
lines(xVal, yNorm, type = "l")



#fInv <- tan(pi * uSam - (pi/2))
fInv2 <- fInv
truInd <- which(abs(fInv) < 6)


rejectCauchy <- function(sample){
  # Initialises a temporary vector
  retVec <- c()
  # Creates M
  optMax <- optimize(optM, c(-10,10), maximum = TRUE, tol = 1e-6)
  # Goes through the number of samples
  for(i in 1:length(sample)){
    # fInv is from a previous question
    yi <- fInv[i]
    # We calculate our uMg(y) and the norm(y)
    uMgx <- sample[i] * optMax$objective * 1/(pi*(1+yi^2))
    fx <- dnorm(yi)
    # If its satisfies the condition, add it to return vector
    if(uMgx < fx){
      retVec <- append(retVec, yi)
    }
  }
  return(retVec)
}

uSam <- c(runif(100000, 0, 1))
valPoi <- rejectCauchy(uSam)

hist(valPoi, freq = FALSE, main = "Histogram for 3c", 
     sub = "Normal Distribution shown with line", ylab = "Density",
     xlab = "x", )

x <- seq(-10,10,0.01)
y <- dnorm(x)
lines(x, y)

test1 <- length(valPoi)/100000
test1
test2 <- 1/optMax$objective
test2
