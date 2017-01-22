package BotAlgorithms.PathFinding;

import EnumVariables.StatusCell;
import GameLogic.Cell;
import GameLogic.Move;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by giogio on 1/22/17.
 */
public class PathFinding {

    Cell startCell, targetCell;
    HashMap<Cell,StarNode> cellMap = new HashMap<>();
    StatusCell ally, enemy;
    int minX,maxX,minY,maxY;
    boolean bounded;

    public PathFinding(Cell startCell, Cell targetCell, StatusCell color) {
        this.startCell = startCell;
        this.targetCell = targetCell;
        this.ally = color;
        if(color == StatusCell.Blue)
            enemy = StatusCell.Red;
        else
            enemy = StatusCell.Blue;
        StarNode root = new StarNode(null, startCell);
        cellMap.put(startCell,root);
        bounded = false;
    }

    public ArrayList<Move> start(){
        StarNode currentNode = cellMap.get(startCell);
        evaluateNode(currentNode);
        currentNode.setVisited(true);
        while (currentNode.h!=0){
            exploreNeighbours(currentNode);
            currentNode = pickBestNode();
            currentNode.setVisited(true);

        }
        if(currentNode.getNode()==null){
            return null;
        }

        return getPath(currentNode);

    }

    public ArrayList<Move> getPath(StarNode end){
        ArrayList<Move> path = new ArrayList<>();
        while (end!=null) {
            Move move = new Move(end.getNode().getCoordXJ(), end.getNode().getCoordYI());
            path.add(move);
            end = end.explorer;
        }
        return path;
    }

    public StarNode pickBestNode(){
        StarNode best = new StarNode(null,null);
        for (StarNode starNode: cellMap.values()){
            if(!starNode.isVisited())
                best = starNode;
        }
        for (StarNode starNode: cellMap.values()){
            if(!starNode.isVisited() && ((starNode.f<best.f) || (starNode.f == best.f && starNode.h < best.h)))
                best = starNode;
        }
        return best;
    }

    public void exploreNeighbours(StarNode node){
        Cell cell = node.getNode();
        for(Cell neighbour: cell.getNeighbors()){
            if(bounded && outOfBound(neighbour))
                continue;
            if(neighbour!=null && neighbour.getStatus()!=enemy){
                StarNode starNode = cellMap.get(neighbour);
                if(starNode==null) {
                    createStarNode(neighbour, node);

                }else if(!starNode.isVisited() && isBetterConnection(starNode, node)){
                    starNode.setExplorer(node);
                    starNode.setG();
                    starNode.setF();
                }
            }
        }
    }

    public boolean isBetterConnection(StarNode node, StarNode newExplorer){
        if(node.getG()>newExplorer.getG()+1){
            return true;
        }
        return false;
    }

    public void createStarNode(Cell cell, StarNode explorer){
        StarNode starNode = new StarNode(explorer,cell);
        cellMap.put(cell,starNode);
        evaluateNode(starNode);

    }

    public boolean outOfBound(Cell cell){
        if(cell.getCoordXJ()<minX)
            return true;
        if(cell.getCoordXJ()>maxX)
            return true;
        if(cell.getCoordYI()<minY)
            return true;
        if(cell.getCoordYI()>maxY)
            return true;
        return false;
    }
    public void evaluateNode(StarNode node){
        int currentX = node.getNode().getCoordXJ();
        int currentY = node.getNode().getCoordYI();
        int count=0;
        while (currentX != targetCell.getCoordXJ() || currentY != targetCell.getCoordYI()){
            if(currentX<targetCell.getCoordXJ() ) {
                currentX++;
                count++;
            }
            if(currentX>targetCell.getCoordXJ() ) {
                currentX--;
                count++;
            }
            if(currentY<targetCell.getCoordYI()) {
                currentY++;
                count++;
            }
            if(currentY>targetCell.getCoordYI()) {
                currentY--;
                count++;
            }

        }

        node.setH(count);
        node.setG();
        node.setF();
    }

    public void setBound(int minX, int maxX, int minY, int maxY){
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        bounded = true;
    }


}
