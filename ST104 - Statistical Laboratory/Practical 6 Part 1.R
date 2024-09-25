# 1) b)

truncatedweibullpdf <- function(x, k, lambda){
  # Checks if k, lambda is over 0
  if (k <= 0 | lambda <= 0){
    return("Invalid Inputs")
  }
  # Set A value
  A <- 1 - exp(-(3/lambda)^k)
  # Find and return value of x
  xVal <- A * (k/lambda) * ((x/lambda)^(k-1)) * exp(-(x/lambda)^k)
  return(xVal)
}


# 1) c)
x <- seq(0,3,0.01)
xi <- truncatedweibullpdf(x, 1, 1)
xii <- truncatedweibullpdf(x, 2, 1)
xiii <- truncatedweibullpdf(x, 5, 2)

plot(x, xi, type = 'l', main = 'Truncated Weibull Distribution', sub = 'k = 1, lambda = 1', ylab = 'Density', xlab = 'x')
plot(x, xii, type = 'l', main = 'Truncated Weibull Distribution', sub = 'k = 2, lambda = 1', ylab = 'Density', xlab = 'x')
plot(x, xiii, type = 'l', main = 'Truncated Weibull Distribution', sub = 'k = 5, lambda = 2', ylab = 'Density', xlab = 'x')


# 1) e)
uniform2d <- function(n, Mx, My){  
  # Generates a uniform sample over 0 to the Max x and y
  x <- runif(n, 0, Mx)
  y <- runif(n, 0, My)
  # Puts x and y into a 2 x n matrix and returns it
  coords <- matrix(c(x,y), nrow = 2, ncol = n, byrow = TRUE)
  return(coords)
}


# 1) f)
optMax <-optimize(truncatedweibullpdf, c(0, 3), k = 5, lambda = 2, maximum = TRUE, tol = 1e-6)
plot(x, xiii, type = 'l', main = 'Truncated Weibull Distribution', sub = 'k = 5, lambda = 2', ylab = 'y', xlab = 'x')
points(optMax$maximum, optMax$objective, pch = 19, col = "blue")


# 1) g)
reject <- function(uniMat){
  # Initialises a temporary vector and variable
  matTemp <- c()
  retDim <- 0
  # Goes through the number of coordinates
  for(i in 1:dim(uniMat)[2]){
    xCoord <- uniMat[1, i]
    yCoord <- uniMat[2, i]
    # Calculates the x value for index i
    fx <- truncatedweibullpdf(xCoord, 2, 1)
    # If its below the graph, add it to temporary vector
    if(yCoord < fx){
      matTemp <- append(matTemp, c(xCoord, yCoord))
      retDim <- retDim + 1
    }
  }
  # Using the number of coordinates (retDim), and matTemp vector
  # we create a 2 x n matrix that we return
  retMat <- matrix(matTemp, nrow = 2, ncol = retDim)
  return(retMat)
}

optMax <-optimize(truncatedweibullpdf, c(0, 3), k = 2, lambda = 1, maximum = TRUE, tol = 1e-6)

sample <- uniform2d(50000, 3, optMax$objective)

newsample <- reject(sample)
dim(newsample)[2]

plot(x, xi, type = 'l', main = 'Truncated Weibull Distribution', sub = 'k = 1, lambda = 1', ylab = 'y', xlab = 'x')
plot(x, xii, type = 'l', main = 'Truncated Weibull Distribution', sub = 'k = 2, lambda = 1', ylab = 'y', xlab = 'x')
plot(x, xiii, type = 'l', main = 'Truncated Weibull Distribution', sub = 'k = 5, lambda = 2', ylab = 'y', xlab = 'x')

points(newsample[1,1:dim(newsample)[2]], newsample[2,1:dim(newsample)[2]], col = "blue")


hist(newsample[1,1:dim(newsample)[2]], main = 'Weibull Distribution Histogram', sub = 'k = 2, lambda = 1', ylab = 'Density', xlab = 'x')



