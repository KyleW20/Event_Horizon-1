package edu.fsu.cs.mobileproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSet;
    private DatePickerDialog.OnDateSetListener startDateSet;
    private TimePickerDialog.OnTimeSetListener startSet;
    private TimePickerDialog.OnTimeSetListener endSet;
    private TextView dText;
    private TextView startdText;
    private TextView sText;
    private TextView eText;
    private EditText title;
    private EditText desc;
    private Button alarm;
    int calYear = 0, year = 0, year2 = 0;
    int calMonth = 0, month = 0, month2 = 0;
    int calDay = 0, day = 0, day2 = 0;
    int hour = 0,hour2 = 0;
    int minute = 0, minute2 = 0;
    int check= 0;
    int timeArr[] = {12,1,2,3,4,5,6,7,8,9,10,11,12,1,2,3,4,5,6,7,8,9,10,11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Add Event");

        ContentValues currentCal = (ContentValues) getIntent().getParcelableExtra("CAL");


        dText = (TextView) findViewById(R.id.textView3);
        sText = (TextView)findViewById(R.id.textView4);
        eText = (TextView) findViewById(R.id.textView5);
        startdText = (TextView) findViewById(R.id.textView10);
        Button date = (Button) findViewById(R.id.dateButton);
        Button startDate = (Button) findViewById(R.id.startDateButton);
        Button start = (Button) findViewById(R.id.timeStart);
        Button end = (Button) findViewById(R.id.timeEnd);
        Button save = (Button) findViewById(R.id.saveButton);
        Button cancel = (Button) findViewById(R.id.cancelButton);
        alarm = (Button) findViewById(R.id.checkBox);
        title = (EditText) findViewById(R.id.editTextTextPersonName2);
        desc = (EditText) findViewById(R.id.editTextTextPersonName);

        Calendar cal = Calendar.getInstance();
        calYear = cal.get(Calendar.YEAR);
        calMonth = cal.get(Calendar.MONTH);
        calDay = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        if(currentCal != null)
        {
            Date populate = new Date((Long) currentCal.get(CalendarContract.Events.DTSTART));
            dText.setText(populate.getMonth() + "/" + populate.getDay() + "/" + populate.getYear());
            sText.setText(populate.getHours() + ":" + populate.getMinutes());
        }

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog datePicker = new DatePickerDialog(AddActivity.this, startDateSet, calYear, calMonth, calDay);
                datePicker.show();

            }
        });

        startDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                month1 = month1 + 1;
                year = year1;
                day = day1;
                month = month1 - 1;
                String pass = month1 + "/" + day1 + "/" + year1;
                startdText.setText(pass);
            }
        };

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog datePicker = new DatePickerDialog(AddActivity.this, dateSet, calYear, calMonth, calDay);
                datePicker.show();

            }
        });

        dateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                month1 = month1 + 1;
                if(year1 < year)
                    year1 = year;
                if(month1 < month)
                    month1 = month;
                if(day1 < day)
                    day1 = day;

                year2 = year1;
                day2 = day1;
                month2 = month1 - 1;

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
                int test = 0;
                if(hour1 >= 13 && hour1 < 24) {
                    /*if(hour1 == 24)
                        hour1 = timeArr[0];
                    else
                        hour1 = timeArr[hour1];*/
                    test = hour1 - 12;
                    time = " PM";
                }
                else{
                    test = hour1;
                    time = " AM";
                }

                if(minute1 < 10)
                {
                    pass = test + ":0" + minute1 + time;
                }
                else {
                    pass = test + ":" + minute1 + time;
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

                if((hour1 < hour || hour1 < (hour + 12)) && (minute1 <= minute)){
                    hour1 = hour + 1;
                }

                int test = 0;
                if (hour1 >= 13 && hour1 <= 24) {
                   /* if (hour1 == 24)
                        hour1 = timeArr[0];
                    else
                        hour1 = timeArr[hour1];*/
                    test = hour1 - 12;
                    time = " PM";
                } else {
                    test = hour1;
                    time = " AM";
                }

                if(minute1 < 10)
                {
                    pass = test + ":0" + minute1;
                }
                else {
                    pass = test + ":" + minute1 + time;
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
                endCal.set(year2,month2,day2,hour2,minute2);
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

                Uri insertedUri;
                insertedUri = getApplicationContext().getContentResolver().insert(baseUri, cal);

                Log.w("AddActivity.class", "want to add: " + cal.get(CalendarContract.Events.TITLE));
                Intent intent = new Intent();
                intent.putExtra("CAL", cal);
                intent.putExtra("URI", insertedUri);
                setResult(1, intent);
                AddActivity.this.finish();
            }
        });
    }
}