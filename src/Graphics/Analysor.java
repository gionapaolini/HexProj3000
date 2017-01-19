package Graphics;

//import Graphics.ChessView.ChessView;

import hypertree.HTView;
import hypertree.HyperTree;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by Nibbla on 11.01.2017.
 */
public class Analysor extends JFrame{
    private  UserInterface userInterface;
    private  JPanel buttonPanel;
    private  JPanel mainPanel;

    private  JButton chessViewButton = new JButton("Chess Board View");
    private  JButton hyperViewButton = new JButton("Hyperplane View");
    private  JButton layerViewButton = new JButton("Layer View");

    //private ChessView chessView;
    private HTView hyperTreeView;
    private LayerView lw;
    private JPanel layerView;


    public Analysor(UserInterface ui) {
        this.userInterface = ui;

        this.setLayout(new BorderLayout());


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        this.add(buttonPanel,BorderLayout.WEST);

        mainPanel = new JPanel(); mainPanel.setPreferredSize(new Dimension(800,600));
        this.add(mainPanel, BorderLayout.CENTER);

        buttonPanel.add(chessViewButton);
        buttonPanel.add(hyperViewButton);
        buttonPanel.add(layerViewButton);


        chessViewButton.addActionListener(e -> showChessView(userInterface));
        hyperViewButton.addActionListener(e -> showHyperView(userInterface));
        layerViewButton.addActionListener(e -> showLayerView(userInterface));



        this.pack();


    }

    private void showLayerView(UserInterface ui) {

        Dimension preveredSize = new Dimension(800,600);
        LayerView layer = new LayerView(ui.gameGui.getMatch().getRootTreeMcts(),preveredSize);


        replaceMainPanel(layer);
        layer.setVisible(true);
        layer.repaint();

   }

    private void showHyperView(UserInterface ui) {
        String pathRoot = "I:\\DateienBilderPersönlich";
        File rootFile = new File(pathRoot);


        HyperTree hypertree = new HyperTree(ui.gameGui.getMatch().getRootTreeMcts());

        Dimension preveredSize = new Dimension(800,600);
                hyperTreeView = hypertree.getView(preveredSize);
       // this.add(hyperTreeView, BorderLayout.CENTER);
       // this.pack();
       // repaint();
        replaceMainPanel(hyperTreeView);
        hyperTreeView.setVisible(true);
        hyperTreeView.repaint();

    }

    private void replaceMainPanel(Component component) {

       Component[] components = this.rootPane.getComponents();
      //  for (Component c : components){
       //     if (c.equals(buttonPanel)){
        //        continue;
       //     }}
        if (mainPanel!=null){
            remove(mainPanel);
            mainPanel=null;
        }
       //if (chessView!=null) remove(chessView);
        if (hyperTreeView!=null) remove(hyperTreeView);
        //if (nnw!=null) remove(nnw);

        this.add(component, BorderLayout.CENTER);


        pack();
        repaint();
        revalidate();

    }

    private void showChessView(UserInterface mainGui) {

    }
}
