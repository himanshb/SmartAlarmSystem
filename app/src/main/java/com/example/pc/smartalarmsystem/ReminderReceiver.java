package com.example.pc.smartalarmsystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;


public class ReminderReceiver extends BroadcastReceiver {

    String description,title;
    @Override
    public void onReceive(Context context, Intent intent) {

        description=intent.getExtras().getString("description");
        title=intent.getExtras().getString("title");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.time_feed)
                        .setContentTitle("Reminder")
                        .setContentText(title);

        Intent notificationIntent = new Intent(context, Reminder2Activity.class);
        notificationIntent.putExtra("title",title);
        notificationIntent.putExtra("description",description);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=builder.build();
        notification.defaults|=Notification.DEFAULT_SOUND;
        notification.defaults|=Notification.DEFAULT_VIBRATE;
        manager.notify(0, notification);
    }

}
