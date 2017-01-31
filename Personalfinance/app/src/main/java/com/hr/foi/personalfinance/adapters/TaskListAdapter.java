package com.hr.foi.personalfinance.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hr.foi.personalfinance.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import pojo.Task;

/**
 * Created by Blaža on 28.1.2017..
 */

/**
 * Klasa TaskListAdapter za rad s korisnickim obvezama
 */
public class TaskListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Task> tasks;

    public TaskListAdapter(Context c, List<Task> t) {
        context = c;
        tasks = t;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(tasks.get(position).getId());
    }

    /**
     * Dohvacanje GUI elemenata i popunjavanje liste obaveza
     * @param position Indeks obaveze
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.task_item_layout, parent, false);

        TextView date = (TextView) view.findViewById(R.id.task_item_layout_date);
        TextView title = (TextView) view.findViewById(R.id.task_item_layout_title);
        TextView note = (TextView) view.findViewById(R.id.task_item_layout_note);
        TextView notice = (TextView) view.findViewById(R.id.task_item_layout_notice);

        Task task = (Task) getItem(position);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
        SimpleDateFormat noticeFormat = new SimpleDateFormat("dd.MM.yyyy. 'u' HH:mm");
        String taskDate = "";
        String noticeDate = "";

        try {
            taskDate = dateFormat.format(inputFormat.parse(task.getDate()));
            noticeDate = noticeFormat.format(inputFormat.parse(task.getNotice()));
        } catch (ParseException e) {
            Log.w("date-parser", e.getMessage());
        }

        date.setText(taskDate);
        title.setText(task.getTitle());
        note.setText(task.getNote());
        notice.append(noticeDate);

        return view;
    }
}
