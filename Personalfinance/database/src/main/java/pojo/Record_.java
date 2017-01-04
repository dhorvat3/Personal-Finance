package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Filip on 4.1.2017..
 */

public class Record_ {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("vrsta")
    @Expose
    private String vrsta;
    @SerializedName("napomena")
    @Expose
    private String napomena;
    @SerializedName("datum")
    @Expose
    private String datum;
    @SerializedName("iznos")
    @Expose
    private String iznos;
    @SerializedName("aktivan")
    @Expose
    private String aktivan;

    public Record_() {
    }

    public Record_(String id, String userId, String catgoryId, String vrsta, String napomena, String datum, String iznos, String aktivan) {
        this.id = id;
        this.userId = userId;
        this.categoryId = catgoryId;
        this.vrsta = vrsta;
        this.napomena = napomena;
        this.datum = datum;
        this.iznos = iznos;
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

}
