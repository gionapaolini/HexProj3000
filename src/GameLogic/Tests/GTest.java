package GameLogic.Tests;

import GameLogic.Board;
import GameLogic.Cell;
import GameLogic.EnumVariables.StatusCell;
import Graphics.BoardGraphics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by giogio on 12/26/16.
 */
public class GTest {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(500,500));
        frame.setVisible(true);

        Board board = new Board(11);
        Cell[][] grid = board.getGrid();

        grid[5][0].setStatus(StatusCell.Red);
        grid[4][1].setStatus(StatusCell.Blue);
        grid[3][2].setStatus(StatusCell.Red);

        JPanel panel = new BoardGraphics(grid,frame);
        frame.add(panel);
    }
}
