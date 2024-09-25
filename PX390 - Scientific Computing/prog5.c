/*****************************************************************
 * This programme defines functions used to generate band
 * matrix for problems in which a solution at some grid point
 * k is coupled to a solution at a number of adjasont points.
 * The file also includes a main() function with an
 * example of how to solve a stationary 2D heat equation with a
 * constant source, for two different sources.
 * 
 * Prior to compilation execute following lines on nenneke:
 * module purge
 * module load intel impi imkl
 * Then:
 * Compile:  gcc -o w10wrkshop w10wrkshop.c -lmkl -liomp5 -lm 
 * Run: ./w10wrkshop
 * ****************************************************************/
#include <stdlib.h>
#include <stdio.h>
#include <mkl_lapacke.h>
#include <math.h>

struct band_mat{
  long ncol;        /* Number of columns in band matrix            */
  long nbrows;      /* Number of rows (bands in original matrix)   */
  long nbands_up;   /* Number of bands above diagonal           */
  long nbands_low;  /* Number of bands below diagonal           */
  double *array;    /* Storage for the matrix in banded format  */
  /* Internal temporary storage for solving inverse problem */
  long nbrows_inv;  /* Number of rows of inverse matrix   */
  double *array_inv;/* Store the inverse if this is generated */
  int *ipiv;        /* Additional inverse information         */
};
typedef struct band_mat band_mat;

/* Initialise a band matrix of a certain size, allocate memory,
   and set the parameters.  */ 
int init_band_mat(band_mat *bmat, long nbands_lower, long nbands_upper, long n_columns) {
  bmat->nbrows = nbands_lower + nbands_upper + 1;
  bmat->ncol   = n_columns;
  bmat->nbands_up = nbands_upper;
  bmat->nbands_low= nbands_lower;
  bmat->array      = (double *) malloc(sizeof(double)*bmat->nbrows*bmat->ncol);
  bmat->nbrows_inv = bmat->nbands_up*2 + bmat->nbands_low + 1;
  bmat->array_inv  = (double *) malloc(sizeof(double)*(bmat->nbrows+bmat->nbands_low)*bmat->ncol);
  bmat->ipiv       = (int *) malloc(sizeof(int)*bmat->ncol);
  if (bmat->array==NULL||bmat->array_inv==NULL) {
    return 0;
  }  
  /* Initialise array to zero */
  long i;
  for (i=0;i<bmat->nbrows*bmat->ncol;i++) {
    bmat->array[i] = 0.0;
  }
  return 1;
};

/* Finalise function: should free memory as required */
void finalise_band_mat(band_mat *bmat) {
  free(bmat->array);
  free(bmat->array_inv);
  free(bmat->ipiv);
}

/* Get a pointer to a location in the band matrix, using
   the row and column indexes of the full matrix.           */
double *getp(band_mat *bmat, long row, long column) {
  int bandno = bmat->nbands_up + row - column;
  if(row<0 || column<0 || row>=bmat->ncol || column>=bmat->ncol ) {
    printf("Indexes out of bounds in getp: %ld %ld %ld \n",row,column,bmat->ncol);
    exit(1);
  }
  return &bmat->array[bmat->nbrows*column + bandno];
}

/* Retrun the value of a location in the band matrix, using
   the row and column indexes of the full matrix.           */
double getv(band_mat *bmat, long row, long column) {
  return *getp(bmat,row,column);
}

void setv(band_mat *bmat, long row, long column, double val) {
  *getp(bmat,row,column) = val;
}

/* Solve the equation Ax = b for a matrix a stored in band format
   and x and b real arrays                                          */
int solve_Ax_eq_b(band_mat *bmat, double *x, double *b) {
  /* Copy bmat array into the temporary store */
  int i,bandno;
  for(i=0;i<bmat->ncol;i++) { 
    for (bandno=0;bandno<bmat->nbrows;bandno++) {
      bmat->array_inv[bmat->nbrows_inv*i+(bandno+bmat->nbands_low)] = bmat->array[bmat->nbrows*i+bandno];
    }
    x[i] = b[i];
  }

  long nrhs = 1;
  long ldab = bmat->nbands_low*2 + bmat->nbands_up + 1;
  int info = LAPACKE_dgbsv( LAPACK_COL_MAJOR, bmat->ncol, bmat->nbands_low, bmat->nbands_up, nrhs, bmat->array_inv, ldab, bmat->ipiv, x, bmat->ncol);
  return info;
}

