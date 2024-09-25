/*
 * PROJECT III: GeneralMatrix.java
 *
 * This file contains a template for the class GeneralMatrix. Not all methods
 * implemented and they do not have placeholder return statements. Make sure 
 * you have carefully read the project formulation before starting to work 
 * on this file. You will also need to have completed the Matrix class.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file!
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 * 
 * Tasks:
 *
 * 1) Complete this class with the indicated methods and instance variables.
 *
 * 2) Fill in the following fields:
 *  
 * NAME: Timothy Yap
 * UNIVERSITY ID: 2161367
 * DEPARTMENT: Warwick Mathematics Institute
 */

import java.util.Arrays;
import java.util.Random;

public class GeneralMatrix extends Matrix {
    /**
     * This instance variable stores the elements of the matrix.
     */
    private double[][] values;

    /**
     * Constructor function: should initialise iDim and jDim through the Matrix
     * constructor and set up the data array.
     *
     * @param firstDim   The first dimension of the array.
     * @param secondDim  The second dimension of the array.
     */
    public GeneralMatrix(int firstDim, int secondDim) {
        // You need to fill in this method.
        super(firstDim, secondDim);
        this.values = new double[iDim][jDim];
    }

    /**
     * Constructor function. This is a copy constructor; it should create a
     * copy of the second matrix.
     *
     * @param second  The matrix to create a copy of.
     */
    public GeneralMatrix(GeneralMatrix second) {
        // You need to fill in this method.
        super(second.iDim, second.jDim);
        this.values = new double[iDim][jDim];
        for(int i = 0; i < iDim; i++){
            for(int j = 0; j < jDim; j++){
                // gets the value from i,j in order to set i,j to that value
                setIJ(i,j,second.getIJ(i,j));
            }
        }
    }
    
    /**
     * Getter function: return the (i,j)'th entry of the matrix.
     *
     * @param i  The location in the first co-ordinate.
     * @param j  The location in the second co-ordinate.
     * @return   The (i,j)'th entry of the matrix.
     */
    public double getIJ(int i, int j) {
        // You need to fill in this method.
        return this.values[i][j];
    }
    
    /**
     * Setter function: set the (i,j)'th entry of the values array.
     *
     * @param i      The location in the first co-ordinate.
     * @param j      The location in the second co-ordinate.
     * @param value  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double value) {
        // You need to fill in this method.
        values[i][j] = value;
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.
        // Passing LUdecomp a double array of sign length 1
        double[] sign = new double[1];
        // Checks for errors and if there is, sends and error message
        try{
            this.LUdecomp(sign);
        }
        catch(MatrixException e){
            System.out.println("Something went wrong here.");
        }
        // Sets det = 1 and multiplies the numbers on the diagonal.
        // Changes sign depending whether sign[0] is negative or not
        double detAbs = 1;
        GeneralMatrix LUdec = LUdecomp(sign);
        for(int i = 0; i < iDim; i++){
            detAbs *= LUdec.getIJ(i,i);
        }
        double det = detAbs * sign[0];
        return det;
    }

    /**
     * Add the matrix to another second matrix.
     *
     * @param second  The Matrix to add to this matrix.
     * @return   The sum of this matrix with the second matrix.
     */
    public Matrix add(Matrix second) {
        // You need to fill in this method.
        // Creates a variable to add both elements
        double setNew = 0;
        for(int i = 0; i < this.iDim; i++){
            for(int j = 0; j < this.jDim; j++){
                // Sets element i,j to the new value
                setNew = this.values[i][j] + second.getIJ(i,j);
                this.setIJ(i,j,setNew);  
            }
        }
        return this;
    }
    
