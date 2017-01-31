package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 4.1.2017..
 */

/**
 * Za pohranjianje odgovora web servisa
 */
public class Records {
    @SerializedName("record")
    @Expose
    private List<Record> record = new ArrayList<Record>();

    public Records() {
    }

    public Records(List<Record> record) {
        this.record = record;
    }

    public List<Record> getRecord() {
        return record;
    }

    public void setRecord(List<Record> record) {
        this.record = record;
    }
}
