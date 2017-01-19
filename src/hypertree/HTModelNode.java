/*
 * HTModelNode.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;


/**
 * The HTModelNode class implements encapsulation of a HTNode
 * for the model. 
 * It keeps the original euclidian coordinates of the node.
 * It implements the Composite design pattern.
 *

 */
class HTModelNode {

    private HTModel model  = null; // tree model

    public HTNode getNode() {
        return node;
    }

    private HTNode node   = null; // encapsulated HTNode
    private HTModelNodeComposite parent = null; // parent node

    private   String               name   = null; // name of the node

    private HTCoordE z      = null; // Euclidian coordinates
    private HTCoordE zr     = null; // to compute radius reduction

    protected double               weight =  1.0;  // part of space taken by this node


  /* --- Constructor --- */

    /**
     * Constructor for root node.
     *
     * @param node     the encapsulated HTNode
     * @param model    the tree model using this HTModelNode
     */
    HTModelNode(HTNode node, HTModel model) {
        this(node, null, model);
    }

    /**
     * Constructor.
     *
     * @param node      the encapsulated HTNode
     * @param parent    the parent node
     * @param model     the tree model using this HTModelNode
     */
    HTModelNode(HTNode node, HTModelNodeComposite parent, HTModel model) {
        this.node = node;
        this.parent = parent;
        this.model = model;
      
        name = node.getName();
        z = new HTCoordE();
        zr = new HTCoordE();
    }


  /* --- Name --- */

    /**
     * Returns the name of this node.
     *
     * @return    the name of this node
     */
    String getName() {
        return name;
    }


  /* --- Weight Managment --- */

    /**
     * Returns the weight of this node.
     *
     * @return    the weight of this node
     */
    double getWeight() {
        return weight;
    }


  /* --- Tree management --- */

    /**
     * Returns the parent of this node.
     *
     * @return    the parent of this node
     */
    HTModelNodeComposite getParent() {
        return parent;
    }

    /**
     * Returns <CODE>true</CODE> if this node
     * is not an instance of HTModelNodeComposite.
     *
     * @return    <CODE>true</CODE>
     */
    boolean isLeaf() {
        return true;
    }


  /* --- Coordinates --- */

    /**
     * Returns the coordinates of this node.
     * Thoses are the original hyperbolic coordinates, 
     * without any translations.
     * WARNING : this is NOT a copy but the true object
     * (for performance).
     *
     * @return    the original hyperbolic coordinates
     */
    HTCoordE getCoordinates() {
        return z;
    }

    /**
     * Returns the coordinate of the point
     * on the radius of the node.
     * WARNING : this is NOT a copy but the true object
     * (for performance).
     *
     * @return    the radius point
     */
    HTCoordE getRCoord() {
        return zr;
    }


  /* --- Hyperbolic layout --- */

    /**
     * Layouts the nodes in the hyperbolic space.
     */
    void layoutHyperbolicTree() {
        this.layout(0.0, 2 * Math.PI);
    }

    /**
     * Layout this node in the hyperbolic space.
     * First set the point at the right distance,
     * then translate by father's coordinates.
     *
     * @param angle    the angle from the x axis (bold as love)
     * @param width    the angular width to divide
     */
    void layout(double angle, double width) {
        double lenght = model.getLenght();
        double radius = model.getRadius();
 
        if (parent == null) {
            zr.x = radius;
            zr.y = 0.0;
            zr.projectionHtoE();
            return;
        }

        // We first start as if the parent was the origin.
        // We still are in the hyperbolic space.
        z.x = lenght * Math.cos(angle);
        z.y = lenght * Math.sin(angle);

        // go to the Euclidian space
        z.projectionHtoE();

        // translate by parent's coordinates
        z.translate(parent.getCoordinates());
        
        // do the same for a point at z + model.radius
        // to have the coefficient of reduction for the radius
        zr.x = (lenght + radius) * Math.cos(angle);
        zr.y = (lenght + radius) * Math.sin(angle); 
        zr.projectionHtoE();
        zr.translate(parent.getCoordinates());
    } 


  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = name +
                        "\n\t" + z +
                        "\n\t" + zr +
                        "\n\tWeight = " + weight; 
        return result;
    }

}

