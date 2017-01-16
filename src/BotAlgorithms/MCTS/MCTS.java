package BotAlgorithms.MCTS;

import BotAlgorithms.Strategy;
import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Move;

import java.util.ArrayList;

/**
 * Created by giogio on 1/13/17.
 */
public class MCTS implements Strategy {

    int maxTime, depthLevel;
    Board initialBoard;
    StatusCell color;
    StatusCell enemy;
    NodeTree root;
    NodeTree lastNode;
    final double a = 0.85;

    public MCTS(Board board, StatusCell color, int maxTime, int depthLevel){
        this.maxTime = maxTime;
        this.depthLevel = depthLevel;
        this.color = color;
        if(color == StatusCell.Blue)
            enemy = StatusCell.Red;
        else
            enemy = StatusCell.Blue;
        this.initialBoard = board;
    }

    public Move start(){
        if(root ==null)
            root = new NodeTree(enemy,initialBoard);
        else
            setNewRoot();

        int count = 0;

        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis()-startTime<=maxTime){
            expand(selection(root));
            count++;
        }
        printTree(root);
        System.out.println(count);
        NodeTree node = selectBestMoveNode(root);
        lastNode = node;
        return node.getMove();
    }

    public void setNewRoot(){
        Board copy = lastNode.getState().getCopy();
        for(NodeTree nodeTree: lastNode.getChildrens()){
            copy.putStone(nodeTree.getMove().getX(),nodeTree.getMove().getY(), enemy);
            if(copy.isEqual(initialBoard)){
                root = nodeTree;
                return;
            }
            copy.setEmpty(nodeTree.getMove().getX(),nodeTree.getMove().getY());
        }
        root = new NodeTree(enemy,initialBoard);
    }

    private NodeTree selectBestMoveNode(NodeTree root){
        int nWins=root.getChildrens().get(0).getTotalWins();
        NodeTree bestMove = root.getChildrens().get(0);
        for(NodeTree nodeTree: root.getChildrens()){
            if(nodeTree.getTotalWins()>nWins){
                nWins = nodeTree.getTotalWins();
                bestMove = nodeTree;
            }
        }
        return bestMove;
    }
    public NodeTree selection(NodeTree node){

        if(node.getState().getFreeMoves().size()>0)
            return node;



        double score = -1000000;
        NodeTree result = node;


        for(NodeTree nodeChild: node.getChildrens()){
            double newScore = selectfn(nodeChild);
            if(newScore>score){
                score = newScore;
                result = nodeChild;
            }
        }
        return selection(result);
    }

    public void printTree(NodeTree root){

        printChild(root);
        System.out.println("Root: "+root.getTotalGames());

    }
    public void printChild(NodeTree leaf){
        for(NodeTree nodeTree: leaf.getChildrens()) {
            for (int i = 0; i < nodeTree.getDepth(); i++) {
                System.out.print("-- ");
            }
            System.out.println(" WINS/TOTAL: "+nodeTree.getTotalWins()+"/"+nodeTree.getTotalGames());
            if(nodeTree.getChildrens().size()>0){
                printChild(nodeTree);
            }
        }
    }

    public void expand(NodeTree node){
        if(node.isWinningState()) {
            if (node.getColor() == color)
                node.incrementWins(10);
            else
                node.incrementWins(-100);
        }else {
            NodeTree child = new NodeTree(node);
            ArrayList<Move> freeMoves = node.getState().getFreeMoves();
            child.setMove(freeMoves.remove((int)(Math.random()*freeMoves.size())));
            child.incrementGame();
            if(child.isWinningState()) {
                if (child.getColor() == color)
                    child.incrementWins(10);
                else
                    child.incrementWins(-100);
            }else {
                simulate(child);
            }
        }


    }

    public void simulate(NodeTree nodeTree){
        Board copy = nodeTree.getState().getCopy();
        StatusCell startColor;
        if(nodeTree.getColor()==StatusCell.Blue)
            startColor = StatusCell.Red;
        else
            startColor = StatusCell.Blue;
        boolean exit=false;

            ArrayList<Move> freemoves = copy.getFreeMoves();
            int size = freemoves.size();

            for (int i=0;i<size/2;i++){
                Move move = freemoves.remove((int)(Math.random()*freemoves.size()));
                copy.putStone(move.getX(),move.getY(),startColor);
            }

            if(startColor == StatusCell.Blue)
                startColor = StatusCell.Red;
            else
                startColor = StatusCell.Blue;

            size = freemoves.size();
            for (int i=0;i<size;i++){
                Move move = freemoves.get(i);
                copy.putStone(move.getX(),move.getY(),startColor);
            }

            if(copy.hasWon(color))
                nodeTree.incrementWins(1);
            else
                nodeTree.incrementWins(-1);




        /*
            for(Move move: freemoves){
                copy.putStone(move.getX(),move.getY(),startColor);
                if(copy.hasWon(startColor)) {
                    if(startColor == color){
                        nodeTree.incrementWins(1);
                    }else {
                        nodeTree.incrementWins(-1);
                    }
                    exit=true;
                    break;

                }
                copy.setEmpty(move.getX(),move.getY());
            }

            if(exit)
                break;


            Move finalMove =
            copy.putStone(finalMove.getX(),finalMove.getY(),startColor);

            if(copy.hasWon(color)) {
                nodeTree.incrementWins(1);
                break;
            }
            else if(copy.hasWon(enemy)) {
                nodeTree.incrementWins(-1);
                break;
            }

            if(startColor == StatusCell.Blue)
                startColor = StatusCell.Red;
            else
                startColor = StatusCell.Blue;

            */

    }

    public double selectfn(NodeTree node){
        float vi = node.getTotalWins()/node.getTotalGames();
        int np = node.getTotalGames();
        int ni = node.getParent().getTotalGames();
        double C = Math.sqrt(2);
        if(vi>a)
            C = 0;
        return vi + C * Math.sqrt(Math.log(np)/ni);
    }


    @Override
    public Move getMove() {
        return start();
    }

    @Override
    public NodeTree getRootTreeMcts() {
        return root;
    }


}
