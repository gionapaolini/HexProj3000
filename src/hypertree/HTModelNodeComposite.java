/*
 * HTModelNodeComposite.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;

import java.util.Enumeration;
import java.util.Vector;


/**
 * The HTModelNodeComposite class implements the Composite design pattern
 * for HTModelNode.
 * It represents a HTModelNode which is not a leaf.
 */
class HTModelNodeComposite 
    extends HTModelNode {

    private Vector children     = null; // children of this node
    private double globalWeight = 0.0;  // sum of children weight


  /* --- Constructor --- */

    /**
     * Constructor for root node.
     *
     * @param node     the encapsulated HTNode
     * @param model    the tree model using this HTModelNode
     */
    HTModelNodeComposite(HTNode node, HTModel model) {
        this(node, null, model);
    }

    /**
     * Constructor.
     *
     * @param node      the encapsulated HTNode
     * @param parent    the parent node
     * @param model     the tree model using this HTModelNode
     */
    HTModelNodeComposite(HTNode node, HTModelNodeComposite parent, HTModel model) {
        super(node, parent, model);
        this.children = new Vector();

        HTNode childNode = null;
        HTModelNode child = null;
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            childNode = (HTNode) e.nextElement();
            if (childNode.isLeaf()) {
                child = new HTModelNode(childNode, this, model);
            } else {
                child = new HTModelNodeComposite(childNode, this, model);
            }
            addChild(child);
        }
        
        // here the down of the tree is built, so we can compute the weight
        computeWeight();
    }


  /* --- Weight Managment --- */

    /**
     * Compute the Weight of this node.
     * As the weight is computed with the log
     * of the sum of child's weight, we must have all children 
     * built before starting the computing.
     */
    private void computeWeight() {
        HTModelNode child = null;
         
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTModelNode) e.nextElement();
            globalWeight += child.getWeight();
        } 
        if (globalWeight != 0.0) {
            weight += Math.log(globalWeight);
        }
    }


  /* --- Tree management --- */

    /**
     * Returns the children of this node, 
     * in an Enumeration.
     *
     * @return    the children of this node
     */
    Enumeration children() {
        return children.elements();
    }

    /** 
     * Adds the HTModelNode as a children.
     *
     * @param child    the child
     */
    void addChild(HTModelNode child) {
        children.addElement(child);
    }

    /**
     * Returns <CODE>false</CODE> as this node
     * is an instance of HTModelNodeComposite.
     *
     * @return    <CODE>false</CODE>
     */
    boolean isLeaf() {
        return false;
    }


  /* --- Hyperbolic layout --- */

    /**
     * Layout this node and its children in the hyperbolic space.
     * Mainly, divide the width angle between children and
     * put the children at the right angle.
     */
   void layout(double angle, double width) {
        super.layout(angle, width);
       System.out.println("withd" + width);
       width = Math.PI * 1 /4;
       System.out.println("withd" + width);
        HTModelNode child = null;

        // Only the root node could have a width > PI
        if ((getParent() != null) && (width > Math.PI)) {
            width = Math.PI;
        }

        double startAngle = angle - (width / 2);

        // It may be interesting to sort children by weight instead
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTModelNode) e.nextElement();
            
            double percent = child.getWeight() / globalWeight;
            double childWidth = width * percent;
            double childAngle = startAngle + (childWidth / 2);
            child.layout(childAngle, childWidth);
            //child.layout(childAngle, 0);
            startAngle += childWidth;
        }
    }


  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = super.toString();
        HTModelNode child = null;
        result += "\n\tChildren :";
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTModelNode) e.nextElement();
            result += "\n\t-> " + child.getName();
        }
        return result;
    }

}

