package com.hr.foi.personalfinance.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hr.foi.personalfinance.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import core.DataBuilder;
import core.DataInterface;
import pojo.Response;
import pojo.Task_;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskEdit extends Fragment implements DataInterface {

    private SharedPreferences prefs;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat displayDateFormat;
    private SimpleDateFormat displayTimeFormat;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat noticeFormat;
    private SimpleDateFormat outputFormat;
    private EditText editTaskDate;
    private EditText editTaskNotice;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private Task_ task = new Task_();

    public TaskEdit() {
        // Required empty public constructor
    }

    /**
     * Used for setting values while creating view.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("Uređivanje obveze");

        prefs = getActivity().getSharedPreferences("login", 0);
        displayDateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        displayTimeFormat = new SimpleDateFormat("HH:mm");
        dateFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
        noticeFormat = new SimpleDateFormat("'Obavijesti me 'dd.MM.yyyy.' u 'HH:mm");
        outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        View view = inflater.inflate(R.layout.task_edit_layout, container, false);
        EditText taskTitle = (EditText) view.findViewById(R.id.edit_task_title);
        EditText taskNote = (EditText) view.findViewById(R.id.edit_task_note);
        EditText taskDate = (EditText) view.findViewById(R.id.edit_task_date);
        EditText taskNotice = (EditText) view.findViewById(R.id.edit_task_notice);

        taskTitle.setText(prefs.getString("edit-task-title", ""));
        taskNote.setText(prefs.getString("edit-task-note", ""));
        taskDate.setText(prefs.getString("edit-task-date", ""));
        taskNotice.setText(prefs.getString("edit-task-notice", ""));

        return view;
    }

    /**
     * Used for setting values after view is created.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        editTaskDate = (EditText) view.findViewById(R.id.edit_task_date);
        editTaskNotice = (EditText) view.findViewById(R.id.edit_task_notice);

        editTaskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog("date");
                }
            }
        });

        editTaskNotice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog("notice");
                }
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.edit_task_cancel_button);
        Button submitButton = (Button) view.findViewById(R.id.edit_task_submit_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTaskTitle = (EditText) view.findViewById(R.id.edit_task_title);
                EditText editTaskNote = (EditText) view.findViewById(R.id.edit_task_note);
                boolean valid = true;

                List<EditText> fields = Arrays.asList(editTaskTitle, editTaskNote, editTaskDate, editTaskNotice);

                for (Iterator<EditText> i = fields.iterator(); i.hasNext();) {
                    EditText field = i.next();

                    if (field.getText().toString().isEmpty()) {
                        field.setError("Obavezno polje");

                        valid = false;
                    }
                }

                if (valid) {
                    task.setId(prefs.getString("edit-task-id", ""));
                    task.setTitle(editTaskTitle.getText().toString());
                    task.setNote(editTaskNote.getText().toString());

                    try {
                        task.setDate(outputFormat.format(dateFormat.parse(editTaskDate.getText().toString())) + ":00");
                        task.setNotice(outputFormat.format(noticeFormat.parse(editTaskNotice.getText().toString() + ":00")));
                    } catch (ParseException e) {
                        Log.w("date-parser", e.getMessage());
                    }

                    dataBuilder.editTask(task);
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void buildData(Object data) {
        Response response = (Response) data;

        switch (response.getId()) {
            case "1":
                SharedPreferences.Editor editor = prefs.edit();

                editor.remove("edit-task-id");
                editor.remove("edit-task-title");
                editor.remove("edit-task-note");
                editor.remove("edit-task-date");
                editor.remove("edit-task-notice");
                editor.commit();

                Toast.makeText(getActivity(), "Uspješna izmjena", Toast.LENGTH_SHORT).show();

                getFragmentManager().popBackStack();
                break;
            case "-1":
                Toast.makeText(getActivity(), "Pogreška", Toast.LENGTH_SHORT).show();
                break;
            case "-2":
                Toast.makeText(getActivity(), "Prazno polje", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showDatePickerDialog(final String field) {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, month, dayOfMonth);

                String formattedDate = displayDateFormat.format(date.getTime());

                if (field.equals("date")) {
                    editTaskDate.setText(formattedDate);
                } else if (field.equals("notice")) {
                    editTaskNotice.setText("Obavijesti me " + formattedDate);
                }

                showTimePickerDialog(field);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (field.equals("date")) {
                    editTaskDate.setText("");
                } else if (field.equals("notice")) {
                    editTaskNotice.setText("");
                }
            }
        });

        datePickerDialog.show();
    }

    private void showTimePickerDialog(final String field) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar time = Calendar.getInstance();
                time.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, hourOfDay, minute);

                String formattedTime = " " + displayTimeFormat.format(time.getTime());

                if (field.equals("date")) {
                    editTaskDate.append(formattedTime);
                } else if (field.equals("notice")) {
                    editTaskNotice.append(" u" + formattedTime);
                }
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (field.equals("date")) {
                    editTaskDate.setText("");
                } else if (field.equals("notice")) {
                    editTaskNotice.setText("");
                }
            }
        });

        timePickerDialog.show();
    }
}
