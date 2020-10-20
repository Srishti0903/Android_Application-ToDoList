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

    public static final String ACTION_DISMISS = "ACTION_DISMISS";

    MediaPlayer myPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        myPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        myPlayer.start();

        String action = intent.getAction();

        if (ACTION_DISMISS.equals(action)) {
            dismissRingtone();
        }

        return START_STICKY;
    }


    private void dismissRingtone() {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        myPlayer.stop();
    }
}

