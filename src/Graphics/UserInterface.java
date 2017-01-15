package Graphics;

import GameLogic.Board;
import GameLogic.Match;

import javax.swing.*;

/**
 * Created by giogio on 1/12/17.
 */
public class UserInterface {

    MenuGUI menuGUI;




    GameGui gameGui;
    BoardGraphics graphics;
    JFrame mainFrame;

    public UserInterface(){
        mainFrame = new JFrame();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuGUI = new MenuGUI(mainFrame,this);
        mainFrame.add(menuGUI);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    public void openGame(){
        Match match = menuGUI.getMatch();
        graphics = new BoardGraphics(match);
        gameGui = new GameGui(graphics,match,this,mainFrame);
        mainFrame.remove(menuGUI);
        mainFrame.add(gameGui);
        mainFrame.pack();
        mainFrame.setVisible(true);
        match.startMatch();
    }
    public void goToSetting(){
        menuGUI = new MenuGUI(mainFrame,this);
        mainFrame.remove(gameGui);
        mainFrame.add(menuGUI);
        mainFrame.pack();
    }

    public GameGui getGameGui() {
        return gameGui;
    }

    public static void main(String[] args){
        new UserInterface();
    }
}
