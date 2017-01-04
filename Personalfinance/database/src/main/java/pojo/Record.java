package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 4.1.2017..
 */

public class Record {
    @SerializedName("record")
    @Expose
    private List<Record_> record = new ArrayList<Record_>();

    public Record() {
    }

    public Record(List<Record_> record) {
        this.record = record;
    }

    public List<Record_> getRecord() {
        return record;
    }

    public void setRecord(List<Record_> record) {
        this.record = record;
    }
}
