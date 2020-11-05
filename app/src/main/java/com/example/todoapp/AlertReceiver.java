package com.example.todoapp;


import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.todoapp.Model.ToDoModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.provider.Settings.System.DEFAULT_RINGTONE_URI;

public class AlertReceiver extends BroadcastReceiver {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager mManager;
    ToDoModel model;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    public void onReceive(Context context, Intent intent) {

        model = new ToDoModel();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            if (mManager == null) {
                mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            mManager.createNotificationChannel(channel);



            Intent i = new Intent(context, RingtonePlayingService.class);
            context.startService(i);

            SharedPreferences sp3 = context.getSharedPreferences("ShiftedData", Context.MODE_PRIVATE);
            String task = sp3.getString("taskToBeShifted","");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channelID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("TO DO");
                    builder.setContentText(task);
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setCategory(NotificationCompat.CATEGORY_ALARM);
            builder.setColor(Color.BLUE);
            builder.setAutoCancel(true);
            Intent dismissIntent = new Intent(context, RingtonePlayingService.class);
            dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
            PendingIntent pendingIntent = PendingIntent.getService(context, 123, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intent1 = new Intent(context, TaskActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pIntent);

            NotificationCompat.Action action = new NotificationCompat.Action(android.R.drawable.ic_lock_idle_alarm, "CANCEL", pendingIntent);
            builder.addAction(action);

            mManager.notify(123, builder.build());
        }


        //NotificationHelper notificationHelper = new NotificationHelper(context);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        //NotificationCompat.Builder nb = notificationHelper.getChannelNotification(pendingIntent);
        //notificationHelper.getManager().notify(1, nb.build());
    }
}