/*
 * HTView.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;

import javax.swing.*;
import java.awt.*;


/**
 * The HTView class implements a view of the HyperTree.
 */
public class HTView
    extends JPanel {

    private HTModel    model  = null; // the tree model represented
    private HTDraw     draw   = null; // the drawing model
    private HTAction action = null; // action manager


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param model    the tree model to view
     */
    public HTView(HTModel model,Dimension preveredSize) {
        super(new BorderLayout());
        setPreferredSize(preveredSize);
        setBackground(Color.white);

        this.model = model; 
        draw = new HTDraw(model, this);
        action = new HTAction(draw);
        this.addMouseListener(action);
        this.addMouseMotionListener(action);
    }


  /* --- Paint --- */

    /**
     * Paint the component.
     *
     * @param g    the graphic context
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw.refreshScreenCoordinates();
        draw.drawBranches(g);
        draw.drawNodes(g);
    }

}

