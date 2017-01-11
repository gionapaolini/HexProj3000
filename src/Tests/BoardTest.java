package Tests;

import GameLogic.Board;
import GameLogic.Cell;
import EnumVariables.StatusCell;

/**
 * Created by giogio on 12/26/16.
 */
public class BoardTest {
    public static void main(String[] args){
        Board board = new Board(11);
        Cell[][] grid = board.getGrid();

        grid[5][0].setStatus(StatusCell.Red);
        grid[4][1].setStatus(StatusCell.Blue);
        grid[3][2].setStatus(StatusCell.Red);




        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {

                if (grid[i][j].getStatus() == StatusCell.Empty)
                    System.out.print("O ");
                else if (grid[i][j].getStatus() == StatusCell.Red)
                    System.out.print("R ");
                else if (grid[i][j].getStatus() == StatusCell.Blue)
                    System.out.print("B ");


            }
            System.out.println(" ");

        }
        System.out.println(board.isConnected(grid[5][0],grid[3][2],null));
    }
}
