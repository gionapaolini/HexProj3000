package GameLogic;

import EnumVariables.StatusCell;

/**
 * Created by giogio on 1/11/17.
 */
public abstract class Player {
    protected StatusCell color;
    protected Match match;
    public void makeMove(int x, int y){
        match.putStone(x,y);

    }
    public StatusCell getColor(){
        return color;
    }
}
