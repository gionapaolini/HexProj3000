package Graphics;

import GameLogic.Board;
import GameLogic.Cell;
import GameLogic.EnumVariables.StatusCell;

import javax.swing.*;
import java.awt.*;

/**
 * Created by giogio on 12/26/16.
 */
public class BoardGraphics extends JPanel{
    Cell[][] grid;
    public BoardGraphics(Cell[][] board){
        this.setPreferredSize(new Dimension(300,300));
        grid = board;
    }

    public Polygon getHexagon(float x, float y, float h)
    {
        Polygon hexagon = new Polygon();

        for (int i=0; i < 7; i++)
        {
            double a = Math.PI / 3.0 * i;
            hexagon.addPoint((int)(Math.round(x + Math.sin(a) * h)), (int)(Math.round(y + Math.cos(a) * h)));
        }
        return hexagon;
    }

    public void paintComponent(Graphics g){
        float dimension = 2f;
        int offsetX =0;
        int offsetY =0;
        int k=10;

        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){

                if(grid[i][j].getStatus()== StatusCell.Blue) {
                    g.setColor(Color.blue);
                    g.fillPolygon(getHexagon((j * 20 + k + offsetX) * dimension, (i * 17 + 10 + offsetY) * dimension, 10 * dimension));
                }
                else if(grid[i][j].getStatus()== StatusCell.Red) {
                    g.setColor(Color.red);
                    g.fillPolygon(getHexagon((j * 20 + k + offsetX) * dimension, (i * 17 + 10 + offsetY) * dimension, 10 * dimension));
                }
                else {
                    g.setColor(Color.black);
                    g.drawPolygon(getHexagon((j * 20 + k + offsetX) * dimension, (i * 17 + 10 + offsetY) * dimension, 10 * dimension));
                }
            }
            k+=10;
        }
    }
}

