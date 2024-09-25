## Gravitational force

G<-6.674e-11
m.sun<-1.989e30
m.earth<-5.972e24
r<-147.1e9

F<-G*m.sun*m.earth/r^2
F


## Kinetic energy
# Bolt
m<-94
v<-44.72*1000/3600
KE<-m*v^2/2
KE

# Samuel Touchard
m<-65
v<-100/12.7
KE<-m*v^2/2
KE

### Logical operators
x <- c(1,2,3,4,5)
x[(x>3) | (x<2)]		# returns values of x which are either 
				# higher than 3 or smaller than 2

y<-c(8, 4, 10, 2, 10, 1, 7, 5, 10, 5)
y[(y>4)&(y<8)]		# returns values of y which are simultaneously 
				# higher than 4 and smaller than 8


### Commands
seq(8, -4, by=-3)
seq(8, -4, length.out=5)

rep(1:5, each=2)

rep(1:5, times=2)

seq(1,15, by=2)
c(-1,1)
seq(1,15, by=2)*c(-1,1)


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




