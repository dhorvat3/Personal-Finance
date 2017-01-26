package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dagy on 13.01.17..
 */

public class Task {
    @SerializedName("tasks")
    @Expose
    private List<Task_> tasks = null;

    public List<Task_> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task_> tasks) {
        this.tasks = tasks;
    }
}
