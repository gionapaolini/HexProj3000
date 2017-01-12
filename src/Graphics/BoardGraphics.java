package Graphics;

import GameLogic.Cell;
import EnumVariables.StatusCell;
import GameLogic.Match;
import GameLogic.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by giogio on 12/26/16.
 */
public class BoardGraphics extends JPanel implements Observer{
    private Match match;
    private Cell[][] grid;
    private Path2D[][] polyCells;
    private Color color;
    private boolean current, canSwap;
    private float distanceXY;
    private float proportion = 4;
    private int x,y;
    public BoardGraphics(Match match){
        grid = match.getBoard().getGrid();

        this.setSize(new Dimension(600,400));
        this.setPreferredSize(new Dimension(600,400));
        this.match = match;
        match.addObserver(this);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(current && !match.isPaused()) {
                    match.putStone(x, y);
                    current=false;
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });


        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent event) {
                if(!match.isPaused() && !match.isBotTurn())
                    checkBounds(event);
            }

            @Override
            public void mouseDragged(MouseEvent event) {

            }
        });




    }

    public Path2D getHexagon(float x, float y, float radius)
    {

        Path2D hexagon = new Path2D.Double();

        hexagon.moveTo(x + radius*Math.sin(0), y + radius* Math.cos(0));
        for(int i = 1; i < 6; ++i) {
            hexagon.lineTo(x + radius*Math.sin(i*2*Math.PI/6), y + radius* Math.cos(i*2*Math.PI/6));
        }
        hexagon.closePath();

        return hexagon;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if(polyCells==null)
            generateHexGrid();
        //paintBorders(g);
        for (int i=0;i<grid.length;i++){
            for (int j=0;j<grid.length;j++){

               if(current && i==y & j==x){
                   g2.setColor(color);
               }else if(grid[i][j].getStatus()==StatusCell.Blue){
                   g2.setColor(Color.BLUE);
               }else if(grid[i][j].getStatus()==StatusCell.Red){
                    g2.setColor(Color.RED);
               }
               else {
                   g2.setColor(Color.gray);
               }
               g2.fill(polyCells[i][j]);
               g2.setColor(Color.black);
               g2.draw(polyCells[i][j]);

            }
        }

    }

    private void generateHexGrid(){
        float initX = 40;
        float initY = 40;
        float radius = 10*proportion;
        distanceXY=3*proportion;
        float X;
        float Y;
        polyCells = new Path2D[grid.length][grid.length];
        for (int i=0;i<grid.length;i++){
            X = initX+radius*i-radius/8*i ;
            Y = initY+(radius*i)+(radius*i)/2 +i*distanceXY;
            for (int j=0;j<grid.length;j++){
                polyCells[i][j] = getHexagon(X,Y,radius);
                X+=radius*2-radius/4 +distanceXY;
            }



        }

    }


    private void checkBounds(MouseEvent event){
        current = false;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid.length;j++){
                if(!canSwap && grid[i][j].getStatus()!=StatusCell.Empty)
                    continue;
                Rectangle2D bound = polyCells[i][j].getBounds2D();
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
        }
        repaint();

    }

    public void paintBorders(Graphics g2){

        Polygon upperSide = new Polygon();
        double lenghtX = polyCells[0][0].getBounds2D().getWidth()*(grid.length)+(distanceXY*(grid.length-1));
        double point1X = polyCells[0][0].getBounds2D().getX() - (3*proportion);
        double point1Y = polyCells[0][0].getBounds2D().getY() - (3*proportion);

        double point2X = point1X + lenghtX + (6*proportion);
        double point2Y = point1Y;

        double point3X = point1X + polyCells[0][0].getBounds2D().getWidth()/2;
        double point3Y = point1Y + polyCells[0][0].getBounds2D().getWidth()/2;

        double point4X = point2X - polyCells[0][0].getBounds2D().getWidth()/2;
        double point4Y = point3Y;

        upperSide.addPoint((int)point1X,(int)point1Y);
        upperSide.addPoint((int)point2X,(int)point2Y);
        upperSide.addPoint((int)point4X,(int)point4Y);
        upperSide.addPoint((int)point3X,(int)point3Y);


        double point1X2 = point3X + polyCells[0][0].getBounds2D().getWidth()/2 * (polyCells.length-2);
        double point1Y2 = polyCells[0][0].getBounds2D().getY() + polyCells[0][0].getBounds2D().getHeight()*(polyCells.length);

        double point2X2 = point1X2 + lenghtX +(6*proportion);
        double point2Y2 = point1Y2;







        Polygon lowerSide = new Polygon();
        lowerSide.addPoint((int)point1X2,(int)point1Y2);
        lowerSide.addPoint((int)point2X2,(int)point2Y2);
        lowerSide.addPoint((int)100,(int)100);


        g2.setColor(Color.RED);
        g2.fillPolygon(upperSide);
        g2.fillPolygon(lowerSide);



    }


    @Override
    public void update(boolean important) {
        grid = match.getBoard().getGrid();
        if(match.isPaused() || match.isBotTurn()){
            current=false;
        }
        if(match.getCurrentColorPlayer()==StatusCell.Red)
            color = new Color(255,0,0,100);
        else
            color = new Color(0,0,255,100);

        if(match.isSwapRule() && match.getHistory().getNValidRecords()<=1){
            canSwap = true;
        }else{
            canSwap = false;
        }
        repaint();

    }
}

