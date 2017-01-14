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
        NodeTree root = new NodeTree(enemy,initialBoard);
        int count = 0;
        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis()-startTime<=maxTime){
            expand(selection(root));
            count++;
        }
        System.out.println(count);
        return selectBestMove(root);
    }

    private Move selectBestMove(NodeTree root){
        int nWins=0;
        Move bestMove = root.getChildrens().get(0).getMove();
        for(NodeTree nodeTree: root.getChildrens()){
            if(nodeTree.getTotalWins()>nWins){
                nWins = nodeTree.getTotalWins();
                bestMove = nodeTree.getMove();
            }
        }
        return bestMove;
    }
    public NodeTree selection(NodeTree node){

        if(node.getState().getFreeMoves().size()>0)
            return node;

        double score = -100000;
        NodeTree result = node;
        for(NodeTree nodeChild: node.getChildrens()){
            double newScore = selectfn(nodeChild);
            if(newScore>score){
                score = newScore;
                result = nodeChild;
            }
        }
        System.out.println(score);
        return selection(result);
    }

    public void expand(NodeTree node){
        if(node.isWinningState()) {
            System.out.println("YES -----------------------------------------------------------");
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
                System.out.println("YES ============================================================");
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
        while (1==1){
            ArrayList<Move> freemoves = copy.getFreeMoves();
            Move move = freemoves.remove((int)(Math.random()*freemoves.size()));
            copy.putStone(move.getX(),move.getY(),startColor);

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


        }
    }

    public double selectfn(NodeTree node){
        int vi = node.getTotalWins();
        int np = node.getTotalGames();
        int ni = node.getParent().getTotalGames();
        double C = Math.sqrt(2);
        return vi + C * Math.sqrt(Math.log(np)/ni);
    }


    @Override
    public Move getMove() {
        return start();
    }
}
