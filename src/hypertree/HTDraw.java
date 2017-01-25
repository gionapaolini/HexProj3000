/*
 * HTDraw.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;

import BotAlgorithms.MCTS.NodeTree;
import BotAlgorithms.MCTS_2.NodeTree_2;

import java.awt.*;


/**
 * The HTDraw class implements the drawing model for the HTView.
 */
class HTDraw {

    private HTModel model    = null; // the tree model
    private HTView view     = null; // the view using this drawing model
    private HTDrawNode drawRoot = null; // the root of the drawing tree 

    private HTCoordS   sOrigin  = null; // origin of the screen plane
    private HTCoordS   sMax     = null; // max point in the screen plane 
   

  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param model    the tree model to draw 
     * @param view     the view using this drawing model
     */
    HTDraw(HTModel model, HTView view) {
        this.view = view;
        this.model = model;
        HTModelNode root = model.getRoot();
        sOrigin = new HTCoordS();
        sMax = new HTCoordS();

        if (root.isLeaf()) {
            drawRoot = new HTDrawNode(root, this);
        } else {
            drawRoot = new HTDrawNodeComposite((HTModelNodeComposite) root, this);
        }
    }


  /* --- Screen coordinates --- */

    /**
     * Refresh the screen coordinates of the drawing tree.
     */
    void refreshScreenCoordinates() {
        Insets insets = view.getInsets();
        sMax.x = (view.getWidth() - insets.left - insets.right) / 2;
        sMax.y = (view.getHeight() - insets.top - insets.bottom) / 2;
        sOrigin.x = sMax.x + insets.left;
        sOrigin.y = sMax.y + insets.top;
        drawRoot.refreshScreenCoordinates(sOrigin, sMax);
    }

    /**
     * Returns the origin of the screen plane.
     * WARNING : this is not a copy but the original object.
     *
     * @return    the origin of the screen plane
     */
    HTCoordS getSOrigin() {
        return sOrigin;
    }

    /**
     * Return the point representing the up right corner
     * of the screen plane, thus giving x and y maxima.
     * WARNING : this is not a copy but the original object.
     *
     * @return    the max point
     */
    HTCoordS getSMax() {
        return sMax;
    }


  /* --- Drawing --- */

    /**
     * Draws the branches of the hyperbolic tree.
     *
     * @param g    the graphic context
     */
    void drawBranches(Graphics g) {
        drawRoot.drawBranches(g);
    }

    /**
     * Draws the nodes of the hyperbolic tree.
     *
     * @param g    the graphic context
     */
    void drawNodes(Graphics g) {
        drawRoot.drawNodes(g);
    }

    /**
     * Draws a branch between 2 screen points.
     *  @param zs1    the first point's screen coordinates
     * @param zs2    the second point's screen coordinates
     * @param g      the graphic context
     * @param node
     * @param aChildnode
     */
    void drawBranch(HTCoordS zs1, HTCoordS zs2, Graphics g, NodeTree_2 node, NodeTree_2 aChildnode) {
        // TODO a ameliorer
        g.setColor(Color.black);
        g.drawLine(zs1.x, zs1.y, zs2.x, zs2.y);
    }
    
    /**
     * Draws a node.
     *  @param zs    the coordinates of the center of the node
     * @param coeff the radius reduction coefficient
     * @param g     the graphic context
     * @param datenKnoten
     */
    void drawNode(HTCoordS zs, double coeff, Graphics g, NodeTree_2 datenKnoten) {
        // TODO a ameliorer        
        double minMax = Math.min(sMax.x, sMax.y);
        int radius = (int) Math.round(model.getRadius() * minMax * coeff);

        g.setColor(Color.red);
        g.fillOval(zs.x - radius, zs.y - radius, 2 * radius, 2 * radius);
        g.setColor(Color.black);
        g.drawOval(zs.x - radius, zs.y - radius, 2 * radius, 2 * radius);

        g.drawString(datenKnoten.getName(),zs.x - radius, zs.y - radius);
    }


  /* --- Radius --- */

    /**
     * Returns the radius of a node in the hyperbolic space.
     *
     * @return    the radius
     */
    double getRadius() {
        return model.getRadius();
    }



  /* --- Translation --- */

    /**
     * Translates the hyperbolic tree by the given vector.
     *
     * @param t    the translation vector
     */
    void translate(HTCoordE t) {
        drawRoot.translate(t);
        view.repaint();
    } 

    /**
     * Signal that the translation ended.
     */
    void endTranslation() {
        drawRoot.endTranslation();
    }

  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        return "HTDraw";
    }

}

