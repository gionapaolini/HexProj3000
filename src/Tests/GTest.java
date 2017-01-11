package Tests;

import GameLogic.Board;
import GameLogic.Cell;
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

        Board board = new Board(5);
        Cell[][] grid = board.getGrid();



        JPanel panel = new BoardGraphics(null,frame);
        frame.add(panel);
    }
}
