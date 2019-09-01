package com.hawk.dilpreet.batteryoptimizer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {
    public static final String CHANNEL_1_ID="channel1";
    public static final String CHANNEL_2_ID="channelOngoing";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel lowBatteryChannel=new NotificationChannel(CHANNEL_1_ID,"Low battery", NotificationManager.IMPORTANCE_HIGH);
            lowBatteryChannel.setDescription("Low battery notification");

            NotificationChannel ongoignNotificationChannel=new NotificationChannel(CHANNEL_2_ID,"Ongoign Notification", NotificationManager.IMPORTANCE_LOW);
            ongoignNotificationChannel.setDescription("It helps to keep the app running at background and complete it's tasks");

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(lowBatteryChannel);
            manager.createNotificationChannel(ongoignNotificationChannel);
        }
    }
}
