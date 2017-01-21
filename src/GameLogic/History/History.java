package GameLogic.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 1/11/17.
 */
public class History {

    private ArrayList<RecordMove> records;

    public History(){
        records = new ArrayList<RecordMove>();
    }

    public void addRecord(RecordMove record){
        records.add(record);
    }

    public ArrayList<RecordMove> getRecords(){
        return records;
    }
    public int getNValidRecords(){
        int count=0;
        for (RecordMove recordMove: records){
            if(recordMove.isStatus())
                count++;
        }
        return count;
    }
    public RecordMove getLastValidRec(){
        for (int i=records.size()-1;i>=0;i--){
            if(records.get(i).isStatus())
                return records.get(i);
        }
        return null;
    }
}
