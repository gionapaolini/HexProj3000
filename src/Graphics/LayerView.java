package Graphics;

import BotAlgorithms.MCTS.NodeTree;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

        Map<NodeTree,int[]> map = new HashMap<>(200);
        for (int i = 0; i <values.size(); i++){
            ArrayList<Integer> leftiesNew = new ArrayList<>(200);
            int layerHeight1= (int) (layerHeight*(i*2));
            int layerHeight0= (int) (layerHeight*(i*2-1));

            double totalSimulationsInThisLayer = 0;
            ArrayList<NodeTree> singleLayer = values.get(i);

            for (int j = 0; j <singleLayer.size(); j++){
                totalSimulationsInThisLayer+= singleLayer.get(j).getTotalGames();
            }

            int lastLayerLeft = 0;

            int lastLayerRight = 0;


            int others = 0;


            for (int j = 0; j <singleLayer.size(); j++){
                double choiseTotalGames = singleLayer.get(j).getTotalGames();
                double ratio = choiseTotalGames/totalSimulationsInThisLayer;
                int choiceWidth = (int) (ratio* layerWidth);
                if (choiceWidth<1) {others++; continue;}

                lastLayerRight = lastLayerLeft + choiceWidth;
                g.setColor(new Color(r.nextInt()));
                g.fillRect(lastLayerLeft,layerHeight1,choiceWidth,layerHeight0-layerHeight1);
                int[] valuePair = {lastLayerLeft,lastLayerRight};
                map.put(singleLayer.get(j),valuePair);



                if(i!=0 ) {
                    ;
                    valuePair = map.get(singleLayer.get(j).getParent());
                    if (valuePair==null)break;
                    //draw from currrent lastlayerleftAndRight to parent left and right a polygon!!!
                    //point p1 = valuepair;
                    //p2
                }




                lastLayerLeft = lastLayerRight;
            }

            //print others wich are too small
            double ratio = others/totalSimulationsInThisLayer;
            int choiceWidth = (int) (ratio* layerWidth);
            lastLayerRight = lastLayerLeft + choiceWidth;
            g.setColor(new Color(r.nextInt()));
            g.fillRect(lastLayerLeft,layerHeight1,choiceWidth,layerHeight0-layerHeight1);

            lastLayerLeft = lastLayerRight;


            //go from old ones from left to right and










        }


    }
}
