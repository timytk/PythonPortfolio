/* 
 * PROJECT I: Circle.java
 *
 * This file contains a template for the class Circle. Not all methods are
 * implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file. You will also need to have completed
 * the Point class.
 *
 * Remember not to change the types, names, parameters or return types of any
 * functions or variables in this file!
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 *
 * Author: Timothy Yap
 * Warwick ID: 2161367
 * 
 */

public class Circle {

    /*
     * Here, YOU should define private variables that represent the radius and
     * centre of this particular Circle. The radius should be of type double,
     * and the centre should be of type Point.
     */

     // r
    private double r;

     // A
    private Point A;
    

    // =========================
    // Constructors
    // =========================
    /**
     * Default constructor - performs no initialization.
     */
    public Circle() {
        // This method is complete.
    }
    
    /**
     * Alternative constructor, which sets the circle up with x and y
     * co-ordinates representing the centre, and a radius. Remember you should
     * not store these x and y co-ordinates explicitly, but instead create a
     * Point to hold them for you.
     *
     * @param xc   x-coordinate of the centre of the circle
     * @param yc   y-coordinate of the centre of the circle
     * @param rad  radius of the circle
     */
    public Circle(double xc, double yc, double rad) {
        // You need to fill in this method.
        setCentre(xc, yc); 
        setRadius(rad);
    }

    /**
     * Alternative constructor, which sets the circle up with a Point
     * representing the centre, and a radius.
     *
     * @param centre  Point representing the centre
     * @param rad     Radius of the circle
     */
    
    public Circle(Point centre, double rad) {
        // You need to fill in this method.
        setCentre(centre);
        setRadius(rad);
    }

    // =========================
    // Setters and Getters
    // =========================

    /**
     * Setter - sets the co-ordinates of the centre.
     *
     * @param xc  new x-coordinate of the centre
     * @param yc  new y-coordinate of the centre
     */   
    public void setCentre(double xc, double yc) {
        // You need to fill in this method.
        this.A = new Point(xc, yc); 
    }

    /**
     * Setter - sets the centre of the circle to a new Point.
     *
     * @param C  A Point representing the new centre of the circle.
     */   
    public void setCentre(Point C) {
        // You need to fill in this method.
        this.A = C;
    }
    
    /**
     * Setter - change the radius of this circle.
     *
     * @param rad  New radius for the circle.
     */   
    public void setRadius(double rad) {
        // You need to fill in this method.
        this.r = rad;
    }
    
    /**
     * Getter - returns the centre of this circle.
     *
     * @return The centre of this circle (a Point).
     */   
    public Point getCentre(){
        // You need to fill in this method.
        return A;
    }

    /**
     * Getter - extract the radius of this circle.
     *
     * @return The radius of this circle.
     */   
    public double getRadius(){
        // You need to fill in this method.
        return r;
    }

    // =========================
    // Convertors
    // =========================

    /**
     * Calculates a String representation of the Circle.
     *
     * @return A String of the form: "[Ax,Ay,Radius]" where Ax and Ay are
     *         numerical values of the coordinates, and Radius is a numerical
     *         value of the radius.
     */
    public String toString() {
        // You need to fill in this method.
        return String.format("[%.9f,%.9f,%.9f]", A.getX(), A.getY(), r);
    }
    
    // ==========================
    // Service routines
    // ==========================
    
    /**
     * Similar to the equals() function in Point. Returns true if two Circles
     * are equal. By this we mean:
     * 
     * - They have the same radius (up to the tolerance defined in Point).
     * - They have the same centre (up to the tolerance defined in Point).
     * 
     * Remember that the second test is already done in the Point class!
     * 
     * @param c The circle to compare this with.
     * @return true if the two circles are equal.
     */
    public boolean equals(Circle c) {
        // You need to fill in this method.
        return (Math.abs(A.getX() - c.getCentre().getX()) <= Point.GEOMTOL &&
                Math.abs(A.getY() - c.getCentre().getY()) <= Point.GEOMTOL &&
                Math.abs(r - c.r) <= Point.GEOMTOL);
    }
    
    // -----------------------------------------------------------------------
    // Do not change the method below! It is essential for marking the
    // project, and you may lose marks if it is changed.
    // -----------------------------------------------------------------------

    /**
     * Compare this Circle with some Object, using the test above.
     *
     * @param obj  The object to compare this with.
     * @return true if the two objects are equal.
     */
    public boolean equals(Object obj) {
        // This method is complete.
        
        if (obj instanceof Circle) {
            boolean test = false;
            Circle C = (Circle)obj;
            
            test = this.equals(C);

            return test;
        } else {
            return false;
        }
    }

    // ======================================
    // Implementors
    // ======================================
    
    /**
     * Computes and returns the area of the circle.
     *
     * @return  Area of the circle
     */
    public double area() {
        // You need to fill in this method.
        double area;
        area =  Math.PI*Math.pow(r, 2);
        return area;
    }

    // =======================================================
    // Tester - test methods defined in this class
    // =======================================================
    
    public static void main(String args[]) {
        // You can use this method for testing.
        // Testing constructors and convertors and printing them
        System.out.println("Testing constructors and convertors");
        Circle A = new Circle(0,0,1);
        Point Bcentre = new Point(1,1);
        Circle B = new Circle(Bcentre,1);
        Circle C = new Circle(9.87654,4.56789,1.23456);
        Circle D = new Circle(3,4,2);
        Circle E = new Circle(3,4,1);
        System.out.println("A = " + A.toString());
        System.out.println("B = " + B.toString());
        System.out.println("C = " + C.toString());
        System.out.println("D = " + D.toString());
        System.out.println("E = " + E.toString());

        // Testing setters and getters
        System.out.println("Testing setters and getters");
        A.setCentre(1,1);
        System.out.println("A with its new centre = " + A.toString());
        Point Bsetcentre = new Point(3,4);
        B.setCentre(Bsetcentre);
        System.out.println("B with its new centre = " + B.toString());
        D.setRadius(1);
        System.out.println("D with its new radius = " + D.toString());
        System.out.println("Centre of C = " + C.getCentre().toString());
        System.out.println("Radius of C = " + String.format("%.9f",C.getRadius()));

        // Testing service routines
        System.out.println("Testing service routines");
        System.out.println("'B is equal to D' is " + B.equals(D));
        System.out.println("'D is equal to E' is " + D.equals(E));
        System.out.println("'A is equal to C' is " + A.equals(C));

        // Testing implementors
        System.out.println("The area of A = " + String.format("%.9f", A.area()));
        System.out.println("The area of B = " + String.format("%.9f", B.area()));
        System.out.println("The area of C = " + String.format("%.9f", C.area()));
    }
}
