package BotAlgorithms.MCTS_2;

import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Cell;
import GameLogic.Move;

import java.util.ArrayList;

/**
 * Created by giogio on 1/21/17.
 */
public class NodeTree {
    private Board state;
    private Move move;
    private NodeTree parent;
    private ArrayList<NodeTree> children;
    private StatusCell color;
    private int wins,games;
    private boolean winningMove, losingMove, deadCell;

    public NodeTree(NodeTree parent){
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

    public void addChildren(NodeTree child){
        children.add(child);
    }

    public Board getState() {
        return state;
    }

    public Move getMove() {
        return move;
    }

    public NodeTree getParent() {
        return parent;
    }

    public ArrayList<NodeTree> getChildren() {
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

    public void setParent(NodeTree parent) {
        this.parent = parent;
    }
}
