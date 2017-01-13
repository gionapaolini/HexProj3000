package BotAlgorithms.MCTS;

import EnumVariables.StatusCell;
import GameLogic.Move;

import java.util.ArrayList;

/**
 * Created by giogio on 1/13/17.
 */
public class NodeTree {
    Move move;
    StatusCell color;
    NodeTree parent;
    ArrayList<NodeTree> childrens;
    int totalGames, totalWins;
    float value;

    public NodeTree(NodeTree parent){
        this.parent = parent;
        childrens = new ArrayList<>();
        if(parent!=null){
            parent.addChild(this);
            if(parent.getColor()==StatusCell.Blue)
                color=StatusCell.Red;
            else
                color=StatusCell.Blue;
        }

    }

    public StatusCell getColor(){
        return color;
    }

    public void incrementGame(){
        totalGames++;
    }

    public void incrementWins(){
        totalWins++;
    }

    public void setValue(float value){
        this.value=value;
    }

    public void addChild(NodeTree child){
        childrens.add(child);
    }
}
