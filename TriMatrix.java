/*
 * PROJECT III: TriMatrix.java
 *
 * This file contains a template for the class TriMatrix. Not all methods are
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

import java.util.Random;

public class TriMatrix extends Matrix {
    /**
     * An array holding the diagonal elements of the matrix.
     */
    private double[] diagonal;

    /**
     * An array holding the upper-diagonal elements of the matrix.
     */
    private double[] upperDiagonal;

    /**
     * An array holding the lower-diagonal elements of the matrix.
     */
    private double[] lowerDiagonal;
    
    /**
     * Constructor function: should initialise iDim and jDim through the Matrix
     * constructor and set up the values array.
     *
     * @param dimension  The dimension of the array.
     */
    public TriMatrix(int dimension) {
        // You need to fill in this method
        super(dimension, dimension);
        this.diagonal = new double [dimension];
        this.upperDiagonal = new double[dimension - 1];
        this.lowerDiagonal = new double[dimension - 1];
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
        // Depending on i,j, it gives the element from either diagonals or 0
        if(i == j){
            return this.diagonal[i];
        }
        else if(i == j - 1){
            return this.upperDiagonal[i];
        }
        else if(i == j + 1){
            return this.lowerDiagonal[i - 1];
        }
        else{
            return 0;
        }
    }
    
    /**
     * Setter function: set the (i,j)'th entry of the data array.
     *
     * @param i      The location in the first co-ordinate.
     * @param j      The location in the second co-ordinate.
     * @param value  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double value) {
        // You need to fill in this method.
        // Depending on i,j, it changes the value for that diagonal
        if(i == j){
            this.diagonal[i] = value;
        }
        else if(i == j - 1){
            this.upperDiagonal[i] = value;
        }
        else if(i == j + 1){
            this.lowerDiagonal[i - 1] = value;
        }
        else{
            throw new MatrixException("Cannot change that value.");
        }
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.
        // Sets det = 1 and multiplies the numbers on the diagonal.
        double det = 1;
        TriMatrix LUdec = LUdecomp();
        for(int i = 0; i < this.iDim; i++){
            det *= LUdec.getIJ(i,i);
        }
        return det;
    }
    
    /**
     * Returns the LU decomposition of this matrix. See the formulation for a
     * more detailed description.
     * 
     * @return The LU decomposition of this matrix.
     */
    public TriMatrix LUdecomp() {
        // You need to fill in this method.
        // Initialize a TriMatrix to change into the LU decomp
        TriMatrix LUdec = new TriMatrix(this.iDim);
        // The upper diagonal of TriMatrix is same for LU decomp
        LUdec.upperDiagonal = this.upperDiagonal;
        // 1,1 element same as TriMatrix
        LUdec.setIJ(0, 0, this.diagonal[0]);
        // Deriving from the formulation we get this formula
        for(int i = 0; i < this.iDim - 1; i++){
            LUdec.lowerDiagonal[i] = this.lowerDiagonal[i] / LUdec.diagonal[i];
            LUdec.diagonal[i + 1] = this.diagonal[i + 1] - (this.upperDiagonal[i] * LUdec.lowerDiagonal[i]);
        }
        return LUdec;
    }

    /**
     * Add the matrix to another second matrix.
     *
     * @param second  The Matrix to add to this matrix.
     * @return        The sum of this matrix with the second matrix.
     */
    public Matrix add(Matrix second){
        // You need to fill in this method.
        // If the dimensions are correct, then addition is possible
        if(this.iDim == second.iDim && this.iDim == second.jDim){
        // Initializes a value to change to
        double setNew = 0;
        int lastInd = this.iDim - 1;
            // Condition for TriMatrix + TriMatrix = TriMatrix
            if(second instanceof TriMatrix){
                // Creates a new TriMatrix to add both to 
                TriMatrix addTri = new TriMatrix(this.iDim);
                for(int i = 0; i < this.iDim - 1; i++){
                    // Adds value for each diagonal
                    setNew = this.diagonal[i] + second.getIJ(i, i);
                    addTri.setIJ(i, i, setNew);
                    setNew = this.upperDiagonal[i] + second.getIJ(i, i + 1);
                    addTri.setIJ(i, i + 1, setNew);
                    setNew = this.lowerDiagonal[i] + second.getIJ(i + 1, i);
                    addTri.setIJ(i + 1, i, setNew);
                }
                // Adds last value as diagonal has one extra element
                setNew = this.diagonal[lastInd] + second.getIJ(lastInd, lastInd);
                addTri.setIJ(lastInd, lastInd, setNew);
                return addTri;
            }
            else{
                // Addition for TriMatrix and General Matrix
                GeneralMatrix addGen = new GeneralMatrix(this.iDim, this.jDim);
                for(int i = 0; i < this.iDim; i++){
                    for(int j = 0; j < this.jDim; j++){
                    // Sets element i,j to the new value
                    setNew = this.getIJ(i, j) + second.getIJ(i, j);
                    addGen.setIJ(i,j,setNew);
                    }
                }
                return addGen;
            }
        }
        // Throws error when addition is not possible
        else{
            throw new MatrixException("Not correct dimensions");
        }
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
        // Initializes a value to change to
        double setNew = 0;
        int lastInd = this.iDim - 1;
        // Initialize a TriMatrix to change to matrix * scalar
        TriMatrix mulTri = new TriMatrix(this.iDim);
        for(int i = 0; i < this.iDim - 1; i++){
            // Multiplies by scalar and sets that to new TriMatrix
            setNew = this.diagonal[i] * scalar;
            mulTri.setIJ(i, i, setNew);
            setNew = this.upperDiagonal[i] * scalar;
            mulTri.setIJ(i, i + 1, setNew);
            setNew = this.lowerDiagonal[i] * scalar;
            mulTri.setIJ(i + 1, i, setNew);
        }
        // Sets last element as diagonal has one more element
        setNew = this.diagonal[lastInd] * scalar;
        mulTri.setIJ(lastInd, lastInd, setNew);
        return mulTri;
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        // You need to fill in this method.
        // Initializes a value to change to
        double setNew = 0;
        int lastInd = this.iDim - 1;
        Random rand = new Random();
        for(int i = 0; i < this.iDim - 1; i++){
            // Sets each diagonal's element to random
            setNew = rand.nextDouble();
            this.setIJ(i, i, setNew);
            setNew = rand.nextDouble();
            this.setIJ(i, i + 1, setNew);
            setNew = rand.nextDouble();
            this.setIJ(i + 1, i, setNew);
        }
        // Sets last element as diagonal has one more element
        setNew = rand.nextDouble();
        this.setIJ(lastInd, lastInd, setNew);
    }
    
    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // Test your class implementation using this method.
        // Constructing TriMatrix
        TriMatrix A = new TriMatrix(1);
        TriMatrix B = new TriMatrix(4);
        TriMatrix C = new TriMatrix(4);
        TriMatrix D = new TriMatrix(5);
        GeneralMatrix E = new GeneralMatrix(4,4);

        // Setting matrix values
        A.setIJ(0,0,1);

        B.setIJ(0,0,3);
        B.setIJ(0,1,2);
        B.setIJ(1,0,5);
        B.setIJ(1,1,7);
        B.setIJ(1,2,2);
        B.setIJ(2,1,3);
        B.setIJ(2,2,7);
        B.setIJ(2,3,3);
        B.setIJ(3,2,4);
        B.setIJ(3,3,1);

        C.setIJ(0,0,7);
        C.setIJ(0,1,8);
        C.setIJ(1,0,5);
        C.setIJ(1,1,3);
        C.setIJ(1,2,8);
        C.setIJ(2,1,7);
        C.setIJ(2,2,3);
        C.setIJ(2,3,7);
        C.setIJ(3,2,6);
        C.setIJ(3,3,9);

        D.setIJ(0,0,6);
        D.setIJ(0,1,2);
        D.setIJ(1,0,3);
        D.setIJ(1,1,4);
        D.setIJ(1,2,8);
        D.setIJ(2,1,3);
        D.setIJ(2,2,2);
        D.setIJ(2,3,1);
        D.setIJ(3,2,2);
        D.setIJ(3,3,1);
        D.setIJ(3,4,7);
        D.setIJ(4,3,4);
        D.setIJ(4,4,1);

        E.setIJ(0,0,1);
        E.setIJ(0,1,2);
        E.setIJ(0,2,3);
        E.setIJ(0,3,4);
        E.setIJ(1,0,1);
        E.setIJ(1,1,2);
        E.setIJ(1,2,3);
        E.setIJ(1,3,4);
        E.setIJ(2,0,1);
        E.setIJ(2,1,2);
        E.setIJ(2,2,3);
        E.setIJ(2,3,4);
        E.setIJ(3,0,1);
        E.setIJ(3,1,2);
        E.setIJ(3,2,3);
        E.setIJ(3,3,4);

        // Printing TriMatrix
        System.out.println("Constructor Test:");
        System.out.println("A = " + A.toString());
        System.out.println("B = \n" + B.toString());
        System.out.println("C = \n" + C.toString());
        System.out.println("D = \n" + D.toString());
        System.out.println("E = \n" + E.toString());

        // Other Tests
        System.out.println("Various Tests:");
        A.setIJ(0,0,3);
        System.out.println("Setting A to 3 = " + A.toString());

        System.out.println("B(2,2) = " + B.getIJ(1,1));
        System.out.println("B(2,1) = " + B.getIJ(1,0));
        System.out.println("B(1,2) = " + B.getIJ(0,1));

        System.out.println("det B = " + B.determinant());

        System.out.println("B + C = \n" + B.add(C).toString());

        System.out.println("10 * (B + C) = \n" + B.add(C).multiply(10).toString());

        System.out.println("B * C = \n" + B.multiply(C).toString());

        B.random();
        System.out.println("Random 4 x 4 matrix = " + B.toString());

        System.out.println("B + E = \n" + B.add(E));

        System.out.println("D = \n" + D.toString());
    }
}