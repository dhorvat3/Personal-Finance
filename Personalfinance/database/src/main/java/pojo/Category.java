package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import core.MainDatabase;

/**
 * Created by Filip on 29.12.2016..
 */

/**
 * Za pohranjianje odgovora web servisa
 */
@Table(database = MainDatabase.class)
public class Category extends BaseModel{
    @PrimaryKey (autoincrement = true)
    @Column
    private int localId;
    @Column
    @SerializedName("id")
    @Expose
    private String id;
    @Column
    @SerializedName("title")
    @Expose
    private String title;
    @Column
    @SerializedName("description")
    @Expose
    private String description;
    @Column
    @SerializedName("user_id")
    @Expose
    private String userId;
    @Column
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @Column
    @SerializedName("active")
    @Expose
    private String active;
    @Column(defaultValue = "0")
    private int edited;

    public Category() {
    }

    public Category(String id, String title, String description, String userId, String categoryId, String active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.categoryId = categoryId;
        this.active = active;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    //For spinner display items
    @Override
    public String toString(){
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getEdited() {
        return edited;
    }

    public void setEdited(int edited) {
        this.edited = edited;
    }
}
