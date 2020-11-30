package edu.fsu.cs.mobileproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    AlarmReceiver receiver;
    ListView lv;
    ListView todayList;
    ArrayList<String> todayEvents;
    ArrayList<String> futureEvents;
    ArrayAdapter<String> futureEventsAdapter;
    ArrayAdapter<String> todayEventsAdapter;
    ArrayList<ContentValues> allEvents;         //stores all the data for upcoming events
    static final int DIALOG_EXIT_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CALENDAR},
                0);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_CALENDAR},
                1);

        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivityForResult(myIntent, 2);

        registerReceiver(receiver, new IntentFilter("TIME"));

        allEvents = new ArrayList<ContentValues>();

        lv = (ListView) findViewById(R.id.listView);
        todayList = (ListView) findViewById(R.id.listView2);


        futureEvents = new ArrayList<String>();
        final ArrayAdapter<String> futureEventsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, futureEvents) {
            //had to override getView function because it kept creating white text instead.
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };

        todayEvents = new ArrayList<String>();
        final ArrayAdapter<String> todayEventsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, todayEvents){
            //had to override getView function because it kept creating white text instead.
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };

        lv.setAdapter(futureEventsAdapter);
        todayList.setAdapter(todayEventsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w("myApp", "onResume");

        Calendar now = Calendar.getInstance();
        for(int i = 0; i < allEvents.size(); i++)
        {
            // If the event has ended, pop it from the appropriate lists
            if((Long)allEvents.get(i).get(CalendarContract.Events.DTEND) < now.getTimeInMillis())
            {
                Log.w("myApp", "an event has finished!");
                // Search for the event to delete in future events.
                int toDelete = futureEvents.indexOf((String)allEvents.get(i).get(CalendarContract.Events.TITLE));
                if(toDelete != -1)
                {
                    Log.w("myApp", "deleting from futureevents");
                    futureEvents.remove(toDelete);
                }

                toDelete = todayEvents.indexOf((String)allEvents.get(i).get(CalendarContract.Events.TITLE));
                if(toDelete != -1)
                {
                    Log.w("myApp", "deleting from todayevents");
                    todayEvents.remove(toDelete);
                }

                allEvents.remove(i);
            }
        }

        updateLists();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
                MainActivity.this.startActivityForResult(myIntent, 1);
                showDialog(1);

                Log.w("myApp", "onClick");
/*
                Calendar begin = Calendar.getInstance();
                ContentValues cal = new ContentValues();
                cal.put(CalendarContract.Events.CALENDAR_ID, 1);
                cal.put(CalendarContract.Events.TITLE, "Test");
                cal.put(CalendarContract.Events.DESCRIPTION, "Testing");
                cal.put(CalendarContract.Events.EVENT_LOCATION, "Who knows");

                cal.put(CalendarContract.Events.DTSTART, begin.getTimeInMillis());
                cal.put(CalendarContract.Events.DTEND, begin.getTimeInMillis());
                cal.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
                cal.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true

                String timeZone = TimeZone.getDefault().getID();
                cal.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

                Uri baseUri;
                if (Build.VERSION.SDK_INT >= 8) {
                    baseUri = Uri.parse("content://com.android.calendar/events");
                } else {
                    baseUri = Uri.parse("content://calendar/events");
                }

                Log.w("myApp", "adding from onOptionsItemSelected" + cal.get(CalendarContract.Events.TITLE));
                getApplicationContext().getContentResolver().insert(baseUri, cal);

                Log.w("myApp", "want to add: " + cal.get(CalendarContract.Events.TITLE));
                addToList((String) cal.get(CalendarContract.Events.TITLE));
*/


                //LEAVE THE 3 LINES UNDERNEATH THIS NO MATTER WHAT. I MAY NEED IT. - KYLE
//                Intent intent = new Intent(Intent.ACTION_INSERT)
//                       .setData(CalendarContract.Events.CONTENT_URI);
//                startActivity(intent);
                /*AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
                                Calendar calendar = Calendar.getInstance(); //The time set in the dialog
                                /*calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                                calendar.set(Calendar.SECOND, 0);
                                if(timePicker.getHour() >= 12)
                                    calendar.set(Calendar.AM_PM,Calendar.PM);
                                else
                                    calendar.set(Calendar.AM_PM,Calendar.AM);
                                calendar.set(Calendar.MONTH, datePicker.getMonth());
                                calendar.set(Calendar.YEAR, datePicker.getYear());
                                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                                Calendar calendar2 = Calendar.getInstance();    //current time
                                Log.w(getClass().getName(), ((Long)calendar.getTimeInMillis()).toString());
                                Log.w(getClass().getName(), ((Long)calendar2.getTimeInMillis()).toString());

                                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                                //Intent intent = new Intent("TIME");
                                //Pending intent is passed to alarm manager. When alarm manager reaches the calendar, it will trigger the intent
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                               // Log.w(getClass().getName(), "Waiting for: " + alarmManager.getNextAlarmClock().getTriggerTime());
*//*

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/

                break;

            case R.id.refresh:
                onResume();
                break;

            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

    // This gets called when we setResult from the AddActivity function
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("Main", "Activity Result");
        if (requestCode == 1) {
            ContentValues cal = (ContentValues) data.getParcelableExtra("CAL");
            Log.w("main", "Date: " + cal.get(CalendarContract.Events.DTSTART));
            Calendar begin = Calendar.getInstance();
            begin.set(Calendar.DAY_OF_MONTH, begin.get(Calendar.DAY_OF_MONTH) + 1);
            begin.set(Calendar.AM_PM, Calendar.AM);
            begin.set(Calendar.HOUR_OF_DAY, 0); //Initialize begin to the beginning of the next day
            begin.set(Calendar.MINUTE, 0);      //so we can check if it's today or not.
            begin.set(Calendar.SECOND, 0);

            allEvents.add(cal);
            if ((Long)cal.get(CalendarContract.Events.DTSTART) < begin.getTimeInMillis()) {
                Log.w("main", "Today " + cal.get(CalendarContract.Events.DTSTART));
                todayEvents.add((String) cal.get(CalendarContract.Events.TITLE));
            }
            else {
                Log.w("main", "Tomorrow " + cal.get(CalendarContract.Events.DTSTART));
                futureEvents.add((String) cal.get(CalendarContract.Events.TITLE));
            }

            updateLists();
        }
    }

    public void updateLists()
    {
        futureEventsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, futureEvents) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        lv.setAdapter(futureEventsAdapter);

        todayEventsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, todayEvents) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        todayList.setAdapter(todayEventsAdapter);
    }


    /*
    public void addToList(ArrayList<String> list, String event) {
        //Toast.makeText(getActivity().getApplicationContext(), urls[0], Toast.LENGTH_SHORT).show();
        list.add(event);
        futureEventsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, events){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        lv.setAdapter(futureEventsAdapter);
    }
*/
}
