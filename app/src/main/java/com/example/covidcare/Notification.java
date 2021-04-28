package com.example.covidcare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.INotificationSideChannel;

import androidx.core.app.NotificationCompat;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class Notification extends BroadcastReceiver {
    private static final String CHANNEL_ID = "Senior_Capstone_Team_Covid_Care";


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context, NotificationTapAction.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationTapAction.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_mask)
                .setContentTitle("Covid Exposed Area Alert!!!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("You've been found near by exposed area. Please wear mask. STAY SAFE AND BE SMART. "))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());

    }


}
