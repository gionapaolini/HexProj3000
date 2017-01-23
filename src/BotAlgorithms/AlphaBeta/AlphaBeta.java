package BotAlgorithms.AlphaBeta;

import BotAlgorithms.MCTS_2.NodeTree_2;
import BotAlgorithms.Strategy;
import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Match;
import GameLogic.MaxFlow;
import GameLogic.Move;

import java.util.List;

/**
 * Created by giogio on 1/22/17.
 */
public class AlphaBeta implements Strategy{

    Board realState;
    StatusCell ally, enemy;
    NodeTree root, lastMove;
    int maxTime, iterativeValue;
    int countEval, countPrune;
    double startTime;



    public AlphaBeta(Board realBoard, StatusCell color, int maxtTime, int depthLevel){
        this.realState = realBoard;
        this.ally = color;
        if(color==StatusCell.Blue)
            enemy = StatusCell.Red;
        else
            enemy = StatusCell.Blue;
        this.maxTime = maxtTime;
        lastMove = null;
    }

    public Move start(){
        iterativeValue = 1;
        setNewRoot();
        countEval = 0;
        countPrune =0;
        startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime<maxTime) {
            expand(root);
            iterativeValue++;
        }



        System.out.println(" ");
        printTree(root);
        System.out.println("EVALCOUNT: "+countEval);
        System.out.println("Pruning: "+countPrune);

        return getBestMove().getMove();


    }

    public void setNewRoot(){
        if(lastMove!=null) {
            Board copy = lastMove.getState().getCopy();
            for (NodeTree child : lastMove.getChildren()) {
                Move move = child.getMove();
                copy.putStone(move.getX(), move.getY(), enemy);
                if (copy.isEqual(realState)) {
                    root = child;
                    root.setParent(null);
                    return;
                }
                copy.setEmpty(move.getX(), move.getY());
            }
            lastMove = null;
            setNewRoot();
        }else {
            root = new NodeTree(null,null);
            root.setColor(enemy);
            root.setState(realState.getCopy());
        }
    }

    public NodeTree getBestMove(){
        int bestValue = -9999999;
        NodeTree bestNode = null;

        for(NodeTree child: root.getChildren()){
            if(child.getValue()>bestValue){
                bestValue = child.getValue();
                bestNode = child;
            }
        }

        lastMove = bestNode;
        return bestNode;
    }

    public void expand(NodeTree node){
        if(System.currentTimeMillis()-startTime>maxTime) {
            return;
        }
        if(node.getDepth()==iterativeValue-1){
            for(Move move: node.getState().getFreeMoves()){
                if(prune(node)) {
                    countPrune++;
                    break;
                }
                expand(new NodeTree(node,move));
            }
        }
        if(node.getDepth()==iterativeValue){
            countEval ++;
            evaluation(node);
            setNewValue(node.getParent());
        }
        if(node.getDepth()<iterativeValue-1){
            for(NodeTree child: node.getChildren()){
                expand(child);
            }
        }
    }


    public void printTree(NodeTree root){
        System.out.println("Root: "+root.getValue());
        printChild(root);

    }
    public void printChild(NodeTree leaf){
        for(NodeTree nodeTree: leaf.getChildren()) {
            for (int i = 0; i < nodeTree.getDepth(); i++) {
                System.out.print("-- ");
            }
            System.out.println("Value: "+nodeTree.getValue());
            if(nodeTree.getChildren().size()>0){
                printChild(nodeTree);
            }
        }
    }
    private boolean prune(NodeTree node){
        if(node.getDepth()==0)
            return false;
        if(node.getColor()==ally){
            if(node.getValue()>node.getParent().getValue())
                return true;
        }else {
            if(node.getValue()<node.getParent().getValue())
                return true;
        }
        return false;
    }
    private void evaluation(NodeTree node){
        int[] values = MaxFlow.flow(node.getState());
        if(ally == StatusCell.Blue){
            node.setValue(values[0]);
        }else {
            node.setValue(values[1]);
        }
    }

    private void setNewValue(NodeTree parent){
        int bestValue;
        if(parent.getColor()==ally){
            bestValue=9999;
            for(NodeTree child: parent.getChildren()){
                if(child.getValue()<bestValue)
                    bestValue = child.getValue();
            }
        }else {
            bestValue=-9999;
            for(NodeTree child: parent.getChildren()){
                if(child.getValue()>bestValue)
                    bestValue = child.getValue();
            }
        }
        parent.setValue(bestValue);
        if(parent.getParent()!=null)
            setNewValue(parent.getParent());
    }


    @Override
    public Move getMove() {
        return start();
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
