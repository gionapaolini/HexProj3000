package GameLogic.History;

import EnumVariables.StatusCell;

/**
 * Created by giogio on 1/11/17.
 */
public class RecordMove {
    private StatusCell color;
    private boolean status;
    private int x, y;

    public RecordMove(StatusCell color, int x, int y){
        this.color = color;
        this.x = x;
        this.y = y;
        status = true;
    }

    public void deleteMove(){
        status = false;
    }

    public String toString(){
        return color+" "+x+" "+y+" "+status;
    }

    public StatusCell getColor() {
        return color;
    }

    public boolean isStatus() {
        return status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
