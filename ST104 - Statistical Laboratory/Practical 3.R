# Student ID: u2161367 
# Lab Group 14
# Tutor: Ollie Kemp

# 1. Vectors
vector_i <- seq(from = -8, to = 13, by = 3)
vector_ii <- rep(seq(2, 12, 2), each = 3)
vector_iii <- rep(seq(9, -6, -3), seq(6, 1))
vector_iv <- seq(1, 10)**seq(2, 11)
vector_v <- (rep(3, 15)**rep(seq(1, 5), each = 3))*c(-1,0,1)

vector_i
vector_ii
vector_iii
vector_iv
vector_v

# 2. Random numbers and vectors
# Generate vector x with 1000 realisations from a Normal(8,3) distribution
x <- rnorm(1000, mean = 8, sd = sqrt(3))
x_i <- x[seq(200,1000,200)]
x_ii <- x[101:899]
x_iii <- length(which((6 < x) & (x < 9)))
x_iv <- sample(x[which(x < 6)], 500, replace = TRUE)



# 3. Short Mathematical Questions
# 1. Leap Years
# a. Lists years that are divisible by 4
years_mod4 <- function(x){
  # checks for any invalid inputs such as negative years or non-integers
  if (any(x < 0) | any(x %% 1 != 0)){
    return("INVALID INPUT")
  }
  # finds the index of which element is divisible by 4 and returns them
  else{
    years <- x[which(x %% 4 == 0)]
    return(years)
  }
}

# b. List of leap years
leap_years <- function(x){
  # uses previous function 
  years <- years_mod4(x)
  # if there is invalid inputs, then it'll return that statement
  if (any(years == "INVALID INPUT")){
    return(years)
  }
  # if its valid, it removes the non-leap year years
  else{
    ind <- which((years %% 100 != 0) | (years %% 400 == 0))
    leap <- years[ind]
    return(leap)
  }
}


# c. Number of leap years from 1515 to 2022
seq.c <- seq(1515, 2022)
leap.c <- length(leap_years(seq.c))
leap.c

# 2. Prime Numbers
# a. primefind; checks if x %% = 0 for all numbers in between 2:sqrt(x)
primefind <- function(x){
  # first two if's checks for invalid inputs
  if (is.numeric(x) == FALSE){
    return(FALSE)
  }
  if (length(x) != 1 | x < 0 | x %% 1 != 0){
    return(FALSE)
  }
  if (x == 2){
    return(TRUE)
  }
  # checks if x is divisible by any number between 2 and floor(sqrt(x))
  if (any(x %% (2:sqrt(x)) == 0)){
    return(FALSE)
  } 
  else{
    return(TRUE)
  }
}

# b. List of prime function
prime.list <- function(x){
  primes <- c()
  # adds primes to list 'primes'
  for(i in 1:length(x)){
    if (primefind(x[i]) == TRUE){
      primes <- c(primes, x[i])
    }
  }
  return(primes)
}

# c. List of primes between 1 and 300
primes.c <- prime.list(seq(1,300))
primes.c

# 4. Longer mathematical question
# a. i. The argument that is passed to the cubetest function
# might be off by a acceptable tolerance. 
# Some code  might pass 7.9999999 instead of 8 and that can cause issues.
cubetest<-function(x){
  b<-round(x^(1/3), digits=6)
  if(b%%1==0){return("Cube number")
  } else {
    return("Not a cube number")
  }
}

# a. ii. function that returns only cube numbers
cube.list <- function(x){
  # checks if there are any invalid inputs
  if (any(x < 0) | any(x %% 1 != 0)){
    return("INVALID INPUT")
  }
  # then returns only the cube numbers; similar to 'cubetest'
  else{ 
    ind <- which(round(x^(1/3), 6) %% 1 == 0)
  cubes <- x[ind]
  return(cubes)
  }
}

# b. i. function takes x, y and sees if x^3 + y^3 is a cube number
fermats.theorem <- function(x, y){
  z.cube <- x^3 + y^3
  if (cubetest(z.cube) == "Cube number"){
    return("Cube number")
  }
  if (cubetest(z.cube) == "Not a cube number"){
    return("Not a cube number")
  }
}


# b. ii. testing for x, y = {1:100}
for (x in 1:100){
  for (y in 1:100){
    if(fermats.theorem(x, y) == "Cube number"){
      print(x, y)
    }
  }
}




