package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.todoapp.Model.ToDoModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CancelAlarmAndNotificationActivity extends AppCompatActivity {

    Switch cancelAlarm;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_alarm_and_notification);
        final ToDoModel model = new ToDoModel();


        cancelAlarm = (Switch) findViewById(R.id.switch2);
        sp1 = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        cancelAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            AlarmManager alm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            String user_username = sp1.getString("usernameFromDB", "");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks").child(user_username);

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Log.d("HELLU", String.valueOf(ds));
                                String state = ds.child("state").getValue(String.class);
                                Log.d("HELLU", state);
                                if (state.equals("SET")) {
                                    model.setState("NOT SET");
                                    alm.cancel(pendingIntent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


    }
}


