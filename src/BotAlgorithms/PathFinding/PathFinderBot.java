package BotAlgorithms.PathFinding;

import BotAlgorithms.MCTS_2.NodeTree_2;
import BotAlgorithms.Strategy;
import EnumVariables.StatusCell;
import GameLogic.Board;
import GameLogic.Cell;
import GameLogic.Match;
import GameLogic.Move;

import java.util.ArrayList;

/**
 * Created by giogio on 1/22/17.
 */
public class PathFinderBot implements Strategy {

    Match match;
    StatusCell color;

    public PathFinderBot(Match match, StatusCell color) {
        this.match = match;
        this.color = color;
    }

    public Move start(){
        if(color == StatusCell.Blue)
            return bluePath();
        else
            return redPath();
    }


    public Move redPath(){

        Cell[][] grid = match.getBoard().getGrid();
        ArrayList<Move> best = new ArrayList<Move>();
        int bestMovesSize = 10000;
        for (int i=0;i<grid.length;i++){
            for (int j=0;j<grid.length;j++){
                if(grid[0][i].getStatus()==StatusCell.Empty && grid[grid.length-1][j].getStatus()==StatusCell.Empty) {
                    PathFinding pathFinding = new PathFinding(grid[0][i], grid[grid.length-1][j], color);
                    ArrayList<Move> moves = pathFinding.start();
                    if (moves != null && moves.size() < bestMovesSize) {
                        bestMovesSize = moves.size();
                        best = moves;
                    }
                }

            }
        }
        boolean firstToPut = true;
        for (int i=0;i<grid.length;i++){
            for (int j=0;j<grid.length;j++){
                if(grid[i][j].getStatus()==color){
                    for (int k=0;k<grid.length;k++){
                        PathFinding pathFinding = new PathFinding(grid[i][j],grid[grid.length-1][k],color);
                        ArrayList<Move> moves = pathFinding.start();
                        if(moves!=null && moves.size()<=bestMovesSize){
                            bestMovesSize=moves.size();
                            best = moves;
                            firstToPut = false;
                        }
                    }

                }

            }
        }
        if(!firstToPut)
            best.remove(best.size()-1);

        return best.get(best.size()-1);

    }


    public Move bluePath(){

        Cell[][] grid = match.getBoard().getGrid();
        ArrayList<Move> best = new ArrayList<Move>();
        int bestMovesSize = 10000;
        for (int i=0;i<grid.length;i++){
            for (int j=0;j<grid.length;j++){
                if(grid[i][0].getStatus()==StatusCell.Empty && grid[j][grid.length-1].getStatus()==StatusCell.Empty) {
                    PathFinding pathFinding = new PathFinding(grid[i][0], grid[j][grid.length-1], color);
                    ArrayList<Move> moves = pathFinding.start();
                    if (moves != null && moves.size() < bestMovesSize) {
                        bestMovesSize = moves.size();
                        best = moves;
                    }
                }

            }
        }
        boolean firstToPut = true;
        for (int i=0;i<grid.length;i++){
            for (int j=0;j<grid.length;j++){
                if(grid[i][j].getStatus()==color){
                    for (int k=0;k<grid.length;k++){
                        PathFinding pathFinding = new PathFinding(grid[i][j],grid[k][grid.length-1],color);
                        ArrayList<Move> moves = pathFinding.start();
                        if(moves!=null && moves.size()<=bestMovesSize){
                            bestMovesSize=moves.size();
                            best = moves;
                            firstToPut = false;
                        }
                    }

                }

            }
        }
        if(!firstToPut)
            best.remove(best.size()-1);

        return best.get(best.size()-1);

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
