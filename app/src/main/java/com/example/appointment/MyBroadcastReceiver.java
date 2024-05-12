package com.example.appointment;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.appointment.models.StudentStaticModel;
import com.example.appointment.activities.StudentMainActivity;

import java.util.Calendar;
import java.util.Random;

public class MyBroadcastReceiver extends BroadcastReceiver
{
    DBHelper db;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        Log.d("Alarm notification", "running");
        Intent i;
        db = new DBHelper(context);
        //db.updateAlarm(1);
        i = new Intent(context, StudentMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(i);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);



        /*Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);*/

        String title = intent.getStringExtra("student");
        String text = String.format("You have an appointment for Sir %s", intent.getStringExtra("teacher"));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notify")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(title)
                .setContentText(text)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("CHANNEL", "CHANNEL", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManagerCompat.createNotificationChannel(channel);
        }

        try
        {
            notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
        }
        catch (SecurityException ex)
        {
            Log.e("Debugging", String.valueOf(ex));
        }


    }


}