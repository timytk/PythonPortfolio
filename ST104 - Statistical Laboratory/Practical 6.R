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
plot(x, xiii, type = 'l', main = 'Truncated Weibull Distribution', sub = 'k = 5, lambda = 2', ylab = 'Density', xlab = 'x')
points(optMax$maximum, optMax$objective, pch = 19, col = "blue")


# 1) g)
sample <- uniform2d(10, 3, optMax$objective)

reject <- function(uniMat){
  optMax <-optimize(truncatedweibullpdf, c(0, 3), k = 5, lambda = 2, maximum = TRUE, tol = 1e-6)
  indRem <- c()
  for(i in 1:dim(uniMat)[2]){
    xCoord <- uniMat[1, i]
    yCoord <- uniMat[2, i]
    fx <- truncatedweibullpdf(xCoord, 5, 2)
    if(yCoord > fx){
      indRem <- append(indRem, -i)
    }
    uniMat <- uniMat[indRem]
  }
  return(uniMat)
}

newsample <- reject(sample)


plot(newsample[1,1:dim(newsample)[2]], newsample[2,1:dim(newsample)[2]])



