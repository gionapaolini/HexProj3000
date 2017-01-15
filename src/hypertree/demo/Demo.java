/*
 * Demo.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree.demo;

import hypertree.HTView;
import hypertree.HyperTree;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


/**
 * The Demo class implements a demo for HyperTree. It display an HyperTree
 * view of a demo tree.
 * The demo tree is build from a file tree, passed in parameter.
 * Demo could take an argument, the path from which 
 * start the representing of files.
 * If no arguments is given, the treemap start from the root.
 */
public class Demo {

    private static HTFileNode root      = null; // the root of the demo tree
    private static HyperTree  hypertree = null; // the hypertree builded


    /**
     * Display a demo HyperTree.
     */
    public static void main(String[] args) {
        String pathRoot = null;

        if (args.length > 0) {
            pathRoot = args[0];
        } else {
            pathRoot = "I:\\DateienBilderPersönlich";
        }

        File rootFile = new File(pathRoot);
        try {
            System.out.println("Starting the hyperbolic tree from " +
                                rootFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (! rootFile.exists()) {
            System.out.println("Can't start hypertree : " + rootFile.getName() +
                               " does not exist.");
            return;
        }


        root = new HTFileNode(rootFile);
        if (root == null) {
            System.err.println("Error : can't start hypertree from " +
                                rootFile.getAbsolutePath());
            return;
        }

        hypertree = new HyperTree(root);
        Dimension preveredSize = new Dimension(800,600);
        HTView view = hypertree.getView(preveredSize);

        JFrame viewFrame = new JFrame(root.getName());
        viewFrame.setContentPane(view);
        viewFrame.pack();
        viewFrame.setVisible(true);
    }

}

