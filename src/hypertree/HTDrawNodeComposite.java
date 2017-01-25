/*
 * HTDrawNodeComposite.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;

import BotAlgorithms.MCTS.NodeTree;
import BotAlgorithms.MCTS_2.NodeTree_2;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;


/**
 * The HTDrawNodeComposite class implements the Composite design pattern
 * for HTDrawNode.
 * It represents a HTDrawNode which is not a leaf.
 */
class HTDrawNodeComposite 
    extends HTDrawNode {

    private HTModelNodeComposite node     = null; // encapsulated HTModelNode
    private Vector               children = null; // children of this node


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param node     the encapsulated HTModelNode
     * @param model    the drawing model
     */
    HTDrawNodeComposite(HTModelNodeComposite node, HTDraw model) {
        super(node, model);
        this.node = node;
        this.children = new Vector();

        HTModelNode childNode = null;
        HTDrawNode child = null;
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            //todo here are the actual values to draw
            Object d = e.nextElement();
            childNode = (HTModelNode) d;
            if (childNode.isLeaf()) {
                child = new HTDrawNode(childNode, model);
            } else {
                child = new HTDrawNodeComposite((HTModelNodeComposite) childNode, model);
            }
            addChild(child);
        }
    }


  /* --- Children --- */

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
     * Adds the HTDrawNode as a children.
     *
     * @param child    the child
     */
    void addChild(HTDrawNode child) {
        children.addElement(child);
    }


  /* --- Screen Coordinates --- */

    /**
     * Refresh the screen coordinates of this node
     * and recurse on children.
     *
     * @param sOrigin   the origin of the screen plane
     * @param sMax      the (xMax, yMax) point in the screen plane
     */ 
    void refreshScreenCoordinates(HTCoordS sOrigin, HTCoordS sMax) {
        super.refreshScreenCoordinates(sOrigin, sMax);
        HTDrawNode child = null;

        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.refreshScreenCoordinates(sOrigin, sMax);
        }
    }


  /* --- Drawing --- */

    /**
     * Draws the branches from this node to 
     * its children.
     *
     * @param g    the graphic context
     */
    void drawBranches(Graphics g) {
        HTDrawNode child = null;

        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            model.drawBranch(this.getScreenCoordinates(), 
                             child.getScreenCoordinates(), g,(NodeTree_2) node.getNode(), (NodeTree_2) child.node.getNode());
            child.drawBranches(g); 
        }
    }

    /**
     * Draws this node.
     *
     * @param g    the graphic context
     */
    void drawNodes(Graphics g) {
        super.drawNodes(g);
        
        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            child.drawNodes(g);
        }
    }


  /* --- Translation --- */

    /**
     * Translates this node by the given vector.
     *
     * @param t    the translation vector
     */
    void translate(HTCoordE t) {
        super.translate(t);

        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
             child = (HTDrawNode) e.nextElement();
             child.translate(t);
        }
    }

    /**
     * Ends the translation.
     */
    void endTranslation() {
        super.endTranslation();

        HTDrawNode child = null;
        for (Enumeration e = children(); e.hasMoreElements(); ) {
             child = (HTDrawNode) e.nextElement();
             child.endTranslation();
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
        HTDrawNode child = null;
        result += "\n\tChildren :";
        for (Enumeration e = children(); e.hasMoreElements(); ) {
            child = (HTDrawNode) e.nextElement();
            result += "\n\t-> " + child.getName();
        }
        return result;
    }

}

