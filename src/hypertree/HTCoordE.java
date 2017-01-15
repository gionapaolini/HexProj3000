/*
 * HTCoordE.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;


/**
 * The HTCoordE class implements the coordinates of a point
 * in the Euclidian space.
 */
class HTCoordE {

    double  x     = 0.0f;  // x coord
    double  y     = 0.0f;  // y coord

    boolean valid = true; // is this E coordinate in the hyperbolic tree ?


  /* --- Constructor --- */

    /**
     * Constructor.
     * x = 0 and y = 0.
     */
    HTCoordE() {}

    /**
     * Constructor copying the given euclidian point.
     *
     * @param z    the euclidian point to copy 
     */
    HTCoordE(HTCoordE z) {
        this.copy(z);
    }

    /**
     * Constructor fixing x and y.
     *
     * @param x    the x coord
     * @param y    the y coord
     */
    HTCoordE(double x, double y) {
        this.x = x;
        this.y = y;
    }


  /* --- Operation --- */

    /**
     * Copy the given HTCoordE into this HTCoordE.
     *
     * @param z    the HTCoordE to copy
     */
    void copy(HTCoordE z) {
        this.x = z.x;
        this.y = z.y;
    }

    /**
     * Substracts the second coord to the first one
     * and put the result in this HTCoorE
     * (this = a - b).
     *
     * @param a    the first coord
     * @param b    the second coord
     */
    void sub(HTCoordE a, HTCoordE b) {
        this.x = a.x - b.x;
        this.y = a.y - b.y;
    }


  /* --- Projections --- */

    /**
     * Projects from Hyperbolic to Euclidian.
     * Just multiply by tanh.
     */
    void projectionHtoE() {
        x = tanh(x);
        y = tanh(y);
    }

    /**
     * Progects from Screen to Euclidian.
     * 
     * @param x        the x screen coordinate
     * @param y        the y screen coordinate
     * @param model    the drawing model
     */
    void projectionStoE(int x, int y, HTDraw model) {
        HTCoordS sOrigin = model.getSOrigin();
        HTCoordS sMax = model.getSMax();
        double ex = (double) (x - sOrigin.x) / (double) sMax.x;
        double ey = (double) (y - sOrigin.y) / (double) sMax.y;

        if (((ex * ex) + (ey * ey)) < 1.0) {
            this.x = ex;
            this.y = ey;
            valid = true;
        } else {
            valid = false;
        }
    } 


  /* --- Validation --- */

    /**
     * Is this coordinate in the hyperbolic tree ?
     *
     * @return    <CODE>true</CODE> if this point is in;
     *            <CODE>false</CODE> otherwise
     */
    boolean isValid() {
        return valid;
    }
  

  /* --- Transformation --- */

    /*
     * Some complex computing formula :
     *
     * d(z)    = (z.x * z.x) + (z.y * z.y) 
     *
     * conj(z) = | z.x
     *           | - z.y
     *
     * a * b   = | (a.x * b.x) - (a.y * b.y)
     *           | (a.x * b.y) + (a.y * b.x)
     *
     * a / b   = | ((a.x * b.x) + (a.y * b.y)) / d(b)
     *           | ((a.y * b.x) - (a.x * b.y)) / d(b)
     */

    /**
     * Translate this Euclidian point 
     * by the coordinates of the given Euclidian point.
     * 
     * @param t    the translation coordinates
     */
    void translate(HTCoordE t) {
        // z = (z + t) / (1 + z * conj(t))

        // first the denominator
        double denX = (x * t.x) + (y * t.y) + 1;
        double denY = (y * t.x) - (x * t.y) ;    
        double dd   = (denX * denX) + (denY * denY);

        // and the numerator
        double numX = x + t.x;
        double numY = y + t.y;
 
        // then the division (bell)
        x = ((numX * denX) + (numY * denY)) / dd;
        y = ((numY * denX) - (numX * denY)) / dd;
    }

    /**
     * Translate the given Euclidian point
     * by the coordinates of the given translation vector,
     * and put the results in this point.
     *
     * @param s    the source point
     * @param t    the translation vector
     */
    void translate(HTCoordE s, HTCoordE t) {
        this.copy(s);
        this.translate(t);
    }


  /* --- Math --- */

    /**
     * Returns the hyperbolic tangent of x.
     * 
     * @param x    we want tanh of x
     */
    private double tanh(double x) {
        x = Math.exp(2 * x);
        x = (x - 1) / (x + 1);
        return x;
    }


  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        return "(" + x + " : " + y + ")E";
    }

}

