package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Shared {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int mode = 0;
    String filename = "Login";
    String data = "b";

    public Shared(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(filename,mode);
        editor = sharedPreferences.edit();
    }

    public void secondTime()
    {
        editor.putBoolean(data,true);
        editor.commit();
    }

    public void logoutTime()
    {
        editor.putBoolean(data,false);
        editor.commit();
    }

    public void firstTime()
    {
        if(!this.login())
        {
            //login
            Intent intent = new Intent(context,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    public boolean login() {
        return sharedPreferences.getBoolean(data,false);
    }
}
