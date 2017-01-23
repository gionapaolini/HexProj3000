package BotAlgorithms.AlphaBeta;

import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 1/22/17.
 */
public class NodeTree {

    private NodeTree parent;
    private ArrayList<NodeTree> children;
    private Move move;
    private int value;
    private StatusCell color;
    private Board state;
    private boolean winningMove, losingMove, deadCell;


    public NodeTree(NodeTree parent, Move move){
        this.parent = parent;
        this.move = move;
        children = new ArrayList<>();
        if(parent!=null){
            parent.addChildren(this);
            if(parent.getColor()==StatusCell.Blue)
                color = StatusCell.Red;
            else
                color = StatusCell.Blue;

            state = parent.getState().getCopy();
            state.putStone(move.getX(),move.getY(),parent.getColor());

            if(state.hasWon(parent.getColor()))
                losingMove = true;
            else
                losingMove = false;

            state.putStone(move.getX(),move.getY(),color);

            if(state.hasWon(color))
                winningMove = true;
            else
                winningMove = false;

        }
    }

    public int getDepth(){
        int count = 0;
        NodeTree currentParent = parent;
        while (currentParent!=null){
            count++;
            currentParent = currentParent.getParent();
        }
        return count;
    }

    public Board getState() {
        return state;
    }

    public boolean isWinningMove() {
        return winningMove;
    }

    public boolean isLosingMove() {
        return losingMove;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setParent(NodeTree parent) {
        this.parent = parent;
    }

    public void setChildren(ArrayList<NodeTree> children) {
        this.children = children;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void setColor(StatusCell color) {
        this.color = color;
    }

    public NodeTree getParent() {
        return parent;
    }

    public ArrayList<NodeTree> getChildren() {
        return children;
    }

    public Move getMove() {
        return move;
    }

    public int getValue() {
        return value;
    }

    public StatusCell getColor() {
        return color;
    }

    public void setState(Board state) {
        this.state = state;
    }

    public void setWinningMove(boolean winningMove) {
        this.winningMove = winningMove;
    }

    public void setLosingMove(boolean losingMove) {
        this.losingMove = losingMove;
    }

    public void setDeadCell(boolean deadCell) {
        this.deadCell = deadCell;
    }

    public void addChildren(NodeTree child){
        children.add(child);
    }
}
