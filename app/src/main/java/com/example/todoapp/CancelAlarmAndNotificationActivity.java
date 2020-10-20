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
import android.widget.Toast;

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
import java.util.Objects;

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

            AlarmManager alm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String user_username = sp1.getString("usernameFromDB", "");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks").child(user_username);

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                //Log.d("HELLU", String.valueOf(ds));
                                String state = ds.child("state").getValue(String.class);
                                //Log.d("HELLU", state);
                                if (state.equals("SET")) {
                                    //Log.d("HELLU", "IF IS WORKING");
                                    model.setState("NOT SET");
                                    reference.child(Objects.requireNonNull(ds.getKey())).child("state").setValue("NOT SET");

                                    alm.cancel(pendingIntent);
                                    Toast.makeText(getApplicationContext(),"All alarms are cancelled!",Toast.LENGTH_LONG).show();
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


