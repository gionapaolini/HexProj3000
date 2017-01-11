package GameLogic.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 1/11/17.
 */
public class History {

    private List<RecordMove> records;

    public History(){
        records = new ArrayList<RecordMove>();
    }

    public void addRecord(RecordMove record){
        records.add(record);
    }

    public List<RecordMove> getRecords(){
        return records;
    }
}
