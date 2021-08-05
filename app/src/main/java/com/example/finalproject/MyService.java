package com.example.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.finalproject.FileHelper.*;
import static com.example.finalproject.TimeHelper.*;

public class MyService extends Service {

    private static final String CHANNEL_ID = "NotificationChannelID";
    public static final String FOREGROUND_PROGRESS = "com.example.finalProject.finalproject";
    private MyWorker worker;
    private Date mydate;
    private static int num =5;
    public final static int NOTIFICATION_ID1 = 1;
    Notification.Builder notificationBuilder;
    static NotificationManager notificationManager;

    public MyService() {
    }

    @Override
    public void onCreate() {
        initService();
        super.onCreate();
    }

    private void initService() {
        String CHANNEL_ID = "my_channel_1";
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "my main channel", notificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Hola")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent);
        startForeground(NOTIFICATION_ID1, updateNotification(Integer.toString(0)));
    }

    public Notification updateNotification(String details) {
        notificationBuilder.setContentText(details).setOnlyAlertOnce(false);//notice
        Notification noti = notificationBuilder.build();
        noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(NOTIFICATION_ID1, noti);
        return noti;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // happens every time the app starts
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.worker == null) {
            this.worker = new MyWorker();
            worker.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyWorker extends Thread {
        Intent intent1local = new Intent(FOREGROUND_PROGRESS);
        @Override
        public void run() {
            intent1local.setAction("Counter");
            Date rnow = new Date();
            Date d = FindStartDate();//function to get the next vacation start date from raw file
            if(d==null){

                return;
            }
                findDifference(rnow, d);

            try {
                while (d != null  ) {
                    timeMi= timeMi -1000;
                   // System.out.println("timeMi "+timeMi);

                    Thread.sleep(1000);
                    //System.out.println("num is" + num);

                    if (timeMi == 0) {
                       // System.out.println("timeRemaining is zero " + timeMi/1000);
                        updateNotification("Your vacation start now");
                        intent1local.putExtra("TimeRemaining", 0);//put this to the broadcast
                        sendBroadcast(intent1local);///send to the broadcast
                        d = FindStartDate();
                        findDifference(rnow, d);
                        //stopSelf();
                    } else {

                          updateNotification("your time left: " +intToString((int) timeMi/1000/3600)+":"+intToString((int) ((timeMi / (1000*60)) % 60))+":"+intToString((int) (timeMi / 1000) % 60 ));

                        intent1local.putExtra("TimeRemaining", 5);//put this to the broadcast
                        sendBroadcast(intent1local);///send to the broadcast

                    }


                }//end run
                stopSelf();     //Stop the service, if it was previously started.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.run();


        }
    }

    private String intToString(int num) {           //function for timer String
        if (num < 10) { //if we want to show one dighit (it means zero in the left)
            return "0" + num;
        }
        return num + "";
    }
}
