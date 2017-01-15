/*
 * HTFileNode.java
 *
 * www.bouthier.net
 * 2001
 */

package hypertree.demo;

import hypertree.HTNode;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * The HTFileNode implements an example of HTNode encapsulating a File.
 */
public class HTFileNode
    implements HTNode {

    private File      file     = null; // the File encapsulated
    private Hashtable children = null; // the children of this node


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param file    the File encapsulated in this node
     */
    public HTFileNode(File file) {
        this.file = file;
        children = new Hashtable();

        if (! isLeaf()) {
            String[] tabFichiers = file.list();
            for (int i = 0; i < tabFichiers.length; i++) {
                File fichier = new File(file.getPath() +
                                        File.separator + tabFichiers[i]);
                HTFileNode child = new HTFileNode(fichier);
                addChild(child);
            }

        }
    }


  /* --- Tree management --- */

    /**
     * Add child to the node.
     * 
     * @param child    the HTFileNode to add as a child
     */
    protected void addChild(HTFileNode child) {
        children.put(child.getName(), child);
    }


  /* --- HTNode --- */

    /**
     * Returns the children of this node in an Enumeration.
     * If this node is a file, return a empty Enumeration.
     * Else, return an Enumeration full with HTFileNode.
     *
     * @return    an Enumeration containing childs of this node
     */
    public Enumeration children() {
        return children.elements();
    }

    /**
     * Returns true if this node is not a directory.
     *
     * @return    <CODE>false</CODE> if this node is a directory;
     *            <CODE>true</CODE> otherwise
     */
    public boolean isLeaf() {
        //boolean hidden = file.is();
        boolean directory = file.isDirectory();

        return ( !directory );
    }
    
    /**
     * Returns the name of the file.
     *
     * @return    the name of the file
     */
    public String getName() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return file.getName();
        }
    }
}

