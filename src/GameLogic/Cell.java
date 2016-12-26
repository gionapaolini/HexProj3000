package GameLogic;

import GameLogic.EnumVariables.StatusCell;

/**
 * Created by giogio on 12/26/16.
 */
public class Cell {
    private StatusCell status;
    private Cell[] neighbors;
    private Cell UL,UR,R,LR,LL,L; //U stands for UPPER and L and R for LEFT and RIGHT.
    private int coordXJ;
    private int coordYI;

    public Cell(int xCoord, int yCoord){
        status = StatusCell.Empty;
        neighbors = new Cell[6];
        coordXJ = xCoord;
        coordYI = yCoord;
    }
    public void setNeighbors(Cell[][] grid){
        if(coordYI>0) {
            UL = grid[coordYI - 1][coordXJ];
            if(coordXJ<grid.length-1)
                UR = grid[coordYI - 1][coordXJ + 1];
        }
        if(coordYI<grid.length-1) {
            LR = grid[coordYI + 1][coordXJ];
            if(coordXJ>0)
                LL = grid[coordYI + 1][coordXJ - 1];
        }
        if(coordXJ<grid.length-1)
            R = grid[coordYI][coordXJ+1];
        if(coordXJ>0)
            L = grid[coordYI][coordXJ-1];

        neighbors[0] = UL;
        neighbors[1] = UR;
        neighbors[2] = R;
        neighbors[3] = LR;
        neighbors[4] = LL;
        neighbors[5] = L;
    }
    public void setStatus(StatusCell status){
        this.status = status;
    }
    public StatusCell getStatus(){
        return status;
    }

    public Cell[] getNeighbors(){
        return neighbors;
    }

    public int getCoordXJ(){
        return coordXJ;
    }
    public int getCoordYI(){
        return coordYI;
    }



}
