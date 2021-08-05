package com.example.finalproject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import static com.example.finalproject.TimeHelper.findDifference;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "MyBroadcastReceiver";
    public static int TherIsInventation=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Integer integerTime = intent.getIntExtra("TimeRemaining", 0);
        if(integerTime!=0){
            TherIsInventation = 1;
        }
        else {
            TherIsInventation = 0;
        }

    }



}


