package com.hr.foi.personalfinance.fragments;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.hr.foi.personalfinance.receivers.AlarmReceiver;

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
public class TaskAdd extends Fragment implements DataInterface {

    private SharedPreferences prefs;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat displayDateFormat;
    private SimpleDateFormat displayTimeFormat;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat noticeFormat;
    private SimpleDateFormat outputFormat;
    private EditText newTaskDate;
    private EditText newTaskNotice;
    private Calendar notificationCalendar = Calendar.getInstance();
    private String notificationTitle;
    private String notificationMessage;
    private String notificationDate;
    private DataBuilder dataBuilder = new DataBuilder(this);
    private Task_ task = new Task_();

    public TaskAdd() {
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
        getActivity().getActionBar().setTitle("Nova obveza");

        prefs = getActivity().getSharedPreferences("login", 0);
        displayDateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        displayTimeFormat = new SimpleDateFormat("HH:mm");
        dateFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
        noticeFormat = new SimpleDateFormat("'Obavijesti me 'dd.MM.yyyy.' u 'HH:mm");
        outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        View view = inflater.inflate(R.layout.task_add_layout, container, false);

        return view;
    }

    /**
     * Used for setting values after view is created.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        newTaskDate = (EditText) view.findViewById(R.id.new_task_date);
        newTaskNotice = (EditText) view.findViewById(R.id.new_task_notice);

        newTaskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog("date");
                }
            }
        });

        newTaskNotice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog("notice");
                }
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.new_task_cancel_button);
        Button submitButton = (Button) view.findViewById(R.id.new_task_submit_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newTaskTitle = (EditText) view.findViewById(R.id.new_task_title);
                EditText newTaskNote = (EditText) view.findViewById(R.id.new_task_note);
                boolean valid = true;

                List<EditText> fields = Arrays.asList(newTaskTitle, newTaskNote, newTaskDate, newTaskNotice);

                for (Iterator<EditText> i = fields.iterator(); i.hasNext();) {
                    EditText field = i.next();

                    if (field.getText().toString().isEmpty()) {
                        field.setError("Obavezno polje");

                        valid = false;
                    }
                }

                if (valid) {
                    Date noticeDate = new Date();

                    task.setUserId(prefs.getString("id", ""));
                    task.setTitle(newTaskTitle.getText().toString());
                    task.setNote(newTaskNote.getText().toString());

                    try {
                        noticeDate = noticeFormat.parse(newTaskNotice.getText().toString() + ":00");

                        task.setDate(outputFormat.format(dateFormat.parse(newTaskDate.getText().toString())) + ":00");
                        task.setNotice(outputFormat.format(noticeDate));
                    } catch (ParseException e) {
                        Log.w("date-parser", e.getMessage());
                    }

                    notificationCalendar.setTime(noticeDate);
                    notificationTitle = task.getTitle();
                    notificationMessage = task.getNote();
                    notificationDate = newTaskDate.getText().toString();
                    dataBuilder.newTask(task);
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void buildData(Object data) {
        Response response = (Response) data;

        if (Integer.parseInt(response.getId()) > -1) {
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            intent.putExtra("notificationTitle", notificationTitle);
            intent.putExtra("notificationMessage", notificationMessage);
            intent.putExtra("notificationDate", notificationDate);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), Integer.parseInt(response.getId()), intent, PendingIntent.FLAG_ONE_SHOT);

            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationCalendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(getActivity(), "Uspješna pohrana", Toast.LENGTH_SHORT).show();

            getFragmentManager().popBackStack();
        } else {
            switch (response.getId()) {
                case "-1":
                    Toast.makeText(getActivity(), "Pogreška", Toast.LENGTH_SHORT).show();
                    break;
                case "-2":
                    Toast.makeText(getActivity(), "Prazno polje", Toast.LENGTH_SHORT).show();
                    break;
            }
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
                    newTaskDate.setText(formattedDate);
                } else if (field.equals("notice")) {
                    newTaskNotice.setText("Obavijesti me " + formattedDate);
                }

                showTimePickerDialog(field);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (field.equals("date")) {
                    newTaskDate.setText("");
                } else if (field.equals("notice")) {
                    newTaskNotice.setText("");
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
                    newTaskDate.append(formattedTime);
                } else if (field.equals("notice")) {
                    newTaskNotice.append(" u" + formattedTime);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (field.equals("date")) {
                    newTaskDate.setText("");
                } else if (field.equals("notice")) {
                    newTaskNotice.setText("");
                }
            }
        });

        timePickerDialog.show();
    }
}
