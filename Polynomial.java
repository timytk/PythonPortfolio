/*
 * PROJECT II: Polynomial.java
 *
 * This file contains a template for the class Polynomial. Not all methods are
 * implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file.
 *
 * This class is designed to use Complex in order to represent polynomials
 * with complex co-efficients. It provides very basic functionality and there
 * are very few methods to implement! The project formulation contains a
 * complete description.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file! You should also test this class using the main()
 * function.
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
 * NAME: Timothy Yap Tze Kien
 * UNIVERSITY ID: 2161367
 * DEPARTMENT: Warwick Mathematics Institute
 */

import java.util.Arrays; 

public class Polynomial {
    /**
     * An array storing the complex co-efficients of the polynomial.
     */
    Complex[] coeff;

    // ========================================================
    // Constructor functions.
    // ========================================================

    /**
     * General constructor: assigns this polynomial a given set of
     * co-efficients.
     *
     * @param coeff  The co-efficients to use for this polynomial.
     */
    public Polynomial(Complex[] coeff) {
        // You need to fill in this function.
        while(coeff[coeff.length - 1].getReal() == 0 && coeff[coeff.length - 1].getImag() == 0 && coeff.length != 1){
            Complex[] coefficients = new Complex[coeff.length - 1];
            for(int i = coeff.length - 2; 0 <= i; i--){
                coefficients[i] = coeff[i];
            }
            coeff = coefficients;
        }
        this.coeff = coeff;

    }

    
    /**
     * Default constructor: sets the Polynomial to the zero polynomial.
     */
    public Polynomial() {
        // You need to fill in this function.
        this.coeff = new Complex[] {new Complex()};
    }

    // ========================================================
    // Operations and functions with polynomials.
    // ========================================================

    /**
     * Return the coefficients array.
     *
     * @return  The coefficients array.
     */
    public Complex[] getCoeff() {
        // You need to fill in this method with the correct code.
        return this.coeff;
    }

    /**
     * Create a string representation of the polynomial.
     * Use z to represent the variable.  Include terms
     * with zero co-efficients up to the degree of the
     * polynomial.
     *
     * For example: (-5.000+5.000i) + (2.000-2.000i)z + (-1.000+0.000i)z^2
     */
    public String toString() {
        // You need to fill in this method with the correct code.
        // This is a StringBuilder that starts with the constant and appends the following terms
        StringBuilder polyStr = new StringBuilder();
        polyStr.append("(");
        polyStr.append(coeff[0].toString());
        polyStr.append(")");
        for(int i = 1; i < coeff.length; i++){
            polyStr.append(" + ");
            polyStr.append("(");
            polyStr.append(coeff[i].toString());
            polyStr.append(")z^");
            polyStr.append(Integer.toString(i));
        }
        return polyStr.toString();
    }

    /**
     * Returns the degree of this polynomial.
     */
    public int degree() {
        // You need to fill in this method with the correct code.
        return this.coeff.length - 1;
    }

    /**
     * Evaluates the polynomial at a given point z.
     *
     * @param z  The point at which to evaluate the polynomial
     * @return   The complex number P(z).
     */
    public Complex evaluate(Complex z) {
        // You need to fill in this method with the correct code.
        // This uses the method stated in the Project2.pdf
        Complex eval = new Complex();
        int deg = this.coeff.length - 1;
        // If it is a constant then it just returns the constant
        if(deg == 0){
            eval = this.coeff[0];
        }
        // Else it will take the smallest bracket as eval and for loop it till it reaches the constant plus the final eval
        else{
            eval.setReal(this.coeff[deg].getReal());
            eval.setImag(this.coeff[deg].getImag());
            for(int i = deg - 1; 0 <= i; i--){
                eval = this.coeff[i].add(eval.multiply(z));
            }
        }
        return eval;
    }

    
    // ========================================================
    // Tester function.
    // ========================================================

    public static void main(String[] args) {
        // You can fill in this function with your own testing code.
        // Creating Complex Polynomials
        Complex a1 = new Complex(1,1);
        Complex a2 = new Complex(1,2);
        Complex a3 = new Complex(2,-1);
        Polynomial PolyA = new Polynomial(new Complex[] {a1, a2, a3});
        Complex b1 = new Complex(1,0);
        Complex b2 = new Complex();
        Complex b3 = new Complex();
        Polynomial PolyB = new Polynomial(new Complex[] {b1, b2, b3});
        Complex c1 = new Complex(1,0);
        Complex c2 = new Complex(2,-2);
        Complex c3 = new Complex(3,1);
        Complex c4 = new Complex(2,-3);
        Complex c5 = new Complex();
        Polynomial PolyC = new Polynomial(new Complex[] {c1, c2, c3, c4, c5});
        Polynomial PolyD = new Polynomial();

        // Printing new polynomials
        System.out.println("Testing Constructors:");
        System.out.println("PolyA = "+PolyA.toString());    
        System.out.println("PolyB = "+PolyB.toString());
        System.out.println("PolyC = "+PolyC.toString());
        System.out.println("PolyD = "+PolyD.toString());

        // Testing operations and functions with polynomials
        System.out.println();
        System.out.println("Testing Operations and Functions:");
        System.out.println("Coeffs of PolyA = "+Arrays.toString(PolyA.getCoeff()));
        System.out.println("Coeffs of PolyB = "+Arrays.toString(PolyB.getCoeff()));
        System.out.println("Coeffs of PolyC = "+Arrays.toString(PolyC.getCoeff()));
        System.out.println("Coeffs of PolyD = "+Arrays.toString(PolyD.getCoeff()));
        
        System.out.println("Degree of PolyA = "+PolyA.degree());
        System.out.println("Degree of PolyB = "+PolyB.degree());
        System.out.println("Degree of PolyC = "+PolyC.degree());
        System.out.println("Degree of PolyD = "+PolyD.degree());
        
        Complex eval1 = new Complex(1,1);
        System.out.println("Evaluate PolyA at (1 + i)= "+PolyA.evaluate(eval1));
        Complex eval2 = new Complex(2,-2);
        System.out.println("Evaluate PolyB at (2 - 2i)= "+PolyB.evaluate(eval2));
        Complex eval3 = new Complex(4,-2);
        System.out.println("Evaluate PolyC at (4 - 2i)= "+PolyC.evaluate(eval3));
        Complex eval4 = new Complex(10,-20);
        System.out.println("Evaluate PolyD at (10 - 20i)= "+PolyD.evaluate(eval4));



    }
}