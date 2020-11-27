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
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //TimePickerDialog timePicker = new TimePickerDialog();
        final int[] year = {0};
        final int[] month = {0};
        final int[] day = {0};

        Button date = (Button) findViewById(R.id.dateButton);
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog datePicker = new DatePickerDialog(AddActivity.this);
                datePicker.show();
                
            }
        });
    }
}