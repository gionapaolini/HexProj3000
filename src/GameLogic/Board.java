package GameLogic;

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
        grid = new Cell[size][size];
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                grid[i][j] = new Cell(j,i);
            }
        }
        connectNeighbours();
    }

    private void connectNeighbours(){
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

}
