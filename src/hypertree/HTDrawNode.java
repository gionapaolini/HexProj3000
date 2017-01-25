/*
 * HTDrawNode.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;

import BotAlgorithms.MCTS.NodeTree;
import BotAlgorithms.MCTS_2.NodeTree_2;

import java.awt.*;


/**
 * The HTDrawNode class contains the drawing coordinates of a HTModelNode 
 * for the HTView. 
 * It implements the Composite design pattern.
 */
class HTDrawNode {


    protected HTDraw model = null; // drawing model
    protected HTModelNode node  = null; // encapsulated HTModelNode

    private HTCoordE ze    = null; // current euclidian coordinates
    private HTCoordE zr    = null; // current euclidian radius coordinates

    private HTCoordE oldZe = null; // old euclidian coordinates
    private HTCoordE oldZr = null; // old euclidian radius coord

    private HTCoordS zs    = null; // current screen coordinates

  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param node     the encapsulated HTModelNode
     * @param model    the drawing model
     */
    HTDrawNode(HTModelNode node, HTDraw model) {
        this.node = node;
        this.model = model;



        ze = new HTCoordE(node.getCoordinates());
        zr = new HTCoordE(node.getRCoord());

        oldZe = new HTCoordE(ze);
        oldZr = new HTCoordE(zr);

        zs = new HTCoordS();
    }


  /* --- Name --- */

    /**
     * Returns the name of this node.
     *
     * @return    the name of this node
     */
    String getName() {
        return node.getName();
    }

  
  /* --- Screen Coordinates --- */

    /**
     * Refresh the screen coordinates of this node.
     *
     * @param sOrigin   the origin of the screen plane
     * @param sMax      the (xMax, yMax) point in the screen plane
     */
    void refreshScreenCoordinates(HTCoordS sOrigin, HTCoordS sMax) {
        zs.projectionEtoS(ze, sOrigin, sMax);
    } 

    /**
     * Returns the screen coordinates of this node.
     *
     * @return    the screen coordinates of this node
     */
    HTCoordS getScreenCoordinates() {
        return new HTCoordS(zs);
    }


  /* --- Drawing --- */

    /**
     * Draws the branches from this node to 
     * its children.
     * Overidden by HTDrawNodeComposite
     *
     * @param g    the graphic context
     */
    void drawBranches(Graphics g) {}

    /**
     * Draws this node.
     *
     * @param g    the graphic context
     */
    void drawNodes(Graphics g) {
        double dist = ((zr.x - ze.x) * (zr.x - ze.x)) +
                      ((zr.y - ze.y) * (zr.y - ze.y));
        double coeff = Math.sqrt(dist) / model.getRadius();
        model.drawNode(zs, coeff, g,(NodeTree_2) node.getNode());
    }


  /* --- Translation --- */

    /**
     * Translates this node by the given vector.
     *
     * @param t    the translation vector
     */
    void translate(HTCoordE t) {
        ze.translate(oldZe, t);
        zr.translate(oldZr, t);
    }

    /**
     * Ends the translation.
     */
    void endTranslation() {
        oldZe.copy(ze);
        oldZr.copy(zr);
    }


  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = getName() + 
                        "\n\t" + ze + 
                        "\n\t" + zs + 
                        "\n\t" + zr;
        return result;
    }

}

