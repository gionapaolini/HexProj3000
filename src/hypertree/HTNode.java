/*
 * HTNode.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;

import java.util.Enumeration;


/**
 * The HTNode interface should be implemented by 
 * object that are node of the tree that want to be 
 * displayed in the TreeMap.
 * <P>
 * If you have already a tree structure, just implements
 * this interface in node of the tree.
 */
public interface HTNode {

    /**
     * Returns the children of this node
     * in an Enumeration.
     * If this object does not have children,
     * it should return an empty Enumeration,
     * not <CODE>null</CODE>.
     * All objects contained in the Enumeration
     * should implements HTNode.
     *
     * @return    an Enumeration containing childs of this node
     */
    public Enumeration children();

    /**
     * Checks if this node is a leaf or not.
     * A node could have no children and still not
     * be a leaf.
     *
     * @return    <CODE>true</CODE> if this node is a leaf;
     *            <CODE>false</CODE> otherwise
     */
    public boolean isLeaf();
    
    /**
     * Returns the name of this node.
     * Used to display a label in the hyperbolic tree.
     *
     * @return    the name of this node
     */
    public String getName();

}

