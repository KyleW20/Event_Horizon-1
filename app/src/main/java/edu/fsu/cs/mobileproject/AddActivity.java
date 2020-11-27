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
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);
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
                
                String pass = hour1 + ":" + minute1;
                hour = hour1;
                minute = minute1;
                sText.setText(pass);
            }
        };


    }
}