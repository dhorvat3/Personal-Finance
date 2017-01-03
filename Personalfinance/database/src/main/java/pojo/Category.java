package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 3.1.2017..
 */

public class Category {
    @SerializedName("category")
    @Expose
    private List<Category_> category = new ArrayList<Category_>();

    public Category() {
    }

    public Category(List<Category_> category) {
        this.category = category;
    }

    public List<Category_> getCategory() {
        return category;
    }

    public void setCategory(List<Category_> category) {
        this.category = category;
    }
}
