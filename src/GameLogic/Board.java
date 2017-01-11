package GameLogic;

import EnumVariables.StatusCell;

/**
 * Created by giogio on 12/26/16.
 */
public class Board {
    private int size;
    private Cell[][] grid;
    public Board(int size){
        this.size = size;
        initializeGrid();
    }

    private void initializeGrid(){
        //create an empty grid of cells
        grid = new Cell[size][size];
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                grid[i][j] = new Cell(j,i);
            }
        }
        connectNeighbours();
    }

    private void connectNeighbours(){
        //set all the neighbours for the cells of the grid
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                grid[i][j].setNeighbors(grid);
            }
        }
    }

    public Cell[][] getGrid(){
        return grid;
    }

    public int getSize(){
        return size;
    }

    //check if two cells are connected by a colour.
    public boolean isConnected(Cell start, Cell target, StatusCell color){
        //if the start or the target are empty or
        //if the start color is different from the target color then the two are not connected and return false

        if(start.getStatus()!=color || target.getStatus()!= color || start.getStatus()==StatusCell.Empty || target.getStatus() ==StatusCell.Empty || start.getStatus()!=target.getStatus())
            return false;



        //otherwise it creates a grid of boolean to keep track of the checked cells.
        boolean[][] statusGrid = new boolean[size][size];


        return checkNeighbours(start,target, statusGrid);

    }

    public boolean checkNeighbours(Cell start, Cell target, boolean[][] statusGrid){
        //if target is reached return true;
        if(start.equals(target))
            return true;

        //otherwise set the cell checked
        statusGrid[start.getCoordYI()][start.getCoordXJ()]=true;
        Cell[] neighbours = start.getNeighbors();
        //checks all the neighbours for connections
        for (int i=0;i<6;i++){
            //if the neighbour exists, it is not already checked, and it has the same color as the target
            if(neighbours[i]!=null && statusGrid[neighbours[i].getCoordYI()][neighbours[i].getCoordXJ()]==false && neighbours[i].getStatus()==target.getStatus())
                //recursively checks its neighbours
                if(checkNeighbours(neighbours[i],target, statusGrid))
                    return true;

        }
        //return false if it checks all possible connections without finding the target
        return false;

    }

    public void putStone(int x, int y, StatusCell color){
        if(grid[y][x].getStatus()== StatusCell.Empty){
            grid[y][x].setStatus(color);
        }else {
            System.out.println("Not available!");
        }
    }

    public boolean hasWon(StatusCell color){
        if(color == StatusCell.Red){
            for (int i=0;i<size;i++){
                for (int j=0;j<size;j++){
                    if(isConnected(grid[0][i],grid[size-1][j],color))
                        return true;
                }
            }
        }else {
            for (int i=0;i<size;i++){
                for (int j=0;j<size;j++){
                    if(isConnected(grid[i][0],grid[j][size-1], color))
                        return true;
                }
            }
        }
        return false;
    }

}