/* Print matrix in packed 'banded format' (not the underlying square
 coefficients matrix) */
int print_bandmat(band_mat *bmat) {
  long i,j;
  for(i=0; i<bmat->ncol;i++) {
    for(j=0; j<bmat->nbrows; j++) {
      printf("%ld %ld %g \n",i,j,bmat->array[bmat->nbrows*i + j]);
    }
  }
  return 0;
}

/*Check that a grid point has valid coordinates */
int is_valid(long j, long p, long J, long P) {
  return (j>=0)&&(j<J)&&(p>=0)&&(p<P);
}


/* Return the 1D element index corresponding to a particular grid point.
   We can rewrite this function without changing the rest of the code if
   we want to change the grid numbering scheme!
   Output: long integer with the index of the point
   Input:
   long j:  The X grid point index
   long p:  The Y grid point index
   long P:  The number of Y points.
*/
long indx( long j, long p, long P) {
  return j*P + p;
}

/* Return the 2D point corresponding to a particular 1D grid index */
void gridp(long indx, long P, long *j, long *p) {
  *p = indx%P;
  *j = (indx-(*p))/P;
}

// Check where our active cell has active neighbours
void checkNeighbour(int **grid, int i, int j,int Nx,int Ny, int *gridCheck) {
  // Resetting our grid check
  for (int k = 0; k < 4; k++) {
    gridCheck[k] = 0;
  }

  // Check by cases: Case 1 - neighbour is inactive
  if (grid[i][j] == 0) {
    return;
  } 

  // Check if on top edge
  if (is_valid(i, j + 1, Nx, Ny) && grid[i][j + 1] == 1) {
    gridCheck[0] = 1;
  }

  // Check if on bottom edge
  if (is_valid(i, j - 1, Nx, Ny) && grid[i][j - 1] == 1) {
    gridCheck[1] = 1;
  }

  // Check if on left edge
  if (is_valid(i - 1, j, Nx, Ny) && grid[i - 1][j] == 1) {
    gridCheck[2] = 1;
  }

  // Check if on right edge
  if (is_valid(i + 1, j, Nx, Ny) && grid[i + 1][j] == 1) {
    gridCheck[3] = 1;
  }
}

// Swap two values
void swap(double **a, double **b){
  double * temp;
  temp = *a;
  *a = *b;
  *b = temp;
}

