package Graphics;

import EnumVariables.GameType;
import EnumVariables.StatusCell;
import GameLogic.Match;
import GameLogic.TimeGame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private final JButton analisysButton = new JButton("Open Analysis window");
    private final JButton backButton = new JButton("Back to menu");
    private Match match;
    private UserInterface userInterface;

    public ControlPanel(Match match, UserInterface userInterface){
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
        add(analisysButton,"span");
        add(backButton,"span");
        this.match = match;
        this.userInterface = userInterface;
        setActionsButton();

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

    private void setActionsButton(){
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(match.getGameType() == GameType.Multiplayer)
                    match.undo();
                else if(match.getGameType() == GameType.HumanVsBot){
                    match.undo();
                    match.undo();
                }
                match.notifyImportant();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                match.pause();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                userInterface.goToSetting();
            }
        });
    }

}
