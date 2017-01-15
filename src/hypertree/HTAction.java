/*
 * HTAction.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


/**
 * The HTAction class manage the action on the hypertree :
 * drag of a node...
 */
class HTAction
    extends MouseAdapter
    implements MouseMotionListener {

    private HTDraw   model      = null; // the drawing model
    private HTCoordE startPoint = null; // starting point of dragging
    private HTCoordE endPoint   = null; // ending point of dragging
    private HTCoordE curTrans   = null; // current translation vector


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param model    the drawing model
     */
    HTAction(HTDraw model) {
        this.model = model;
        startPoint = new HTCoordE();
        endPoint = new HTCoordE();
        curTrans = new HTCoordE();
    }


  /* --- MouseAdapter --- */

    /**
     * Called when a user pressed the mouse button
     * on the hyperbolic tree.
     * Used to get the starting point of the drag.
     *
     * @param e    the MouseEvent generated when clicking
     */
    public void mousePressed(MouseEvent e) {
        startPoint.projectionStoE(e.getX(), e.getY(), model);
    }

    /**
     * Called when a user release the mouse button
     * on the hyperbolic tree.
     * Used to signal the end of the translation.
     *
     * @param e    not used here
     */
    public void mouseReleased(MouseEvent e) {
        model.endTranslation();
    }

  /* --- MouseMotionListener --- */

    /**
     * Called when a used drag the mouse on the hyperbolic tree.
     * Used to translate the hypertree, thus moving the focus.
     *
     * @param e    the MouseEvent generated when draging
     */
    public void mouseDragged(MouseEvent e) {
        if (startPoint.isValid()) {
            endPoint.projectionStoE(e.getX(), e.getY(), model);
            if (endPoint.isValid()) {
                curTrans.sub(endPoint, startPoint);
                model.translate(curTrans);
            }
        }
    }

    /**
     * Called when the mouse mouve into the hyperbolic tree.
     * Not used here.
     *
     * @param e    the MouseEvent generated when mouving
     */
    public void mouseMoved(MouseEvent e) {}

}

