package BotAlgorithms.PathFinding;

import GameLogic.Cell;

/**
 * Created by giogio on 1/22/17.
 */
public class StarNode {
    int h, g, f;
    boolean visited;
    Cell node;
    StarNode explorer;

    public StarNode(StarNode explorer, Cell nodeCell){
        this.explorer=explorer;
        node = nodeCell;
    }


    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG() {
        if(explorer!=null)
            g = explorer.g+1;
        else
            g = 0;
    }

    public int getF() {
        return f;
    }

    public void setF() {
        f = g+h;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Cell getNode() {
        return node;
    }

    public void setNode(Cell node) {
        this.node = node;
    }

    public StarNode getExplorer() {
        return explorer;
    }

    public void setExplorer(StarNode explorer) {
        this.explorer = explorer;
    }
}
