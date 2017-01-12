package Graphics;
import GameLogic.Match;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by giogio on 12/28/16.
 */
public class MenuGUI extends JPanel {
    private JFrame frame;
    private final JCheckBox swaprule = new JCheckBox();
    private final JCheckBox learningMode = new JCheckBox();

    private final JLabel title = new JLabel("HexGame");
    private final JLabel swapRuleLabel = new JLabel("SwapRule: ");
    private final JLabel learningLabel = new JLabel("Learning mode: ");
    private final JLabel gameModeLabel = new JLabel("Game mode: ");
    private final JLabel colorLabel = new JLabel("Your color: ");
    private final JLabel botTypeLabel = new JLabel("Type: ");
    private final JLabel maxTimeLabel = new JLabel("Max-time per move: ");
    private final JLabel maxDepthTreeLabel = new JLabel("Max depth-level tree : ");

    private final JTextField blueMaxTime = new JTextField();
    private final JTextField redMaxTime = new JTextField();
    private final JTextField blueDepthTree = new JTextField();
    private final JTextField redDepthTree = new JTextField();


    private final String[] gameTypeEntries = {"Human vs Human", "Human vs Bot", "Bot vs Bot"};
    private final String[] botTypeEntries = {"PathFinding", "MCTS", "A-B Pruning"};
    private final String[] colorEntries = {"Red","Blue"};

    private final JComboBox<String>  gameType = new JComboBox<>(gameTypeEntries);
    private final JComboBox<String>  botTypeBlue = new JComboBox<>(botTypeEntries);
    private final JComboBox<String>  botTypeRed = new JComboBox<>(botTypeEntries);
    private final JComboBox<String>  colorType = new JComboBox<>(colorEntries);

    private final JButton redButton = new JButton("RedBot Settings");
    private final JButton blueButton = new JButton("BlueBot Settings");
    private final JButton startButton = new JButton("Start");

    public MenuGUI(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        blueDepthTree.setText("1000");
        redDepthTree.setText("1000");
        blueMaxTime.setText("1000");
        redMaxTime.setText("1000");

        setListener();

        this.setLayout(new MigLayout("","center"));
        this.add(title, "span");
        this.add(swapRuleLabel);
        this.add(swaprule, "wrap");
        this.add(gameModeLabel);
        this.add(gameType,"wrap");
        this.add(learningLabel);
        this.add(learningMode,"wrap");
        this.add(startButton,"span");
        frame.setVisible(true);

    }

    public void multiplayer(){
        clear();
        this.add(learningLabel);
        this.add(learningMode,"wrap");
    }

    public void humanBot(){
        clear();
        colorType.setSelectedItem("Blue");
        this.add(learningLabel);
        this.add(learningMode,"wrap");
        this.add(colorLabel);
        this.add(colorType, "wrap");
        this.add(redButton, "span");
    }

    public void botBot(){
        clear();
        this.add(blueButton);
        this.add(redButton,"wrap");
    }

    public void redSelection(){
        clear();
        this.add(learningLabel);
        this.add(learningMode,"wrap");
        this.add(colorLabel);
        this.add(colorType, "wrap");
        this.add(redButton, "span");

    }
    public void blueSelection(){
        clear();
        this.add(learningLabel);
        this.add(learningMode,"wrap");
        this.add(colorLabel);
        this.add(colorType, "wrap");
        this.add(blueButton, "span");
    }

    public void clear(){
        this.remove(learningLabel);
        this.remove(learningMode);
        this.remove(colorLabel);
        this.remove(colorType);
        this.remove(botTypeBlue);
        this.remove(botTypeRed);
        this.remove(startButton);
        this.remove(redButton);
        this.remove(blueButton);



    }
    public void addStartButton(){
        this.add(startButton,"span");

        this.validate();
        this.repaint();
    }

    public void blueSetting(){
        JFrame blueFrame = new JFrame();
        blueFrame.setLayout(new MigLayout("","center"));
        blueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        blueFrame.setTitle("Blue Bot Settings");
        blueFrame.add(botTypeLabel);
        blueFrame.add(botTypeBlue,"wrap");
        blueFrame.add(maxTimeLabel);
        blueFrame.add(blueMaxTime, "wrap, grow");
        blueFrame.add(maxDepthTreeLabel);
        blueFrame.add(blueDepthTree, "wrap, grow");
        blueMaxTime.setEnabled(false);
        blueDepthTree.setEnabled(false);
        blueFrame.pack();
        blueFrame.setLocationRelativeTo(null);
        blueFrame.setVisible(true);

    }
    public void redSetting(){
        JFrame redFrame = new JFrame();
        redFrame.setLayout(new MigLayout("","center"));
        redFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        redFrame.setTitle("Red Bot Settings");
        redFrame.add(botTypeLabel);
        redFrame.add(botTypeRed,"wrap");
        redFrame.add(maxTimeLabel);
        redFrame.add(redMaxTime, "wrap, grow");
        redFrame.add(maxDepthTreeLabel);
        redFrame.add(redDepthTree, "wrap, grow");
        redMaxTime.setEnabled(false);
        redDepthTree.setEnabled(false);
        redFrame.pack();
        redFrame.setLocationRelativeTo(null);
        redFrame.setVisible(true);

    }

    public void setListener(){
        gameType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(gameType.getSelectedItem()=="Human vs Human")
                    multiplayer();
                else if(gameType.getSelectedItem()=="Human vs Bot")
                    humanBot();
                else
                    botBot();

                addStartButton();
                frame.pack();

            }

        });
        colorType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(colorType.getSelectedItem()=="Red")
                    blueSelection();
                else
                    redSelection();
                addStartButton();
            }
        });

        blueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                blueSetting();
            }
        });
        redButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               redSetting();
            }
        });

        botTypeBlue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(botTypeBlue.getSelectedItem()=="PathFinding"){
                    blueMaxTime.setEnabled(false);
                    blueDepthTree.setEnabled(false);
                }else{
                    blueMaxTime.setEnabled(true);
                    blueDepthTree.setEnabled(true);
                }
            }
        });
        botTypeRed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(botTypeRed.getSelectedItem()=="PathFinding"){
                    redMaxTime.setEnabled(false);
                    redDepthTree.setEnabled(false);
                }else{
                    redMaxTime.setEnabled(true);
                    redDepthTree.setEnabled(true);
                }
            }
        });
    }

    public Match getMatch(){
        /*
        if(gameType.getSelectedItem().equals("Human vs Human"))
            "Human vs Human", "Human vs Bot", "Bot vs Bot"};
            */
        return null;
    }




}
