package Tests;

import EnumVariables.BotType;
import EnumVariables.GameType;
import EnumVariables.StatusCell;
import GameLogic.Match;

import Graphics.BoardGraphics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by giogio on 1/11/17.
 */
public class MatchTest {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(500,500));
        frame.setVisible(true);
        Match match = new Match(5, GameType.HumanVsBot,false, StatusCell.Red, BotType.MCTS);
        BoardGraphics panel = new BoardGraphics(match,frame);
        match.addObserver(panel);
        match.startMatch();
        frame.add(panel);
    }
}
