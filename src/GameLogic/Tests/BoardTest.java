package GameLogic.Tests;

import GameLogic.Board;
import GameLogic.Cell;
import GameLogic.EnumVariables.StatusCell;

import java.util.Scanner;

/**
 * Created by giogio on 12/26/16.
 */
public class BoardTest {
    public static void main(String[] args){
        Board board = new Board(11);
        Cell[][] grid = board.getGrid();

        Scanner scanner = new Scanner(System.in);
        int x = 0;
        int y = 0;

        while (x!=500) {
            x = scanner.nextInt();
            y = scanner.nextInt();
            Cell[] arrayCell = grid[y][x].getNeighbors();

            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    boolean gne = false;
                    for(int k=0;k<6;k++){
                        if(grid[i][j].equals(arrayCell[k])) {
                            System.out.print("N ");
                            gne = true;
                            break;
                        }
                    }
                    if(gne)
                        continue;

                    if (grid[i][j].getStatus() == StatusCell.Empty)
                        System.out.print("O ");
                    else if (grid[i][j].getStatus() == StatusCell.Red)
                        System.out.print("R ");
                    else if (grid[i][j].getStatus() == StatusCell.Blue)
                        System.out.print("B ");


                }
                System.out.println(" ");
            }
        }
    }
}
