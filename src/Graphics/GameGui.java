package Graphics;

import GameLogic.Match;
import GameLogic.Observer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by giogio on 1/11/17.
 */
public class GameGui extends JPanel implements Observer{
    JFrame mainFrame;
    ControlPanel controlPanel;
    HistoryPanel historyPanel;
    Match match;
    BoardGraphics graphics;
    JButton openHistory;



    public GameGui(BoardGraphics graphics, Match match, UserInterface userInterface,JFrame frame){
        mainFrame = frame;
        setLayout(new MigLayout());
        openHistory = new JButton("<html><center>H<br>i<br>s<br>t<br>o<br>r<br>y</center></html>");
        openHistory.setSize(new Dimension(5,400));
        openHistory.setPreferredSize(new Dimension(5,400));
        this.match = match;
        this.graphics = graphics;
        controlPanel = new ControlPanel(match,userInterface);
        historyPanel = new HistoryPanel(this);
        add(controlPanel);
        add(graphics);
        add(historyPanel);
        match.addObserver(this);

        openHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                openHistoryPanel();
            }
        });




    }

    public void openHistoryPanel(){
        remove(openHistory);
        add(historyPanel);
        mainFrame.pack();
    }
    public void closeHistoryPanel(){
        remove(historyPanel);
        add(openHistory);
        mainFrame.pack();
    }

    @Override
    public void update(boolean important) {
        controlPanel.setTime(match.getTime());

        if(important) {
            controlPanel.setPlayerTurn(match.getCurrentColorPlayer());
            if (match.isWon())
                controlPanel.setWin(match.getCurrentColorPlayer());

            historyPanel.updateHistory(match.getHistory().getRecords());
        }

    }


    public Match getMatch() {
        return match;
    }


}
