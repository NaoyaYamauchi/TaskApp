package com.example.na_sun.taskapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TaskAlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent ){
        Log.d("taskapp","onReceive");
    }
}
