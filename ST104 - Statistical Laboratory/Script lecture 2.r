

#################
x<-rnorm(10, mean=0, sd=1)
x

m<-sum(x)/length(x)
m
# Formula for var: sum((xi-xibar)^2)/(n-1)
var<-sum((x-m)^2)/(length(x)-1)
var

mean(x)
var(x)

#######################
x<-c(8, 7, 10, 19, 20, 14, 19, 4, 12, 11)
y<-c(15, 13, 5, 4, 12, 20, 11, 14, 15, 6)

ind<-which(x<10)
ind

x[-ind]

x[ind]<-y[ind]
x
#################################


#########################

matrix(c(1,3,5,7,9,11,13,15,17,19,21,23),nrow=3)
matrix(c(1,7,13,19,3,9,15,21,5,11,17,23),ncol=4, byrow=T)

matrix(seq(from=1,by=2,length=12), nrow=3)

############################

A<- matrix(c(1,2,3),1,3)		# 1x3 matrix
B <- matrix(4:5,3,1,byrow=TRUE)	# 3x1 matrix
B <- t(B)					# 1x3 matrix
C <- rbind(A,B)				# merge A and B by rows to get a 2x3times
C[1,2:3]					# get the values on first row, and second and third column


#############################

dat <- data.frame(id = letters[1:3], x = 1:3, y = 11:13)
dat
dat[1,3]
dat[["y"]]

#################################


sum.of.squares=function(x, location=mean(x)){
	z=sum((x-location)^2)
	return(z)
}

sum.of.squares2=function(x, location=mean(x)){
	int=x-location
	int2=int^2
	z=sum(int2)
	return(z)
}


sum.of.squares(c(-1,0,1))
sum.of.squares2(c(-1,0,1))

sum.of.squares(c(-1,0,1), location=c(1,1,1))
sum.of.squares2(c(-1,0,1), location=c(1,1,1))

sum.of.squares(c(1,4,2))
sum.of.squares(c(1,4,2), location=c(0,0,0))

#################################


celsius_to_farenheit<-function(temp_C){
	temp_F<-9/5*temp_C+32
	return(temp_F)
}
celsius_to_farenheit(c(0,100))
celsius_to_farenheit(0)
celsius_to_farenheit(100)
#################################

multiple.return=function(x){
	s=sum(x)
	p=prod(x)
	mat=matrix(x,nrow=2)
	return(list(s,p,mat))
}
multiple.return(1:4)
multiple.return(c(1,4,-2,5))
vec=sample(1:10,4)
vec
multiple.return(vec)


###################################


all.non.negative <- function(x){
  y <- x >=0
  if (all(y)){ return(x)
  } else { print("Some elements in the input vector are negative")
  }
}

all.non.negative(c(1,4,5))
all.non.negative(c(1,-4,5))


##########################################
## Several possibilities

n <- 20
fibonacci <- numeric(length=n)	# or also fibonacci<-vector()
for (i in 1:n) {
  if (i==1){fibonacci[i] <- 0}
  if (i==2){fibonacci[i] <- 1}
  if (i >2){fibonacci[i]<- fibonacci[i-1]+ fibonacci[i-2]}
}
fibonacci




n <- 20
i <- 1
fibonacci <- numeric(length=n)	# or also fibonacci<-vector()
while (i <=n){
  if (i==1){
    fibonacci[1] <- 0}
  if (i==2){
    fibonacci[2] <- 1}
  if (i >2){
    fibonacci[i] <- fibonacci[i-1] + fibonacci[i-2]
  }
  i <- i+1
}


fibonacci


n=20
x=c(0,1)
while (length(x) < n) {
		position <- length(x)
		new <- x[position] + x[position-1]
		x <- c(x,new)
	}
n=20
x=c(0,1)
for (i in 3:20){
	x[i]=x[i-1]+x[i-2]
}
x



fibonacci.function<-function(n){
	fibonacci <- numeric(length=n)	# or also fibonacci<-vector()
	for (i in 1:n) {
  		if (i==1){fibonacci[i] <- 0}
  		if (i==2){fibonacci[i] <- 1}
  		if (i >2){fibonacci[i]<- fibonacci[i-1]+ fibonacci[i-2]}
	}
	return(fibonacci)
}
fibonacci.function(20)

fibonacci2<-function(x){
	if (n==1) {return(0)}
	x <- c(0,1)
	while (length(x) < n) {
		position <- length(x)
		new <- x[position] + x[position-1]
		x <- c(x,new)
	}
	return(x)	
}
fibonacci2(20)

##################################################

c(rep(1,4), 3:6)
P <- matrix(c(rep(1,4), 3:6), nrow = 4, ncol = 2)
P
P[3,1]

####################################

x<- rep(1,4)
x
y <- c(seq(from=1, to=3, by=0.5))
y
x*y

#####################################

k <- 100
if(k > 100){
  print("Greater than 100")
} else if (k < 100){
  print("Less than 100")
} 

########################################

my.fn <- function(x) {
  if (x==0) {
    return(1)
  } else if (x==1) {
    return(1)
  } else {
    return(x * my.fn(x-1))
  }
}

my.fn(3)
my.fn(5)

apply(matrix(c(1:10),nrow=1), 2, FUN=my.fn)
factorial(1:10)
#########################################


quadratic<-function(a,b,c){
	if (a!=0){
		delta=b^2-4*a*c
		if(delta<0){
			print("no solutions")
		}
		else if (delta==0){
			print("1 double solution")
			sol0=-b/(2*a)
			return(sol0)
		}
		else {
			print("2 solutions")
			sols=(-b+c(-1,1)*sqrt(delta))/(2*a)
			return(sols)
		}
	}
	else {
		print("Error: equation is not quadratic") 
	}
}

quadratic(0,1,1)

quadratic(1,0,0)
quadratic(1,2,3)
quadratic(-1,2,3)

quadratic.complex<-function(a,b,c){
	if (a!=0){
		delta=b^2-4*a*c
		if(delta<0){
			print("2 complex solutions")
			sols=(-b+c(-1,1)*sqrt(as.complex(delta)))/(2*a)
			return(sols)
		}
		else if (delta==0){
			print("1 double solution")
			sol0=-b/(2*a)
			return(sol0)
		}
		else {
			print("2 solutions")
			sols=(-b+c(-1,1)*sqrt(delta))/(2*a)
			return(sols)
		}
	}
	else {
		print("Error: equation is not quadratic") 
	}
}


quadratic.complex(1,0,1)
quadratic.complex(1,2,3)





########################################


M=matrix(rep(NA,25), ncol=5)
for (i in 1:nrow(M)){
	for (j in i:nrow(M)){
		M[i,i]=0
		M[i,j]=M[i,i]+(j-i)
		M[j,i]=M[i,j]
	}
}
M


M=matrix(rep(0,25), ncol=5)
for (i in 1:nrow(M)){
	for (j in i:nrow(M)){
		M[i,j]=M[i,i]+(j-i)
		M[j,i]=M[i,j]
	}
}
M

#########################################################

stock=100
count=0
while(stock>50 & stock<150){
	count=count+1
	stock=stock+rnorm(1,0,0.1)
}
count




