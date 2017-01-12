package GameLogic;

/**
 * Created by giogio on 1/12/17.
 */
public class TimeGame {
    int s,m,h;

    public void increment(){
        s++;
        if(s==60){
            s=0;
            m++;
            if(m==60){
                m=0;
                h++;
            }
        }
    }

    public String toString(){
        return h+":"+m+":"+s;
    }
}
