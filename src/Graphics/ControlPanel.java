package Graphics;

import EnumVariables.StatusCell;
import GameLogic.TimeGame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by giogio on 1/11/17.
 */
public class ControlPanel extends JPanel {
    private final JLabel playerText = new JLabel("<html>Player <font color='blue'>Blue</font> is your turn!</html>");
    private final JLabel timeText = new JLabel("Time: 00:00:00");
    private final JButton pauseButton = new JButton("Pause");
    private final JButton undoButton = new JButton("Undo");
    private final JButton loadButton = new JButton("Load");
    private final JButton saveButton = new JButton("Save");

    public ControlPanel(){
        setSize(new Dimension(200,400));
        setPreferredSize(new Dimension(200,400));
        setBackground(Color.GREEN);
        setLayout(new MigLayout("","center"));
        add(playerText,"span");
        add(timeText,"span");
        add(undoButton);
        add(pauseButton,"wrap");
        add(loadButton);
        add(saveButton,"wrap");

    }
    public void setPlayerTurn(StatusCell color){
        playerText.setText("<html>Player <font color='"+color+"'>"+color+"</font> is your turn!</html>");

    }
    public void setTime(TimeGame time){
        timeText.setText(time.toString());
    }
    public void setWin(StatusCell color){
        playerText.setText("<html>Player <font color='"+color+"'>"+color+"</font> WON!</html>");

    }
}
