package edu.fsu.cs.mobileproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class AddActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSet;
    private TimePickerDialog.OnTimeSetListener startSet;
    private TimePickerDialog.OnTimeSetListener endSet;
    private TextView dText;
    private TextView sText;
    private TextView eText;
    private EditText title;
    private EditText desc;
    private Button alarm;
    int year = 0;
    int month = 0;
    int day = 0;
    int hour = 0,hour2 = 0;
    int minute = 0, minute2 = 0;
    int check= 0;
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
        Button save = (Button) findViewById(R.id.saveButton);
        Button cancel = (Button) findViewById(R.id.cancelButton);
        alarm = (Button) findViewById(R.id.checkBox);
        title = (EditText) findViewById(R.id.editTextTextPersonName2);
        desc = (EditText) findViewById(R.id.editTextTextPersonName);

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
                if(hour1 < hour || hour1 < (hour + 12)) {
                    hour1 = hour + 1;
                }

                if (hour1 >= 13 && hour1 <= 24) {
                    if (hour1 == 24)
                        hour1 = timeArr[0];
                    else
                        hour1 = timeArr[hour1];
                    time = " PM";
                } else {
                    time = " AM";
                }

                if(minute1 < 10)
                {
                    pass = hour1 + ":0" + minute1;
                }
                else {
                    pass = hour1 + ":" + minute1 + time;
                }
                hour2 = hour1;
                minute2 = minute1;
                eText.setText(pass);
            }
        };

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddActivity.this.finish();
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                check = 1;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar begin = Calendar.getInstance();
                ContentValues cal = new ContentValues();
                cal.put(CalendarContract.Events.CALENDAR_ID, 1);
                cal.put(CalendarContract.Events.TITLE, title.getText().toString());
                cal.put(CalendarContract.Events.DESCRIPTION, desc.getText().toString());
                cal.put(CalendarContract.Events.EVENT_LOCATION, "Unknown");
                begin.set(year,month,day,hour,minute);
                Calendar endCal = Calendar.getInstance();
                endCal.set(year,month,day,hour2,minute2);
                cal.put(CalendarContract.Events.DTSTART, begin.getTimeInMillis());
                cal.put(CalendarContract.Events.DTEND, endCal.getTimeInMillis());
                cal.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
                cal.put(CalendarContract.Events.HAS_ALARM, check); // 0 for false, 1 for true

                String timeZone = TimeZone.getDefault().getID();
                cal.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

                Uri baseUri;
                if (Build.VERSION.SDK_INT >= 8) {
                    baseUri = Uri.parse("content://com.android.calendar/events");
                } else {
                    baseUri = Uri.parse("content://calendar/events");
                }
                getApplicationContext().getContentResolver().insert(baseUri, cal);

                Log.w("myApp", "want to add: " + cal.get(CalendarContract.Events.TITLE));
                AddActivity.this.finish();
            }
        });
    }
}