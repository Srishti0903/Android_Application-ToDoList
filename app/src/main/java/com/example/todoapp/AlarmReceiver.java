package com.example.todoapp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.todoapp.Adapter.MhyAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.todoapp.Adapter.MhyAdapter.NOTIFICATION_CHANNEL_ID;

public class AlarmReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;
    @Override
    public void onReceive(final Context context, Intent intent) {

        //MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        //mediaPlayer.start();

        Intent i = new Intent(context, RingtonePlayingService.class);
        context.startService(i);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {

            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }

        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
        assert notificationManager != null;
        notificationManager.notify(id , notification) ;

        SharedPreferences sp = context.getSharedPreferences("ShiftedData", Context.MODE_PRIVATE);
        String taskShift = sp.getString("taskToBeShifted","");
        String dateShift = sp.getString("dateToBeShifted","");
        String timeShift = sp.getString("timeToBeShifted","");

        Toast.makeText(context, "Alarm fired", Toast.LENGTH_SHORT).show();



        /*Intent intent1 = new Intent(context,MhyAdapter.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);*/


}}






