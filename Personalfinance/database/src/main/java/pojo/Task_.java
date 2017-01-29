package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import core.MainDatabase;

/**
 * Created by dagy on 13.01.17..
 */
@Table(database = MainDatabase.class)
public class Task_ extends BaseModel{
    @PrimaryKey (autoincrement = true)
    @Column
    private int localId;
    @Column
    @SerializedName("id")
    @Expose
    private String id;
    @Column
    @SerializedName("user_id")
    @Expose
    private String userId;
    @Column
    @SerializedName("title")
    @Expose
    private String title;
    @Column
    @SerializedName("note")
    @Expose
    private String note;
    @Column
    @SerializedName("date")
    @Expose
    private String date;
    @Column
    @SerializedName("notice")
    @Expose
    private String notice;
    @Column
    @SerializedName("aktivan")
    @Expose
    private String aktivan;

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getAktivan() {
        return aktivan;
    }

    public void setAktivan(String aktivan) {
        this.aktivan = aktivan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