    /**
     * Multiply the matrix by another matrix A. This is a _left_ product,
     * i.e. if this matrix is called B then it calculates the product BA.
     *
     * @param A  The Matrix to multiply by.
     * @return   The product of this matrix with the matrix A.
     */
    public Matrix multiply(Matrix A) {
        // You need to fill in this method.
        if(this.jDim != A.iDim){
            throw new MatrixException("Wrong dimensions.");
        }
        // Creates a new matrix of the correct size
        GeneralMatrix mulMat = new GeneralMatrix(this.iDim,A.jDim);
        // Creates a variable to change and set later
        double setNew = 0;
        // Three for loops to goes through row and columns and
        // muliplies the correct number of elements from B and A
        for(int i = 0; i < this.iDim; i++){
            for(int j = 0; j < A.jDim; j++){
                for(int k = 0; k < this.jDim; k++){
                    setNew += this.getIJ(i,k) * A.getIJ(k,j);
                }
            mulMat.setIJ(i,j,setNew);
            setNew = 0;
            }
        }
        return mulMat;
    }

    /**
     * Multiply the matrix by a scalar.
     *
     * @param scalar  The scalar to multiply the matrix by.
     * @return        The product of this matrix with the scalar.
     */
    public Matrix multiply(double scalar) {
        // You need to fill in this method.
        // Creates a variable to multiply an element and scalar
        double setNew = 0;
        for(int i = 0; i < this.iDim; i++){
            for(int j = 0; j < this.jDim; j++){
                // Sets element i,j to the new value
                setNew = scalar * this.values[i][j];
                this.setIJ(i,j,setNew);  
            }
        }
        return this;
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        // You need to fill in this method.
        Random rand = new Random();
        // Goes through each i,j and changes it to 0 to 1
        for(int i = 0; i < this.iDim; i++){
            for(int j = 0; j < this.jDim; j++){
                double setNew = rand.nextDouble();
                setIJ(i, j, setNew);
            }
        }
    }

