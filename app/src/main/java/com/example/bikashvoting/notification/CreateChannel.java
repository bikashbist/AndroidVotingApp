package com.example.bikashvoting.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class CreateChannel {
    Context context;
    public final static String CHANNEL_1="CHANNEL_1";
    public final static String CHANNEL_2="CHANNEL_2";

    public CreateChannel(Context context) {
        this.context = context;
    }


    public void createChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(
                    CHANNEL_1,
                    "CHANNEL 1",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("This is channel 1");

            NotificationChannel channel2=new NotificationChannel(
                    CHANNEL_2,
                    "CHANNEL 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is channel 2");


            NotificationManager notificationManager=context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }

}
