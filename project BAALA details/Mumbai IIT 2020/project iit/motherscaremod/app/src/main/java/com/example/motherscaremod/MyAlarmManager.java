package com.example.motherscaremod;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.widget.Toast;


public class MyAlarmManager extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm == null) return;
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "motherscaremod:alarmwakelock");
        wl.acquire(1000 * 60);

        // Show notification if necessary
        if (intent.getBooleanExtra("SHOW_NOTIFICATION", false))
            MyNotificationManager.showNotification(context);

        // Show toast
        Toast.makeText(context, "New data received!", Toast.LENGTH_LONG).show();

        // Play sound
        Uri alarmRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmRingtoneUri != null)
            alarmRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (alarmRingtoneUri != null)
            alarmRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        Ringtone alarmRingtone = RingtoneManager.getRingtone(context, alarmRingtoneUri);
        alarmRingtone.play();

        wl.release();
    }

    public void setAlarm(Context context, boolean showNotification)
    {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent i = new Intent(context, MyAlarmManager.class);
        i.putExtra("SHOW_NOTIFICATION", showNotification);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pi);
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, MyAlarmManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;
        alarmManager.cancel(sender);
    }/*if (intent.getBooleanExtra("SHOW_NOTIFICATION", false))
            MyNotificationManager.showNotification(context);*/
}
