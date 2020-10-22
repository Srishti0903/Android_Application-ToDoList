package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todoapp.Adapter.MhyAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.UserHelperClass;
import com.example.todoapp.ROOM.MyDatabase;
import com.example.todoapp.ROOM.Tasks;
import com.example.todoapp.ROOM.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.todoapp.DayOrNightActivity.MyPREFERENES;

public class TaskActivity extends AppCompatActivity {

    RecyclerView tasksRecyclerView;
    MhyAdapter m;
    FloatingActionButton floatingActionButton;
    String ampm;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    Switch alarmToggle;
    SharedPreferences sp;
    SharedPreferences sp1;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_task);
        // getSupportActionBar().hide();
        setContentView(R.layout.activity_drawer);

        final ToDoModel model = new ToDoModel();

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navmenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        alarmToggle = (Switch) findViewById(R.id.alarmToggle);
        sp = getApplicationContext().getSharedPreferences("ShiftedData", Context.MODE_PRIVATE);
        sp1 = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //write other cases here

                    case R.id.menu_profile:
                        Intent intent1 = new Intent(TaskActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_mode:
                        Intent intent2 = new Intent(TaskActivity.this, DayOrNightActivity.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_alarm:
                       Intent intent3 = new Intent(TaskActivity.this, CancelAlarmAndNotificationActivity.class);
                       startActivity(intent3);
                      drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_pasttodo:
                        Intent intent4 = new Intent(TaskActivity.this, PastTaskActivity.class);
                        startActivity(intent4);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_settings:
                        Intent intent5 = new Intent(TaskActivity.this, LogOutActivity.class);
                        startActivity(intent5);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });


        tasksRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();

        String user_username = sp1.getString("usernameFromDB", "");
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference();
        reference.keepSynced(true);
        FirebaseRecyclerOptions<ToDoModel> options = new FirebaseRecyclerOptions.Builder<ToDoModel>()
                .setQuery(reference.child("Tasks").child(user_username), ToDoModel.class)
                .build();

        m = new MhyAdapter(options);
        tasksRecyclerView.setAdapter(m);

        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialogPlus = DialogPlus.newDialog(TaskActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.save_new_task))
                        .setExpanded(true, 600)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText taskText = myview.findViewById(R.id.savenewTaskText);
                final EditText dateInput = myview.findViewById(R.id.savedateInput);
                final EditText timeInput = myview.findViewById(R.id.savetimeInput);
                Button saveText = myview.findViewById(R.id.savenewTaskButton);
                final Switch alarmToggle = (Switch) myview.findViewById(R.id.savealarmToggle);

                dateInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        DatePickerDialog picker = new DatePickerDialog(TaskActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        dateInput.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        model.setDate(dateInput.getText().toString());
                                    }
                                }, year, month, day);
                        picker.getDatePicker().setMinDate(cldr.getTimeInMillis());
                        picker.show();

                    }
                });

                timeInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cldr = Calendar.getInstance();
                        int currenthour = cldr.get(Calendar.HOUR_OF_DAY);
                        int currentminute = cldr.get(Calendar.MINUTE);


                        TimePickerDialog tpicker = new TimePickerDialog(TaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    ampm = "PM";
                                } else {
                                    ampm = "AM";
                                }
                                timeInput.setText(hourOfDay + ":" + minutes);
                                model.setTime(timeInput.getText().toString());
                            }
                        }, currenthour, currentminute, false);
                        tpicker.show();
                    }
                });


                dialogPlus.show();

                saveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(taskText.getText().toString().isEmpty() | dateInput.getText().toString().isEmpty() | timeInput.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("task", taskText.getText().toString());
                            map.put("date", dateInput.getText().toString());
                            map.put("time", timeInput.getText().toString());
                            map.put("status", 0);
                            map.put("state", model.getState());

                            String user_username = sp1.getString("usernameFromDB", "");

                          /*  Tasks tasks = new Tasks(taskText.getText().toString(),0,model.getState(),dateInput.getText().toString(),timeInput.getText().toString());
                            MyDatabase myDatabase = Room.databaseBuilder(TaskActivity.this,MyDatabase.class,"TaskDB")
                                    .allowMainThreadQueries().build();
                            myDatabase.dao().addTasks(tasks);  */


                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference();
                            reference.keepSynced(true);
                            reference.child("Tasks").child(user_username).push()
                                    .setValue(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            taskText.setText("");
                                            dateInput.setText("");
                                            timeInput.setText("");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Could not inserted", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }

                    }

                });
                if(!alarmToggle.isChecked())
                {
                    model.setState("NOT SET");
                }
                alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                        AlarmManager alm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        if (isChecked) {
                            model.setState("SET");
                            String dateFormat = model.getDate();
                            String[] splitedDate = dateFormat.split("-");
                            int dayTobeAlarmed = Integer.parseInt(splitedDate[0]);
                            int monthTobeAlarmed = Integer.parseInt(splitedDate[1]);
                            int yearTobeAlarmed = Integer.parseInt(splitedDate[2]);
                            Log.d("ALARM",String.valueOf(dayTobeAlarmed));
                            Log.d("ALARM",String.valueOf(monthTobeAlarmed));
                            Log.d("ALARM",String.valueOf(yearTobeAlarmed));

                            String timeFormat = model.getTime();
                            String[] splitedTime = timeFormat.split(":");
                            int hourTobeAlarmed = Integer.parseInt(splitedTime[0]);
                            int minuteTobeAlarmed = Integer.parseInt(splitedTime[1]);
                            Log.d("ALARM",String.valueOf(hourTobeAlarmed));
                            Log.d("ALARM",String.valueOf(minuteTobeAlarmed));

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, yearTobeAlarmed);
                            calendar.set(Calendar.MONTH, monthTobeAlarmed - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, dayTobeAlarmed);
                            calendar.set(Calendar.HOUR_OF_DAY, hourTobeAlarmed);
                            calendar.set(Calendar.MINUTE, minuteTobeAlarmed);
                            calendar.set(Calendar.SECOND, 0);
                            Log.d("ALARM",String.valueOf(calendar.get(Calendar.YEAR)));
                            Log.d("ALARM",String.valueOf(calendar.get(Calendar.MONTH)));
                            Log.d("ALARM",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                            Log.d("ALARM",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                            Log.d("ALARM",String.valueOf(calendar.get(Calendar.MINUTE)));
                            Log.d("ALARM",String.valueOf(calendar.get(Calendar.SECOND)));


                            String taskToBeShifted = model.getTask();
                            String dateToBeShifted = model.getDate();
                            String timeToBeShifted = model.getTime();

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("taskToBeShifted", taskToBeShifted);
                            editor.putString("dateToBeShifted", dateToBeShifted);
                            editor.putString("timeToBeShifted", timeToBeShifted);
                            editor.commit();

                                /*alm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 300000, pendingIntent);
                                alm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 600000, pendingIntent);
                                alm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 900000, pendingIntent);
                                alm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); */
                               alm.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() - 900000,300000,pendingIntent);

                            Toast.makeText(getApplicationContext(), "ALARM ON", Toast.LENGTH_LONG);
                        }

                        else {
                            model.setState("NOT SET");
                            alm.cancel(pendingIntent);
                        }
                    }

                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Shared shared = new Shared(getApplicationContext());
        shared.firstTime();
        m.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        m.stopListening();
    }

}