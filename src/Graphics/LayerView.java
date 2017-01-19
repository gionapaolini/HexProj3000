package Graphics;

import BotAlgorithms.MCTS.NodeTree;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nibbla on 16.01.17.
 */
public class LayerView extends JPanel{
    private final Dimension preservedSize;
    ArrayList<ArrayList<NodeTree>> values;

    public LayerView(NodeTree root, Dimension preveredSize){
        super(new BorderLayout());
        setPreferredSize(preveredSize);
        setBackground(Color.white);

        this.preservedSize = preveredSize;
        int height = root.getHeight();
        System.out.println("Height: " + height);
        values = new ArrayList<>(root.getHeight());
        for (int i = 0; i <= height; i++) {
            values.add(new ArrayList<>());
        }
        ArrayList<NodeTree> breadthFirst = root.breadthFirst();
        int bfs = breadthFirst.size();
        for (int i = 0; i <bfs; i++) {
            NodeTree node = breadthFirst.get(i);
            int depth = node.getDepth();
            values.get(depth).add(node);
        }
        for (int i = 0; i <values.size(); i++) {
            System.out.println("Level " + i + " has " + values.get(i).size() + "members");
        }


    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.red);
        //g.fillRect(0,0,(int)preservedSize.getWidth(),(int)preservedSize.getHeight());

        double layerHeight = this.getHeight() / values.size() / 2;
        double layerWidth = this.getWidth();
        Random r = new Random();
        for (int i = 0; i <values.size(); i++){
            int layerHeight1= (int) (layerHeight*(i*2));
            int layerHeight2= (int) (layerHeight*(i*2+1));

            double totalSimulationsInThisLayer = 0;
            ArrayList<NodeTree> singleLayer = values.get(i);

            for (int j = 0; j <singleLayer.size(); j++){
                totalSimulationsInThisLayer+= singleLayer.get(j).getTotalGames();
            }

            int lastLayerLeft = 0;
            int lastLayerRight = 0;

            for (int j = 0; j <singleLayer.size(); j++){
                double choiseTotalGames = singleLayer.get(j).getTotalGames();
                double ratio = choiseTotalGames/totalSimulationsInThisLayer;
                int choiceWidth = (int) (ratio* layerWidth);
                lastLayerRight = lastLayerLeft + choiceWidth;
                g.setColor(new Color(r.nextInt()));
                g.fillRect(lastLayerLeft,layerHeight1,choiceWidth,layerHeight2-layerHeight1);
                lastLayerLeft = lastLayerRight;
            }



            //print first layer

            //print connection layer

        }


    }
}
