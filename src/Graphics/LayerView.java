package Graphics;

import BotAlgorithms.MCTS.NodeTree;
import BotAlgorithms.MCTS_2.NodeTree_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
 * Created by nibbla on 16.01.17.
 */
public class LayerView extends JPanel{
    private final Dimension preservedSize;
    private final UserInterface userInterface;
    ArrayList<ArrayList<NodeTree_2>> values;
    private LinkedList<Polygon> polygones  = new LinkedList<>();;
    private HashMap<Polygon, NodeTree_2> messages = new HashMap<>(4000);

    public LayerView(UserInterface ui, Dimension preveredSize){
        super(new BorderLayout());


        setPreferredSize(preveredSize);
        setBackground(Color.white);

        this.preservedSize = preveredSize;

        userInterface = ui;

        createTree();



        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for(Polygon p: polygones){
                    if (p.contains(e.getPoint())){
                        JOptionPane.showMessageDialog(null, messages.get(p).toString(), "Info", JOptionPane.OK_CANCEL_OPTION);
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void createTree() {
        NodeTree_2 root = userInterface.gameGui.getMatch().getRootTreeMcts();
        int height = root.getHeight();
        System.out.println("Height: " + height);
        values = new ArrayList<>(root.getHeight());
        for (int i = 0; i <= height; i++) {
            values.add(new ArrayList<>());
        }
        ArrayList<NodeTree_2> breadthFirst = root.breadthFirst();
        int bfs = breadthFirst.size();
        for (int i = 0; i <bfs; i++) {
            NodeTree_2 node = breadthFirst.get(i);
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
        messages.clear();
        messages = new HashMap<>(4000);
        polygones.clear();
        polygones = new LinkedList<>();
        double layerHeight = this.getHeight() / values.size() / 2;
        double layerWidth = this.getWidth();
        Random r = new Random();
        createTree();
        Map<NodeTree_2,int[]> map = new HashMap<>(200);
        for (int i = 0; i <values.size(); i++){
            ArrayList<Integer> leftiesNew = new ArrayList<>(200);
            int layerHeight1= (int) (layerHeight*(i*2));
            int layerHeight2= (int) (layerHeight*(i*2+1));

            double totalSimulationsInThisLayer = 0;
            ArrayList<NodeTree_2> singleLayer = values.get(i);

            for (int j = 0; j <singleLayer.size(); j++){
                totalSimulationsInThisLayer+= singleLayer.get(j).getGames();
            }

            int lastLayerLeft = 0;

            int lastLayerRight = 0;


            int others = 0;


            for (int j = 0; j <singleLayer.size(); j++){
                double choiseTotalGames = singleLayer.get(j).getGames();
                double ratio = choiseTotalGames/totalSimulationsInThisLayer;
                int choiceWidth = (int) (ratio* layerWidth);
                if (choiceWidth<1) {others++; continue;}

                lastLayerRight = lastLayerLeft + choiceWidth;
                g.setColor(new Color(r.nextInt()));
                g.fillRect(lastLayerLeft,layerHeight1,choiceWidth,layerHeight2-layerHeight1);
                int[] valuePair = {lastLayerLeft,lastLayerRight};
                map.put(singleLayer.get(j),valuePair);

                int[] XpointsSqare = new int[4];
                int[] YpointsSqare = new int[4];
                XpointsSqare[0] = lastLayerLeft;
                XpointsSqare[1] = lastLayerRight;
                XpointsSqare[2] = lastLayerRight;
                XpointsSqare[3] = lastLayerLeft;

                YpointsSqare[2] = layerHeight2;
                YpointsSqare[3] = layerHeight2;
                YpointsSqare[0] = layerHeight1;
                YpointsSqare[1] = layerHeight1;
                Polygon p = new Polygon(XpointsSqare,YpointsSqare,4);
                polygones.add(p);
                messages.put(p,singleLayer.get(j));


                if(i!=0 ) {
                    int layerHeight_minus1= (int) (layerHeight*(i*2-1));

                    valuePair = map.get(singleLayer.get(j).getParent());
                    if (valuePair==null)break;
                    //draw from currrent lastlayerleftAndRight to parent left and right a polygon!!!
                    int[] Xpoints = new int[4];
                    int[] Ypoints = new int[4];
                    Xpoints[0] = valuePair[0];
                    Xpoints[1] = valuePair[1];
                    Xpoints[2] = lastLayerRight;
                    Xpoints[3] = lastLayerLeft;

                    Ypoints[2] = layerHeight1;
                    Ypoints[3] = layerHeight1;
                    Ypoints[0] = layerHeight_minus1;
                    Ypoints[1] = layerHeight_minus1;

                    g.fillPolygon(Xpoints,Ypoints,4);
                }




                lastLayerLeft = lastLayerRight;
            }

            //print others wich are too small
            double ratio = others/totalSimulationsInThisLayer;
            int choiceWidth = (int) (ratio* layerWidth);
            lastLayerRight = lastLayerLeft + choiceWidth;
            g.setColor(new Color(r.nextInt()));
            g.fillRect(lastLayerLeft,layerHeight1,choiceWidth,layerHeight2-layerHeight1);

            lastLayerLeft = lastLayerRight;


            //go from old ones from left to right and










        }


    }
}
