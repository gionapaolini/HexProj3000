package Graphics;

import GameLogic.Cell;
import GameLogic.EnumVariables.StatusCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

/**
 * Created by giogio on 12/26/16.
 */
public class BoardGraphics extends JPanel{
    Cell[][] grid;
    JFrame mainFrame;
    float proportion=1;
    float dimension;
    float distanceX;
    float distanceY;
    float offsetX;
    float offsetY;
    float stepOffsetX;
    boolean current;
    int x,y;
    public BoardGraphics(Cell[][] board, JFrame frame){
        grid = board;
        mainFrame = frame;
        this.setSize(frame.getSize());
        this.setPreferredSize(frame.getSize());



        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent event) {
               checkBounds(event);
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                checkBounds(event);
            }
        });




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
        super.paintComponent(g);
        this.setSize(mainFrame.getSize());
        this.setPreferredSize(mainFrame.getSize());
        proportion = mainFrame.getSize().width/(float)(30*grid.length);
        dimension = 10*proportion;
        distanceX=20*proportion;
        distanceY=20*proportion;
        offsetX =10*proportion;
        offsetY =10*proportion;
        stepOffsetX = 10*proportion;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid.length;j++){

                if(grid[i][j].getStatus()== StatusCell.Blue) {
                    g.setColor(Color.blue);
                    g.fillPolygon(getHexagon(j*distanceX+offsetX,i*distanceY+offsetY,dimension));
                }
                else if(grid[i][j].getStatus()== StatusCell.Red) {
                    g.setColor(Color.red);
                    g.fillPolygon(getHexagon(j*distanceX+offsetX,i*distanceY+offsetY,dimension));
                }
                else {
                    if(current && x==j && y==i) {
                        g.setColor(Color.yellow);
                        g.fillPolygon(getHexagon(j * distanceX + offsetX, i * distanceY + offsetY, dimension));

                    }else {
                        g.setColor(Color.black);
                        g.drawPolygon(getHexagon(j * distanceX + offsetX, i * distanceY + offsetY, dimension));
                    }
                }
            }
            offsetX+=stepOffsetX;

        }

    }


    private void checkBounds(MouseEvent event){
        current = false;
        proportion = mainFrame.getSize().width/(float)(30*grid.length);
        dimension = 10*proportion;
        distanceX=20*proportion;
        distanceY=20*proportion;
        offsetX =10*proportion;
        offsetY =10*proportion;
        stepOffsetX = 10*proportion;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid.length;j++){
                Rectangle2D bound = getHexagon(j * distanceX + offsetX, i * distanceY + offsetY, dimension).getBounds2D();
                if(event.getX()>=bound.getX() && event.getX()<=bound.getX()+bound.getWidth() &&
                        event.getY()>=bound.getY() && event.getY()<=bound.getY()+bound.getHeight()){
                    current = true;
                    x = j;
                    y = i;
                    break;
                }

             }
            if(current)
                break;
            offsetX+=stepOffsetX;

        }
        repaint();

    }



}

