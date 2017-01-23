package BotAlgorithms.AlphaBeta;

import BotAlgorithms.MCTS_2.NodeTree_2;
import BotAlgorithms.Strategy;
import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Match;
import GameLogic.Move;

import java.util.List;

/**
 * Created by giogio on 1/22/17.
 */
public class AlphaBeta implements Strategy{

    Board realState;
    StatusCell ally, enemy;
    NodeTree root;
    int maxTime, iterativeValue;

    public AlphaBeta(Board realBoard, StatusCell color, int maxtTime, int depthLevel){
        this.realState = realBoard;
        this.ally = color;
        if(color==StatusCell.Blue)
            enemy = StatusCell.Red;
        else
            enemy = StatusCell.Blue;
        this.maxTime = maxtTime;
    }

    public void start(){
        iterativeValue = 1;
        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime<maxTime){
            expand(root);
            iterativeValue++;
        }

    }

    public void expand(NodeTree node){
        if(node.getDepth()==iterativeValue-1){
            for(Move move: node.getState().getFreeMoves()){
                if(prune(node))
                    break;
                expand(new NodeTree(node,move));
            }
        }
        if(node.getDepth()==iterativeValue){
            evaluation(node);
        }
        if(node.getDepth()<iterativeValue-1){
            for(NodeTree child: node.getChildren()){
                expand(child);
            }
        }
    }


    private boolean prune(NodeTree node){
        if(node.getDepth()==0)
            return false;
        if(node.getColor()==ally){
            if(node.getValue()<node.getParent().getValue())
                return true;
        }else {
            if(node.getValue()>node.getParent().getValue())
                return true;
        }
        return false;
    }
    private void evaluation(NodeTree node){

    }


    @Override
    public Move getMove() {
        return null;
    }

    @Override
    public NodeTree_2 getRootTreeMcts() {
        return null;
    }

    @Override
    public void resetTree() {

    }

    @Override
    public void updateBoard(Board board) {

    }
}