// Main code ----------------------
int main() {
  FILE *file;
    
    // Open file in read mode
    file = fopen("input.txt", "r");

    // Validate pointer if opened successfully
    if (file == NULL){
      perror("Error opening the file.\n");
      exit(1);
    }

    // Set each variable from input txt file
    double Lx, Ly, tf, lam, tD;
    long int Nx, Ny, Na;
    fscanf(file, "%ld", &Nx);
    fscanf(file, "%ld", &Ny);
    fscanf(file, "%ld", &Na);
    fscanf(file, "%lf", &Lx);
    fscanf(file, "%lf", &Ly);
    fscanf(file, "%lf", &tf);
    fscanf(file, "%lf", &lam);
    fscanf(file, "%lf", &tD);

    // Close file
    fclose(file);

    // Initialising i, j, and 2d grid
    long x, y;
    int **grid = (int **) malloc(Nx * sizeof(int * ));
    // Validating pointer
    if (grid == NULL){
      printf("Error allocating memory for 2d grid\n");
      exit(1); 
    }
    for (int i = 0; i < Nx; i++) {
      grid[i] = (int *) malloc (Ny * sizeof(int));
      // Validating pointer
      if (grid == NULL){
        printf("Error allocating memory for 2d grid\n");
        exit(1); 
      }
    }

    // Making a 0 grid
    for(int i = 0; i < Nx; i++){
      for(int j = 0; j < Ny; j++){
        grid[i][j] = 0;
      }
    }  

    // Memory allocation for arrays
    double *conVect = (double *) malloc(Na * sizeof(double));
    double *gridInd = (double *) malloc(Na * sizeof(double));
    double *vectInd = (double *) malloc(Nx*Ny * sizeof(double));

    // Check if the file is opened successfully
    if (conVect == NULL || gridInd == NULL || vectInd == NULL) {
      perror("Memory allocation failed.\n");
      exit(1); // Exit if error
    }

    FILE *coefficients = fopen("coefficients.txt", "r");

    // Validate pointer if opened successfully
    if (coefficients == NULL){
      perror("Error opening the file.\n");
      exit(1);
    }
    
    for(int k = 0; k < Na; k++) {
      fscanf(coefficients, "%ld %ld %lf", &x, &y, &conVect[k]);
      gridInd[k] = indx(x, y, Ny);
      vectInd[indx(x, y, Ny)] = k;
      grid[x][y] = 1;
    }
    fclose(coefficients);

    // Creating a band matrix
    band_mat matrix;
    init_band_mat(&matrix, Na, Na, Na);
    double dx, dy, dt;
    dx = Lx / Nx;
    dy = Ly / Ny;
    dt = tD / 2;

    // Grid check with check
    int *gridCheck = (int *) malloc(4 * sizeof(int));

    if (gridCheck == NULL){
      printf("Error allocating memory for grid check\n");
      exit(1);
    }

    // Initialise our coefficients
    double a1, a2, a3, a4, a5;
    a1 = 1 + ((2 * dt) / (dx * dx)) + ((2 * dt) / (dy * dy));
    a2 = (-dt / (dx * dx));
    a3 = (-dt / (dx * dx));
    a4 = (-dt / (dy * dy));
    a5 = (-dt / (dy * dy));

    // Initialise new pair
    long u, v;

    for (int i = 0; i < Na; i++) {
      // printf("Nx: %d\n", i);
      gridp(gridInd[i], Ny, &u, &v);
      // printf("u: %ld %ld\n", u,v);
      checkNeighbour(grid, u, v, Nx, Ny, gridCheck);
      // printf("check: %d %d %d %d\n", gridCheck[0], gridCheck[1],gridCheck[2],gridCheck[3]);

      if (gridCheck[0] == 1) {
      // Both top and bottom
        if (gridCheck[1] == 1) {
          setv(&matrix, i, i, a1);
          setv(&matrix, i, i - 1, a4);
          setv(&matrix, i, i + 1, a5);
        } 
      // Top but no bottom
        else {
          setv(&matrix, i, i, a1);
          setv(&matrix, i, i + 1, 2 * a5);
        }
      }
      else {
        // No top but bottom
        if (gridCheck[1] == 1) {
          setv(&matrix, i, i, a1);
          setv(&matrix, i, i - 1, 2 * a4);
        }
        // Neither top or bottom
        else {
          setv(&matrix, i, i, a1);
        }
      }

      if (gridCheck[2] == 1) {
      // Both left and right
        if (gridCheck[3] == 1) {
          setv(&matrix, i, vectInd[indx(u - 1, v, Ny)], a2);
          setv(&matrix, i, vectInd[indx(u + 1, v, Ny)], a3);
        } 
      // Left but not right
        else {
          setv(&matrix, i, vectInd[indx(u - 1, v, Ny)], 2 * a2);
        }
      }
      else {
        // Not left but right
        if (gridCheck[3] == 1) {
          setv(&matrix, i, vectInd[indx(u + 1, v, Ny)], 2 * a2);
        }
      }
    }

    // Making an output file
    file = fopen("output.txt", "w");

    // Validate pointer if opened successfully
    if (file == NULL){
      perror("Error opening the file.\n");
      exit(1);
    }

  // Finding the next iteration
    double *conVectNext = (double *) malloc(Na * sizeof(double));
    // Validating pointer
    if (conVectNext == NULL){
      printf("Error allocating memory for next iteration\n");
      exit(1); 
    }
    for (int i = 0; i < (tf / tD); i++) {
      for (int j = 0; j < (tD / dt); j++) {
        for (int k = 0; k < Na; k++) {
          conVect[k] = (dt * lam + 1) * conVect[k] - (dt * conVect[k] * conVect[k] * conVect[k]);
        } 
        solve_Ax_eq_b(&matrix, conVectNext, conVect);
        swap(&conVectNext, &conVect);
      }
      for (int k = 0; k < Na; k++) {
        gridp(gridInd[k], Ny, &u, &v);
        fprintf(file, "%lf %ld %ld %lf\n", tD * (i + 1), u, v, conVect[k]);
      }
    }

    // Close file
    fclose(file);

    // Freeing up memory
  
    free(conVect);
    free(gridInd);
    free(vectInd);
    free(gridCheck);
    free(conVectNext);
    finalise_band_mat(&matrix);
    for (int i = 0; i < Nx; i++) {
      free(grid[i]);
    }
    free(grid);
  return(0);
}
