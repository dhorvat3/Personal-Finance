package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import core.MainDatabase;

/**
 * Created by Filip on 4.1.2017..
 */

/**
 * Za pohranjianje odgovora web servisa
 */
@Table(database = MainDatabase.class)
public class Record extends BaseModel{
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
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @Column
    @SerializedName("vrsta")
    @Expose
    private String vrsta;
    @Column
    @SerializedName("napomena")
    @Expose
    private String napomena;
    @Column
    @SerializedName("datum")
    @Expose
    private String datum;
    @Column
    @SerializedName("iznos")
    @Expose
    private String iznos;
    @Column
    @SerializedName("aktivan")
    @Expose
    private String aktivan;
    @Column(defaultValue = "0")
    private int edited;
    @Column
    @SerializedName("lastEdited")
    @Expose
    private String lastEdited;

    public Record() {
    }

    public Record(String id, String userId, String catgoryId, String vrsta, String napomena, String datum, String iznos, String aktivan) {
        this.id = id;
        this.userId = userId;
        this.categoryId = catgoryId;
        this.vrsta = vrsta;
        this.napomena = napomena;
        this.datum = datum;
        this.iznos = iznos;
        this.aktivan = aktivan;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
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

    public String getCatgoryId() {
        return categoryId;
    }

    public void setCatgoryId(String catgoryId) {
        this.categoryId = catgoryId;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getIznos() {
        return iznos;
    }

    public void setIznos(String iznos) {
        this.iznos = iznos;
    }

    public String getAktivan() {
        return aktivan;
    }

    public void setAktivan(String aktivan) {
        this.aktivan = aktivan;
    }

    public int getEdited() {
        return edited;
    }

    public void setEdited(int edited) {
        this.edited = edited;
    }

    public String getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(String lastEdited) {
        this.lastEdited = lastEdited;
    }
}
