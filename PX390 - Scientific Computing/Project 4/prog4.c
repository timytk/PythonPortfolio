/*****************************************************************
 * This programme defines functions used to generate band
 * matrix for problems in which a solution at some grid point
 * k is coupled to a solution at a number of adjasont points.
 * This has been discussed for both, the implicit method
 * of finding solution to time dependent heat equation and
 * for a stationary solution of the heat equation with given
 * sources. The file also includes a main() function with an
 * example of how to solve a stationary heat equation with a
 * constant source.
 * 
 * Prior to compilation execute following lines on nenneke:
 * module purge
 * module load intel impi imkl
 * Then:
 * Compile:  gcc -o bandu band_utility.c -lm -lmkl -liomp5
 * Run: ./bandu
 * ****************************************************************/
#include <stdlib.h>
#include <stdio.h>
#include <mkl_lapacke.h>

/* Define structure that holds band matrix information */
struct band_mat{
  long ncol;        /* Number of columns in band matrix */
  long nbrows;      /* Number of rows (bands in original matrix) */
  long nbands_up;   /* Number of bands above diagonal */
  long nbands_low;  /* Number of bands below diagonal */
  double *array;    /* Storage for the matrix in banded format */
  /* Internal temporary storage for solving inverse problem */
  long nbrows_inv;  /* Number of rows of inverse matrix */
  double *array_inv;/* Store the inverse if this is generated */
  int *ipiv;        /* Additional inverse information */
};
/* Define a new type band_mat */
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

/* Set an element of a band matrix to a desired value based on the pointer
   to a location in the band matrix, using the row and column indexes
   of the full matrix.           */
double setv(band_mat *bmat, long row, long column, double val) {
  *getp(bmat,row,column) = val;
  return val;
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

int printmat(band_mat *bmat) {
  long i, j;
  for (i = 0; i < bmat -> ncol; i++){
    for(j=0; j<bmat->nbrows; j++) {
      printf("%ld %ld %g \n",i,j,bmat->array[bmat->nbrows*i + j]);
    }
  }
  return 0;
}

void swap(double **a, double **b){
  double * temp;
  temp = *a;
  *a = *b;
  *b = temp;
}

/* An example of how to use the band matrix routines to solve a PDE:
   The equation solved is related to the steady state solution of the heat 
   diffusion equation.   
*/

int main() {
  FILE *file;
    int number;
    
    // Open file in read mode
    file = fopen("input.txt", "r");

    // Check if opened successfully
    if (file == NULL){
        printf("Error opening the file.\n");
        return 1;
    }

    // Set each variable from txt file
    double xL, xR, aL, aR, bL, bR, E0;
    long int N, ite;
    fscanf(file, "%lf", &xL);
    fscanf(file, "%lf", &xR);
    fscanf(file, "%ld", &N);
    fscanf(file, "%lf", &aL);
    fscanf(file, "%lf", &aR);
    fscanf(file, "%lf", &bL);
    fscanf(file, "%lf", &bR);
    fscanf(file, "%lf", &E0);
    fscanf(file, "%ld", &ite);

    // Close file
    fclose(file);

    // Open file in read mode
    file = fopen("potential.txt", "r");

    // Check if opened successfully
    if (file == NULL){
        printf("Error opening the file.\n");
        return 1;
    }

    // Set potential from txt file
    double *potential = (double *) malloc(N * sizeof(double));
    for (int i = 0; i < N; i++){
      fscanf(file, "%lf", &potential[i]);
    }

    // Close the file
    fclose(file);

  band_mat bmat;
  long ncols = N;
  /* We have a three-point stencil (domain of numerical dependence) of
     our finite-difference equations:
     1 point to the left  -> nbands_low = 1
     1       to the right -> nbands_up  = 1
  */
  long nbands_low = 1;  
  long nbands_up = 1;
  init_band_mat(&bmat, nbands_low, nbands_up, ncols);
  double *psi = (double *) malloc(N * sizeof(double));
  double *psiPlusOne = (double *) malloc(N * sizeof(double));

  double deltaX = (xR - xL) / (N - 1);

  long i;
  /* Loop over the equation number and set the matrix
     values equal to the coefficients of the grid values. 
     Note that boundaries are treated with special cases */

  for (int i = 0; i<ncols; i++){
    setv(&bmat, i, i, 2 / (deltaX * deltaX) + potential[i] - E0);
    if (i > 0){
      setv(&bmat, i, i - 1, -1 / (deltaX * deltaX));
    }
    if (i < N - 1){
      setv(&bmat, i, i + 1, -1 / (deltaX * deltaX));
    }
  }

  if (bL != 0){
    setv(&bmat, 0, 0, (2 / (deltaX * deltaX)) * (1 - ((aL * deltaX) / bL)) + potential[i] - E0);
    setv(&bmat, 0, 1, (-2 / (deltaX * deltaX)));
  }

  else{
    setv(&bmat, 0, 0, 1);
    setv(&bmat, 0, 1, 0);
  }
  
  if (bR != 0){
    setv(&bmat, N - 1, N - 1, (2 / (deltaX * deltaX)) * (1 + ((aR * deltaX) / bR)) + potential[i] - E0);
    setv(&bmat, N - 1, N - 2, (-2 / (deltaX * deltaX)));
  }
  
  else{
    setv(&bmat, N - 1, N - 1, 1);
    setv(&bmat, N - 1, N - 2, 0);
  }

  for (int i = 0; i < N; i ++){
    psi[i] = (double)rand() / (double)RAND_MAX;
  }

  file = fopen("output.txt", "w");

   // Check if opened successfully
    if (file == NULL) {
        printf("Error opening the file.\n");
        return 1;
    }

    // Write data into file (i, x, psi_i(x))
    for (int i = 0; i < ite; i++){
        solve_Ax_eq_b(&bmat, psiPlusOne, psi);
        swap(&psiPlusOne, &psi);
        for (int j = 0; j < N; j++){ 
            fprintf(file, "%d, %lf, %lf\n", i, j * deltaX, psi[j]);
        }
    }

    // Close the file
    fclose(file);

    // Freeing memory
    free(potential);
    free(psi);
    free(psiPlusOne);
  finalise_band_mat(&bmat);
  return(0);
}
