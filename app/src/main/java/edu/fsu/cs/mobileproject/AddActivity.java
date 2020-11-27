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
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSet;
    private TextView dText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //TimePickerDialog timePicker = new TimePickerDialog();

        dText = (TextView) findViewById(R.id.textView3);
        Button date = (Button) findViewById(R.id.dateButton);
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog datePicker = new DatePickerDialog(AddActivity.this, dateSet, year, month, day);
                datePicker.show();

            }
        });

        dateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String pass = month + "/" + day + "/" + year;
                dText.setText(pass);
            }
        };
    }
}