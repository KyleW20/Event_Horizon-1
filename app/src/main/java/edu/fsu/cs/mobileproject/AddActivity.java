package edu.fsu.cs.mobileproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSet;
    private TimePickerDialog.OnTimeSetListener startSet;
    private TimePickerDialog.OnTimeSetListener endSet;
    private TextView dText;
    private TextView sText;
    private TextView eText;
    int year = 0;
    int month = 0;
    int day = 0;
    int hour = 0;
    int minute = 0;
    int timeArr[] = {12,1,2,3,4,5,6,7,8,9,10,11,12,1,2,3,4,5,6,7,8,9,10,11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dText = (TextView) findViewById(R.id.textView3);
        sText = (TextView)findViewById(R.id.textView4);
        eText = (TextView) findViewById(R.id.textView5);
        Button date = (Button) findViewById(R.id.dateButton);
        Button start = (Button) findViewById(R.id.timeStart);
        Button end = (Button) findViewById(R.id.timeEnd);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog datePicker = new DatePickerDialog(AddActivity.this, dateSet, year, month, day);
                datePicker.show();

            }
        });

        dateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                month1 = month1 + 1;
                year = year1;
                day = day1;
                month = month1;
                String pass = month1 + "/" + day1 + "/" + year1;
                dText.setText(pass);
            }
        };

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(AddActivity.this, startSet, hour, minute, false);
                timePicker.show();
            }
        });

        startSet = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour1, int minute1) {
                String time, pass;
                if(hour1 >= 13 && hour1 <= 24) {
                    if(hour1 == 24)
                        hour1 = timeArr[0];
                    else
                        hour1 = timeArr[hour1];
                    time = " PM";
                }
                else{
                    time = " AM";
                }

                if(minute1 < 10)
                {
                    pass = hour1 + ":0" + minute1;
                }
                else {
                    pass = hour1 + ":" + minute1 + time;
                }
                hour = hour1;
                minute = minute1;
                sText.setText(pass);
            }
        };

        end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(AddActivity.this, endSet, hour, minute, false);
                timePicker.show();
            }
        });

        endSet = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour1, int minute1) {
                String time, pass;
                if(hour1 < hour || hour1 < (hour - 12)) {
                    hour1 = hour + 1;
                    time = "";
                }
                else {
                    if (hour1 >= 13 && hour1 <= 24) {
                        if (hour1 == 24)
                            hour1 = timeArr[0];
                        else
                            hour1 = timeArr[hour1];
                        time = " PM";
                    } else {
                        time = " AM";
                    }
                }
                if(minute1 < 10)
                {
                    pass = hour1 + ":0" + minute1;
                }
                else {
                    pass = hour1 + ":" + minute1 + time;
                }
                hour = hour1;
                minute = minute1;
                eText.setText(pass);
            }
        };

    }
}