package edu.fsu.cs.mobileproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.StreamCorruptedException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton add = (ImageButton) findViewById(R.id.imageButton);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                ScrollView scrollView = new ScrollView(MainActivity.this);
                LinearLayout lp = new LinearLayout(MainActivity.this);
                lp.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 20, 30, 0);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params2.setMargins(20, 10, 30, 0);

                final TextView title = new TextView(MainActivity.this);
                title.setText("Event Name");
                lp.addView(title, params);
                final EditText name = new EditText(MainActivity.this);
                lp.addView(name, params2);
                final TextView date = new TextView(MainActivity.this);
                date.setText("Time of Event");
                lp.addView(date, params2);
                final TimePicker timePicker = new TimePicker(MainActivity.this);
                lp.addView(timePicker, params2);
                final DatePicker datePicker = new DatePicker(MainActivity.this);
                lp.addView(datePicker, params2);
                //final EditText time = new EditText(MainActivity.this);
                //lp.addView(time, params2);
                scrollView.addView(lp);
                alertDialog.setView(scrollView);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }
}