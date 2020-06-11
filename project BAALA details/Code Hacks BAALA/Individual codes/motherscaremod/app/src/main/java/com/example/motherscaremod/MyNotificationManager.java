package com.example.motherscaremod;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;


public class MyNotificationManager
{

    static void showNotification(@NonNull Context context)
    {
        context = context.getApplicationContext();

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                Math.abs(new Random().nextInt()) /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "MCMNoti";
        String channelName = "MothersCaremodNotification";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)
                )
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setContentTitle("New data received!")
                .setContentText("Tap to view")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }

        if (notificationBuilder != null && notificationManager != null) {
            notificationManager.notify(
                    channelName /* Tag of notification */,
                    1 /* ID of notification */,
                    notificationBuilder.build());
        }
    }

}
