package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 3.1.2017..
 */

public class Categories {
    @SerializedName("category")
    @Expose
    private List<Category> category = new ArrayList<Category>();

    public Categories() {
    }

    public Categories(List<Category> category) {
        this.category = category;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }
}
