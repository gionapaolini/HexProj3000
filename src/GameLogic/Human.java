package GameLogic;

import EnumVariables.StatusCell;

/**
 * Created by giogio on 1/11/17.
 */
public class Human extends Player{
    public Human(StatusCell color, Match match){
        this.color = color;
        this.match = match;
    }
    @Override
    public void makeMove(int x, int y) {
        super.makeMove(x, y);
    }
}
