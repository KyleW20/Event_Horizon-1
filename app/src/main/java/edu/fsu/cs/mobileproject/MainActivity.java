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
import android.content.SharedPreferences;
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
import android.view.Window;
import android.widget.AdapterView;
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
    ArrayList<Uri> uriList;                     //holding all the uris for easy access to them in the database
    static final int DIALOG_EXIT_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Event Horizon");

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
        uriList = new ArrayList<Uri>();

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
            if(data != null)
            {
                ContentValues cal = (ContentValues) data.getParcelableExtra("CAL");
                Log.w("main", "Date: " + cal.get(CalendarContract.Events.DTSTART));
                Calendar begin = Calendar.getInstance();
                begin.set(Calendar.DAY_OF_MONTH, begin.get(Calendar.DAY_OF_MONTH) + 1);
                begin.set(Calendar.AM_PM, Calendar.AM);
                begin.set(Calendar.HOUR_OF_DAY, 0); //Initialize begin to the beginning of the next day
                begin.set(Calendar.MINUTE, 0);      //so we can check if it's today or not.
                begin.set(Calendar.SECOND, 0);

                allEvents.add(cal);

                Uri insertedUri = data.getParcelableExtra("URI");
                uriList.add(insertedUri);

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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.w("myApp", "onClick for future events.");

                getApplicationContext().getContentResolver().delete(uriList.get(i), null, null);
                ContentValues cal = allEvents.get(i);
                allEvents.remove(futureEventsAdapter.getItem(i));
                futureEvents.remove(i);
                uriList.remove(i);
                updateLists();

                //Open AddActivity fragment
                Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
                myIntent.putExtra("CAL", cal);
                MainActivity.this.startActivityForResult(myIntent, 1);
            }
        });


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
        todayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.w("myApp", "onClick for today events.");

                getApplicationContext().getContentResolver().delete(uriList.get(i), null, null);
                ContentValues cal = allEvents.get(i);
                allEvents.remove(todayEventsAdapter.getItem(i));
                todayEvents.remove(i);
                uriList.remove(i);
                updateLists();
                Toast.makeText(MainActivity.this, cal.get(CalendarContract.Events.DTSTART).toString(), Toast.LENGTH_SHORT).show();

                //Open AddActivity fragment
                Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
                myIntent.putExtra("CAL", cal);
                MainActivity.this.startActivityForResult(myIntent, 1);
            }
        });

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
