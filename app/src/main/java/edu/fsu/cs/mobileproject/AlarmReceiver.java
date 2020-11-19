package edu.fsu.cs.mobileproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Time reached.", Toast.LENGTH_SHORT).show();
        Log.w(getClass().getName(), "Time reached.");
    }

}
