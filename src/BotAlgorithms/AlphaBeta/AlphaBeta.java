package BotAlgorithms.AlphaBeta;

import BotAlgorithms.MCTS_2.NodeTree_2;
import BotAlgorithms.Strategy;
import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.MaxFlow;
import GameLogic.Move;

import java.util.ArrayList;

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
    boolean flowEvaluation = false;


    public AlphaBeta(Board realBoard, StatusCell color, int maxtTime, int depthLevel){
        this.realState = realBoard;
        this.ally = color;
        if(color==StatusCell.Blue)
            enemy = StatusCell.Red;
        else
            enemy = StatusCell.Blue;
        this.maxTime = maxtTime;
        lastMove = null;
        iterativeValue = depthLevel;
        flowEvaluation=false;
    }

    public Move start(){
        iterativeValue=1;
        setNewRoot();
        countEval = 0;
        countPrune =0;
        startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime<maxTime) {
            expand(root);
            iterativeValue++;
        }



        System.out.println(" ");
       // printTree(root);
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
        float bestValue = -9999999;
        NodeTree bestNode = null;

        for(NodeTree child: root.getChildren()){
            System.out.println(child.getValue());
            if(child.getValue()>bestValue){
                bestValue = child.getValue();
                bestNode = child;
            }
        }

        lastMove = bestNode;
        return bestNode;
    }

    public void newExpand(NodeTree node){
        if(System.currentTimeMillis()-startTime>maxTime) {
            return;
        }
        if(node.getDepth()<iterativeValue){
            if(!prune(node)) {
                ArrayList<Move> freeMoves = node.getState().getFreeMoves();
                while (freeMoves.size() > 0 && System.currentTimeMillis()-startTime<maxTime) {
                    Move move = freeMoves.remove((int) (Math.random() * freeMoves.size()));
                    newExpand(new NodeTree(node, move));
                }
                if(node.getParent()!=null)
                    backTrack(node);
            }else {
                countPrune++;
            }
        }
        if(node.getDepth()==iterativeValue){
            node.setValue(evaluationOld(node));
            backTrack(node);
            countEval++;
        }
    }

    public void backTrack(NodeTree node){
        if(node.getParent().getValue()==0){
            node.getParent().setValue(node.getValue());
            return;
        }
        if(node.getColor()==ally){
            if(node.getParent().getValue()<node.getValue())
                node.getParent().setValue(node.getValue());
        }else {
            if(node.getParent().getValue()>node.getValue())
                node.getParent().setValue(node.getValue());
        }

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
            if (flowEvaluation) evaluation(node);
            else node.setValue(evaluationOld(node));
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
            if(node.getValue()<node.getParent().getValue())
                return true;
        }else {
            if(node.getValue()>node.getParent().getValue())
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

    public static float evaluationOld(NodeTree node){
        float ratio = 0;
        if(node.getColor() == StatusCell.Blue) {
            int[] horizontal = new int[node.getState().getGrid().length-1];
            //Getting #horizontal
            for (int j = 0; j < node.getState().getGrid().length-1 ; j++) {
                for (int jj = j; jj < j + 2; jj++) {
                    for (int i = 0; i < node.getState().getGrid().length; i++) {
                        if (node.getState().getGrid()[jj][i].getStatus() == StatusCell.Blue) {
                            horizontal[j]++;
                        }
                    }
                }
            }
            int[] vertical = new int[node.getState().getGrid().length-1];
            //Getting #vertical
            for (int j = 0; j < node.getState().getGrid().length-1 ; j++) {
                for (int jj = j; jj < j + 2; jj++) {
                    for (int i = 0; i < node.getState().getGrid().length; i++) {
                        if (node.getState().getGrid()[i][jj].getStatus() == StatusCell.Blue) {
                            vertical[j]++;
                        }
                    }
                }
            }
            //System.out.println("DRatio Hor " + (getMax(horizontal)) + " Ver " + (getMax(vertical)));
            ratio = (float)(getMax(horizontal)) / (float)(getMax(vertical));
        }else {
            int[] horizontal = new int[node.getState().getGrid().length-1];
            //Getting #horizontal
            for (int j = 0; j < node.getState().getGrid().length-1 ; j++) {
                for (int jj = j; jj < j + 2; jj++) {
                    for (int i = 0; i < node.getState().getGrid().length; i++) {
                        if (node.getState().getGrid()[jj][i].getStatus() == StatusCell.Red) {
                            horizontal[j]++;
                        }
                    }
                }
            }
            int[] vertical = new int[node.getState().getGrid().length-1];
            //Getting #vertical
            for (int j = 0; j < node.getState().getGrid().length-1 ; j++) {
                for (int jj = j; jj < j + 2; jj++) {
                    for (int i = 0; i < node.getState().getGrid().length; i++) {
                        if (node.getState().getGrid()[i][jj].getStatus() == StatusCell.Red) {
                            vertical[j]++;
                        }
                    }
                }
            }
            //System.out.println("DRatio Hor " + (getMax(horizontal)) + " Ver " + (getMax(vertical)));
            ratio = (float)(getMax(vertical)) / (float)getMax(horizontal);
        }
        return ratio;
    }

    public static int getMax(int[] arr){
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    private void setNewValue(NodeTree parent){
        float bestValue;
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
