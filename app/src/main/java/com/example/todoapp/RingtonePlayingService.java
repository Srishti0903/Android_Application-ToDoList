package com.example.todoapp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class RingtonePlayingService extends Service {

   // public static final String ACTION_DISMISS = "ACTION_DISMISS";
   private static final String URI_BASE = RingtonePlayingService.class.getName() + ".";
   public static final String ACTION_DISMISS = URI_BASE + "ACTION_DISMISS";

    private MediaPlayer myPlayer;
    private Ringtone ringtone;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null) {
            return START_REDELIVER_INTENT;
        }

        String action = intent.getAction();

        if(ACTION_DISMISS.equals(action))
            dismissRingtone();

        else
        {
            myPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            myPlayer.start();
        }


        return START_NOT_STICKY;
    }



    private void dismissRingtone() {
        Intent i = new Intent(this, RingtonePlayingService.class);
        stopService(i);

        NotificationManager notificationManager = (NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(123);
    }

    @Override
    public void onDestroy() {
        myPlayer.stop();
    }
}

