# 2) c)
unif <- c(runif(100000, 0, 1))
fInv <- tan(pi * unif - (pi/2))
truInd <- which(abs(fInv) < 6)



hist(fInv[truInd], freq = FALSE, breaks = 31, 
     main = "Histogram of Inverse Transform Method for Cauchy Distribution"
     , xlab = "x", sub = "Cauchy distribution shown with line")

xCau <- seq(-6, 6, 0.001)
yCau <- 1/(pi*(1 + xCau^2))
lines(xCau, yCau, type = "l")


# e)
unif <- c(runif(100000, 0, 1))
intVal <- mean(2/(pi*(1+(2*unif+5)^2)))
intVal