    /**
     * Returns the LU decomposition of this matrix; i.e. two matrices L and U
     * so that A = LU, where L is lower-diagonal and U is upper-diagonal.
     * 
     * On exit, decomp returns the two matrices in a single matrix by packing
     * both matrices as follows:
     *
     * [ u_11 u_12 u_13 u_14 ]
     * [ l_21 u_22 u_23 u_24 ]
     * [ l_31 l_32 u_33 u_34 ]
     * [ l_41 l_42 l_43 u_44 ]
     *
     * where u_ij are the elements of U and l_ij are the elements of l. When
     * calculating the determinant you will need to multiply by the value of
     * sign[0] calculated by the function.
     * 
     * If the matrix is singular, then the routine throws a MatrixException.
     * In this case the string from the exception's getMessage() will contain
     * "singular"
     *
     * This method is an adaptation of the one found in the book "Numerical
     * Recipies in C" (see online for more details).
     * 
     * @param sign  An array of length 1. On exit, the value contained in here
     *              will either be 1 or -1, which you can use to calculate the
     *              correct sign on the determinant.
     * @return      The LU decomposition of the matrix.
     */
    public GeneralMatrix LUdecomp(double[] sign) {
        // This method is complete. You should not even attempt to change it!!
        if (jDim != iDim)
            throw new MatrixException("Matrix is not square");
        if (sign.length != 1)
            throw new MatrixException("d should be of length 1");
        
        int           i, imax = -10, j, k; 
        double        big, dum, sum, temp;
        double[]      vv   = new double[jDim];
        GeneralMatrix a    = new GeneralMatrix(this);
        
        sign[0] = 1.0;
        
        for (i = 1; i <= jDim; i++) {
            big = 0.0;
            for (j = 1; j <= jDim; j++)
                if ((temp = Math.abs(a.values[i-1][j-1])) > big)
                    big = temp;
            if (big == 0.0)
                throw new MatrixException("Matrix is singular");
            vv[i-1] = 1.0/big;
        }
        
        for (j = 1; j <= jDim; j++) {
            for (i = 1; i < j; i++) {
                sum = a.values[i-1][j-1];
                for (k = 1; k < i; k++)
                    sum -= a.values[i-1][k-1]*a.values[k-1][j-1];
                a.values[i-1][j-1] = sum;
            }
            big = 0.0;
            for (i = j; i <= jDim; i++) {
                sum = a.values[i-1][j-1];
                for (k = 1; k < j; k++)
                    sum -= a.values[i-1][k-1]*a.values[k-1][j-1];
                a.values[i-1][j-1] = sum;
                if ((dum = vv[i-1]*Math.abs(sum)) >= big) {
                    big  = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (k = 1; k <= jDim; k++) {
                    dum = a.values[imax-1][k-1];
                    a.values[imax-1][k-1] = a.values[j-1][k-1];
                    a.values[j-1][k-1] = dum;
                }
                sign[0] = -sign[0];
                vv[imax-1] = vv[j-1];
            }
            if (a.values[j-1][j-1] == 0.0)
                a.values[j-1][j-1] = 1.0e-20;
            if (j != jDim) {
                dum = 1.0/a.values[j-1][j-1];
                for (i = j+1; i <= jDim; i++)
                    a.values[i-1][j-1] *= dum;
            }
        }
        
        return a;
    }

    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // Test your class implementation using this method.
        // Constructs general matrix
        GeneralMatrix A = new GeneralMatrix(1,1);
        GeneralMatrix B = new GeneralMatrix(2,2);
        GeneralMatrix C = new GeneralMatrix(1,2);
        GeneralMatrix D = new GeneralMatrix(2,2);

        // Setting matrix values
        A.setIJ(0,0,2);

        B.setIJ(0,0,1);
        B.setIJ(0,1,0);
        B.setIJ(1,0,0);
        B.setIJ(1,1,1);

        C.setIJ(0,0,2);
        C.setIJ(0,1,3);

        D.setIJ(0,0,1);
        D.setIJ(0,1,2);
        D.setIJ(1,0,3);
        D.setIJ(1,1,4);

        // Testing Converters
        System.out.println("Constructor test:");
        System.out.println("A = " + A.toString());
        System.out.println("B = " + B.toString());
        System.out.println("C = " + C.toString());
        System.out.println("D = " + D.toString());

        // Testing accessors, mutators, and others
        System.out.println("Various tests:");
        A.setIJ(0,0,3);
        System.out.println("A(1,1) set to 3 = " + A.toString());

        GeneralMatrix Bcopy = new GeneralMatrix(B);
        System.out.println("Copy of B = " + Bcopy.toString());

        B.add(D);
        System.out.println("B + D = " + B.toString());

        System.out.println("C * D = " + C.multiply(D).toString());

        Bcopy.multiply(2);
        System.out.println("2B = " + Bcopy.toString());

        System.out.println("det of 2B = " + Bcopy.determinant());

        Bcopy.random();
        System.out.println("A 2x2 random matrix = " + Bcopy.toString());
        System.out.println("The det = " + Bcopy.determinant());

        GeneralMatrix F = new GeneralMatrix(3,3);
        F.setIJ(0,0,4);
        F.setIJ(0,1,3);
        F.setIJ(0,2,2);
        F.setIJ(1,0,1);        
        F.setIJ(1,1,10);
        F.setIJ(1,2,3);
        F.setIJ(2,0,4);
        F.setIJ(2,1,8);        
        F.setIJ(2,2,7);
        System.out.println("F = " + F.toString());
        System.out.println("det of F = " + F.determinant());

        GeneralMatrix E = new GeneralMatrix(4,4);
        E.setIJ(0,0,4);
        E.setIJ(0,1,3);
        E.setIJ(0,2,2);
        E.setIJ(0,3,1);        
        E.setIJ(1,0,1);
        E.setIJ(1,1,10);
        E.setIJ(1,2,3);
        E.setIJ(1,3,4);        
        E.setIJ(2,0,5);
        E.setIJ(2,1,3);
        E.setIJ(2,2,2);
        E.setIJ(2,3,-4);        
        E.setIJ(3,0,4);
        E.setIJ(3,1,8);
        E.setIJ(3,2,7);
        E.setIJ(3,3,9);
        System.out.println("E = " + E.toString());
        System.out.println("det of E = " + E.determinant());
    }
}
