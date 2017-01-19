/*
 * HyperTree.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree;


import BotAlgorithms.MCTS.NodeTree;

import javax.swing.tree.TreeNode;
import java.awt.*;

/**
 * The HyperTree class implements an hyperbolic tree representation for data.
 * <P>
 * An HyperTree is build from hierarchical data, given as a tree
 * of HTNode. So, the first parameter to give to build an HyperTree
 * is the HTNode which is the root of the to-be-represented tree.
 * The tree to be displayed by the HyperTree should be build before the call
 * to HyperTree, that is the root node should return children
 * when the children() method is called.
 * <P>
 * You can get a HTView (herited from JView) containing the HyperTree by calling
 * getView().
 */
public class HyperTree {

    private HTModel model = null; // the model of the tree for the HyperTree


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param root    the root of the tree to be represented;
     *                could not be <CODE>null</CODE>
     */
    public HyperTree(NodeTree root) {
        model = new HTModel(root);
    }


  /* --- View --- */

    /**
     * Returns a view of the hypertree.
     *
     * @return              the desired view of the hypertree
     * @param preveredSize
     */
    public HTView getView(Dimension preveredSize) {
        return new HTView(model,preveredSize);
    }

}

