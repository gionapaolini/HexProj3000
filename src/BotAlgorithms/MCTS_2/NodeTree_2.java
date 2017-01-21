package BotAlgorithms.MCTS_2;

import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Cell;
import GameLogic.Move;
import hypertree.HTNode;

import java.util.*;

/**
 * Created by giogio on 1/21/17.
 */
public class NodeTree_2 implements HTNode {
    private Board state;
    private Move move;
    private NodeTree_2 parent;
    private ArrayList<NodeTree_2> children;
    private StatusCell color;
    private int wins,games;
    private boolean winningMove, losingMove, deadCell;

    public NodeTree_2(NodeTree_2 parent){
        if(parent!=null){
            this.parent = parent;
            parent.addChildren(this);
            if(parent.getColor() == StatusCell.Blue)
                color = StatusCell.Red;
            else
                color = StatusCell.Blue;
        }
        children = new ArrayList<>();

    }

    public void setMove(Move move){

        this.move = move;
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


        checkDeadCell();
    }

    public void checkDeadCell(){
        Cell[] neighbours = state.getGrid()[move.getY()][move.getX()].getNeighbors();
        int count =0;
        for(Cell cell: neighbours){
            if(cell!=null && cell.getStatus()!=StatusCell.Empty)
                count++;
            if(count==4)
                break;
        }
        if(count<4){
            deadCell = false;
            return;
        }
        count = 0;
        int start =0;
        while(start<6){
            if(neighbours[(start+count)%6] !=null && neighbours[(start+count)%6].getStatus()==color) {
                count++;
                if(count==4) {
                    deadCell = true;
                    return;
                }
            }else {
                start+=count+1;
                count = 0;

            }
        }
        count = 0;
        start =0;

        while(start<6){
            if(neighbours[(start+count)%6] !=null && neighbours[(start+count)%6].getStatus()==parent.getColor()) {
                count++;
                if(count==4) {
                    deadCell = true;
                    return;
                }
            }else {
                start+=count+1;
                count = 0;

            }
        }
        deadCell=false;
    }

    public boolean isDeadCell() {

        return deadCell;
    }

    public void incrementWin(int wins){
        this.wins+=wins;
        if(parent!=null)
            parent.incrementWin(wins);
    }
    public void incrementGame(){
        this.games+=1;
        if(parent!=null)
            parent.incrementGame();
    }

    public boolean isLosingMove(){
        return losingMove;
    }

    public void setState(Board state) {
        this.state = state;
    }

    public void setColor(StatusCell color) {
        this.color = color;
    }

    public void addChildren(NodeTree_2 child){
      //  System.out.println(this);
        children.add(child);
    }

    public Board getState() {
        return state;
    }

    public Move getMove() {
        return move;
    }

    public NodeTree_2 getParent() {
        return parent;
    }

    public ArrayList<NodeTree_2> getChildren() {
        return children;
    }

    public StatusCell getColor() {
        return color;
    }

    public int getWins() {
        return wins;
    }

    public int getGames() {
        return games;
    }

    public boolean isWinningMove() {
        return winningMove;
    }

    public void setParent(NodeTree_2 parent) {
        this.parent = parent;
    }

    public String toString(){

        String s =  color.toString() + " " ;
        if (move != null) s+= move.toString();
        s += " " + "Depth: " + getDepth() + " Height: " + getHeight() + " Value: " + wins + " Games: " + games + " Parent: ";
        if (parent != null) if (parent.move!=null) s += parent.move.toString();
        return s;
    }

    public int getDepth(){
        NodeTree_2 dad = parent;
        int count = 0;
        while (dad!=null) {
            count++;
            dad = dad.parent;
        }
        return count;
    }

    public int getHeight(){
        if (isLeaf()) return 0;
        int maxHeight = -1;
        for (int i = 0; i < children.size(); i++) {
            int childHeight = children.get(i).getHeight();
            if (childHeight>maxHeight) maxHeight = childHeight;
        }

        maxHeight+=1;
        return maxHeight;
    }


    @Override
    public Enumeration children() {
        Enumeration c = Collections.enumeration(children);
        return c;
    }

    @Override
    public boolean isLeaf() {
        if (children.size()==0)return true;
        return false;
    }

    @Override
    public String getName() {
        if (move == null) return "root";
        return move.toString();
    }

    public  ArrayList<NodeTree_2> breadthFirst(){
        ArrayList<NodeTree_2> bfsearch = new ArrayList<>(games);
        Queue<NodeTree_2> quey = new LinkedList<NodeTree_2>();

        quey.add(this);
        while (!quey.isEmpty()){
            NodeTree_2 n = quey.remove();
            bfsearch.add(n);
            for (int i = 0; i < n.children.size(); i++) {
                quey.add(n.children.get(i));
            }

        }

        return bfsearch;

    }

}
