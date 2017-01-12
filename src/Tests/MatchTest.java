package Tests;

import EnumVariables.BotType;
import EnumVariables.GameType;
import EnumVariables.StatusCell;
import GameLogic.Match;
import Graphics.GameGui;
import Graphics.BoardGraphics;

/**
 * Created by giogio on 1/11/17.
 */
public class MatchTest {
    public static void main(String[] args){

        Match match = new Match(8, GameType.Multiplayer,true, StatusCell.Red, BotType.MCTS);
        BoardGraphics panel = new BoardGraphics(match);
        match.startMatch();
        GameGui gameGui = new GameGui(panel,match);
    }
}
