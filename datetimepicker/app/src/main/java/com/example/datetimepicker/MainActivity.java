package com.example.datetimepicker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button dateSpinnerBtn, dateCalendarBtn, timeSpinnerBtn, timeClockBtn;
    TextView resultTextView;

    int year, month, day, hour, minute;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateSpinnerBtn = findViewById(R.id.dateSpinnerBtn);
        dateCalendarBtn = findViewById(R.id.dateCalendarBtn);
        timeSpinnerBtn = findViewById(R.id.timeSpinnerBtn);
        timeClockBtn = findViewById(R.id.timeClockBtn);
        resultTextView = findViewById(R.id.resultTextView);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Date Picker - Spinner mode
        dateSpinnerBtn.setOnClickListener(v -> {
            DatePickerDialog spinnerDialog = new DatePickerDialog(MainActivity.this,
                    android.R.style.Theme_Holo_Dialog,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        updateResult("Date (Spinner)");
                    }, year, month, day);
            spinnerDialog.getDatePicker().setCalendarViewShown(false); // Force spinner
            spinnerDialog.show();
        });

        // Date Picker - Calendar mode
        dateCalendarBtn.setOnClickListener(v -> {
            DatePickerDialog calendarDialog = new DatePickerDialog(MainActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        updateResult("Date (Calendar)");
                    }, year, month, day);
            calendarDialog.getDatePicker().setCalendarViewShown(true); // Force calendar
            calendarDialog.show();
        });

        // Time Picker - Spinner mode
        timeSpinnerBtn.setOnClickListener(v -> {
            TimePickerDialog spinnerTimeDialog = new TimePickerDialog(MainActivity.this,
                    android.R.style.Theme_Holo_Dialog,
                    (view, selectedHour, selectedMinute) -> {
                        hour = selectedHour;
                        minute = selectedMinute;
                        updateResult("Time (Spinner)");
                    }, hour, minute, false);
            spinnerTimeDialog.show();
        });

        // Time Picker - Clock mode (default)
        timeClockBtn.setOnClickListener(v -> {
            TimePickerDialog clockTimeDialog = new TimePickerDialog(MainActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        hour = selectedHour;
                        minute = selectedMinute;
                        updateResult("Time (Clock)");
                    }, hour, minute, false);
            clockTimeDialog.show();
        });
    }

    private void updateResult(String source) {
        String formattedDate = String.format("%02d/%02d/%04d", day, month + 1, year);
        String formattedTime = String.format("%02d:%02d", hour, minute);
        resultTextView.setText(source + ":\n" + "Date: " + formattedDate + "\nTime: " + formattedTime);
    }
}
