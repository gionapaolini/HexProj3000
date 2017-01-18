package EnumVariables;

/**
 * Created by giogio on 1/11/17.
 */
public enum StatusCell {
    Empty(0), Red(-1), Blue(1);

    private final int status;



    StatusCell(int i) {
        status = i;
    }

    public int getInt() {
        return status;
    }

    public StatusCell opposite(){
        if (status ==-1) return Blue;
        if (status ==1) return Red;
        return Empty;
    }
}
