package com.example.helpme.Extras;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.helpme.R;

public abstract class Notifications {

    public static void createNotificationChannel(Activity activity) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = activity.getString(R.string.channel_name);
            String description = activity.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Log.d(Constants.NOTIFICATION_LOG, "createNotificationChannel: notification channel created");
        }
    }

    public static void showNotification(Activity currentActivity, Intent intent, String title, String content){

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //check
        PendingIntent pendingIntent = PendingIntent.getActivity(currentActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = buildNotification(currentActivity, title, content, pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(currentActivity);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());

        Log.d(Constants.NOTIFICATION_LOG, "showNotification: notification showed");


    }

    private static NotificationCompat.Builder buildNotification(Activity activity, String title, String content, PendingIntent pendingIntent){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Log.d(Constants.NOTIFICATION_LOG, "buildNotification: notification builder created");

        return builder;

    }


}
