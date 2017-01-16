package BotAlgorithms.MCTS;

import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Move;
import hypertree.HTNode;

import java.util.*;

/**
 * Created by giogio on 1/13/17.
 */
public class NodeTree implements HTNode {
    private Board state;
    private Move move;
    private StatusCell color;
    private NodeTree parent;
    private ArrayList<NodeTree> childrens;
    private int totalGames, totalWins;
    private boolean winningState;

    public NodeTree(StatusCell color, Board state){
        parent = null;
        this.color = color;
        this.state = state;
        childrens = new ArrayList<>();
    }

    public  ArrayList<NodeTree> breadthFirst(){
        ArrayList<NodeTree> bfsearch = new ArrayList<>(totalGames);
        Queue<NodeTree> quey = new LinkedList<NodeTree>();

        quey.add(this);
        while (!quey.isEmpty()){
            NodeTree n = quey.remove();
            bfsearch.add(n);
            for (int i = 0; i < n.childrens.size(); i++) {
                quey.add(n.childrens.get(i));
            }

        }

        return bfsearch;

    }


    public NodeTree(NodeTree parent){
        this.parent = parent;
        parent.addChildren(this);
        if(parent.getColor()==StatusCell.Blue)
            color=StatusCell.Red;
        else
            color=StatusCell.Blue;
        childrens = new ArrayList<>();
    }


    public void setMove(Move move){
        this.move = move;
        this.state = parent.getState().getCopy();
        state.putStone(move.getX(),move.getY(),color);
        checkWinningState();
    }

    public Board getState(){
        return state;
    }
    public Move getMove() {
        return move;
    }

    public NodeTree getParent() {
        return parent;
    }

    public ArrayList<NodeTree> getChildrens() {
        return childrens;
    }

    public void addChildren(NodeTree node){
        childrens.add(node);
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public StatusCell getColor(){
        return color;
    }

    public void incrementGame(){
        totalGames++;
        if(parent!=null)
            parent.incrementGame();
    }

    public void incrementWins(int wins){
        totalWins+=wins;
        if(parent!=null)
            parent.incrementWins(wins);
    }

    public boolean isWinningState(){
        return winningState;
    }

    public void checkWinningState(){
        if(state.hasWon(color))
            winningState = true;
    }

    public int getDepth(){
        NodeTree dad = parent;
        int count = 0;
        while (dad!=null) {
            count++;
            dad = dad.parent;
        }
        return count;
    }

    @Override
    public Enumeration children() {
        Enumeration c = Collections.enumeration(childrens);
        return c;
    }

    @Override
    public boolean isLeaf() {
        if (childrens.size()==0)return true;
        return false;
    }

    @Override
    public String getName() {
        if (move == null) return "root";
        return move.toString();
    }

    public void setParent(NodeTree parent){
        this.parent = parent;
    }

    public int getHeight(){
        if (isLeaf()) return 0;
        int maxHeight = -1;
        for (int i = 0; i < childrens.size(); i++) {
            int childHeight = childrens.get(i).getHeight();
            if (childHeight>maxHeight) maxHeight = childHeight;
        }

        maxHeight+=1;
        return maxHeight;


    }
}
