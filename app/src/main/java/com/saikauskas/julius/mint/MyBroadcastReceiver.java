package com.saikauskas.julius.mint;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.saikauskas.julius.mint.App.CHANNEL_1_ID;

public class MyBroadcastReceiver extends BroadcastReceiver {

   public static NotificationManagerCompat notificationManagerCompat;
   String title;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManagerCompat = NotificationManagerCompat.from(context);


        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(context, CHANNEL_1_ID)
                    .setContentTitle("Mint")
                    .setContentText(AddReminderActivity.mTitle)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setOngoing(true)
                    .build();

            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManagerCompat.notify(255, notification);


        }
        else {
            Notification noti = new Notification.Builder(context)
                    .setContentTitle("Mint")
                    .setContentText("You have a notification!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .build();
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            noti.flags = Notification.FLAG_AUTO_CANCEL;
            manager.notify(555, noti);
        }


    }
}
