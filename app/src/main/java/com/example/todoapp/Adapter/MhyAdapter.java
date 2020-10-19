package com.example.todoapp.Adapter;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.allyants.notifyme.NotifyMe;
import com.example.todoapp.AlarmReceiver;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.PastTaskActivity;
import com.example.todoapp.R;
import com.example.todoapp.ROOM.MyDatabase;
import com.example.todoapp.ROOM.Tasks;
import com.example.todoapp.TaskActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MhyAdapter extends FirebaseRecyclerAdapter<ToDoModel,MhyAdapter.myviewholder> {

    String ampm;
    SharedPreferences sp;
    SharedPreferences sp1;
    DatePickerDialog picker;
    TimePickerDialog tpicker;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private static final String TAG = "Srishti";
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    public MhyAdapter(@NonNull FirebaseRecyclerOptions<ToDoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final ToDoModel model) {
        holder.checkbox.setText(model.getTask());
        holder.checkbox.setChecked(toBoolean(model.getStatus()));
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.setUnset.setText(model.getState());

        sp = holder.date.getContext().getSharedPreferences("ShiftedData",Context.MODE_PRIVATE);
        sp1 = holder.date.getContext().getSharedPreferences("UserData",Context.MODE_PRIVATE);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    String taskToBeShifted =  model.getTask();
                    String dateToBeShifted =  model.getDate();
                    String timeToBeShifted =  model.getTime();

                    /*SharedPreferences.Editor editor = sp.edit();
                    editor.putString("taskToBeShifted",taskToBeShifted);
                    editor.putString("dateToBeShifted",dateToBeShifted);
                    editor.putString("timeToBeShifted",timeToBeShifted);
                    editor.commit(); */

                    String user_username = sp1.getString("usernameFromDB", "");

                    //remove from here and add to Past Activity
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference();
                    reference.keepSynced(true);
                    reference.child("Tasks").child(user_username)
                            .child(Objects.requireNonNull(getRef(holder.getAdapterPosition()).getKey())).removeValue();

                    Map<String,Object> map = new HashMap<>();
                    map.put("task",taskToBeShifted);
                    map.put("date",dateToBeShifted);
                    map.put("time",timeToBeShifted);
                    map.put("status",0);

                    reference.child("PastTasks").child(user_username).push()
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                   // Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   // Toast.makeText(getApplicationContext(),"Could not inserted",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.date.getContext())
                        .setContentHolder(new ViewHolder(R.layout.new_task))
                        .setExpanded(true,600)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText taskText = myview.findViewById(R.id.newTaskText);
                final EditText dateInput = myview.findViewById(R.id.dateInput);
                final EditText timeInput = myview.findViewById(R.id.timeInput);
                Switch alarmToggle =(Switch)myview.findViewById(R.id.alarmToggle);
                Button saveText = myview.findViewById(R.id.newTaskButton);
                //Button setAlarm = myview.findViewById(R.id.setAlarm);

                taskText.setText(model.getTask());
                dateInput.setText(model.getDate());
                timeInput.setText(model.getTime());

                dateInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);

                        // date picker dialog
                        picker = new DatePickerDialog(holder.date.getContext(),
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


                        tpicker = new TimePickerDialog(holder.date.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if(hourOfDay >=12)
                                {
                                    ampm="PM";
                                }
                                else
                                {
                                    ampm="AM";
                                }
                                timeInput.setText(hourOfDay + ":" + minutes);
                                model.setTime(timeInput.getText().toString());
                            }
                        },currenthour,currentminute,false);
                        tpicker.show();

                    }
                });
                if(!alarmToggle.isChecked())
                {
                    model.setState("NOT SET");
                }
                alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                        AlarmManager alm = (AlarmManager)holder.date.getContext().getSystemService(Context.ALARM_SERVICE);
                        Intent alarmIntent = new Intent(holder.date.getContext(), AlarmReceiver.class);
                        alarmIntent.putExtra(AlarmReceiver. NOTIFICATION_ID , 1 ) ;
                        alarmIntent.putExtra(AlarmReceiver. NOTIFICATION , getNotification()) ;
                        PendingIntent aPendingIntent = PendingIntent.getBroadcast(holder.date.getContext(),0,alarmIntent,PendingIntent. FLAG_UPDATE_CURRENT);

                        if(isChecked)
                        {
                            model.setState("SET");
                            String dateFormat = model.getDate();
                            Log.i("TAG", dateFormat);
                            String [] splitedDate = dateFormat.split("-");
                            int dayTobeAlarmed = Integer.parseInt(splitedDate[0]);
                            int monthTobeAlarmed = Integer.parseInt(splitedDate[1]);
                            int yearTobeAlarmed = Integer.parseInt(splitedDate[2]);
                            System.out.println(dayTobeAlarmed);

                            String timeFormat = model.getTime();
                            String [] splitedTime = timeFormat.split(":");
                            int hourTobeAlarmed = Integer.parseInt(splitedTime[0]);
                            int minuteTobeAlarmed = Integer.parseInt(splitedTime[1]);

                            Calendar calendar = Calendar.getInstance();

                            calendar.set(Calendar.YEAR,yearTobeAlarmed);
                            calendar.set(Calendar.MONTH,monthTobeAlarmed - 1);
                            calendar.set(Calendar.DAY_OF_MONTH,dayTobeAlarmed);
                            calendar.set(Calendar.HOUR_OF_DAY,hourTobeAlarmed);
                            calendar.set(Calendar.MINUTE,minuteTobeAlarmed);
                            calendar.set(Calendar.SECOND,0);

                            Long time = calendar.getTimeInMillis();

                            String taskToBeShifted =  model.getTask();
                            String dateToBeShifted =  model.getDate();
                            String timeToBeShifted =  model.getTime();

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("taskToBeShifted",taskToBeShifted);
                            editor.putString("dateToBeShifted",dateToBeShifted);
                            editor.putString("timeToBeShifted",timeToBeShifted);
                            editor.commit();

                            alm.setExact(AlarmManager.RTC_WAKEUP, time - 900000,aPendingIntent);
                            alm.setExact(AlarmManager.RTC_WAKEUP, time - 600000,aPendingIntent);
                            alm.setExact(AlarmManager.RTC_WAKEUP, time - 300000,aPendingIntent);
                            alm.setExact(AlarmManager.RTC_WAKEUP, time,aPendingIntent);
                            Toast.makeText(holder.date.getContext(),"ALARM ON",Toast.LENGTH_LONG);
                        }
                        else
                        {
                            model.setState("NOT SET");
                            alm.cancel(aPendingIntent);
                            Toast.makeText(holder.date.getContext(),"ALARM OFF",Toast.LENGTH_LONG);
                        }
                    }

                    private Notification getNotification() {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder( holder.date.getContext(), default_notification_channel_id ) ;
                        builder.setContentTitle( "TO DO" ) ;
                        builder.setContentText(model.getTask()) ;
                        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
                        builder.setAutoCancel( true ) ;
                        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
                        return builder.build() ;
                    }
                });

                dialogPlus.show();

                saveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(taskText.getText().toString().isEmpty() | dateInput.getText().toString().isEmpty() | timeInput.getText().toString().isEmpty())
                        {
                            Toast.makeText(holder.date.getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                        Map<String,Object> map = new HashMap<>();
                        map.put("task",taskText.getText().toString());
                        map.put("date",dateInput.getText().toString());
                        map.put("time",timeInput.getText().toString());
                        map.put("status",0);

                        //where database of ROOM is updated
                      /*  MyDatabase myDatabase = Room.databaseBuilder(holder.date.getContext(),MyDatabase.class,"TaskDB")
                                .allowMainThreadQueries().build();
                        myDatabase.dao().updateTask(taskText.getText().toString(),dateInput.getText().toString(),timeInput.getText().toString(),0,Integer.parseInt(getRef(position).getKey())); */

                        String user_username = sp1.getString("usernameFromDB", "");

                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference();
                        reference.keepSynced(true);
                        reference.child("Tasks").child(user_username)
                                .child(Objects.requireNonNull(getRef(holder.getAdapterPosition()).getKey())).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });

                    }}
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Entry");
                builder.setMessage("Do you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String user_username = sp1.getString("usernameFromDB", "");

                        //Delete from ROOM database
                    /*    MyDatabase myDatabase = Room.databaseBuilder(holder.date.getContext(),MyDatabase.class,"TaskDB")
                                .allowMainThreadQueries().build();
                        myDatabase.dao().deleteTask(Integer.parseInt(getRef(position).getKey())); */



                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference();
                        reference.keepSynced(true);
                        reference.child("Tasks").child(user_username).
                                child(Objects.requireNonNull(getRef(holder.getAdapterPosition()).getKey())).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }




    private boolean toBoolean(int status) {
        return status!=0;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        CheckBox checkbox;
        TextView date;
        TextView time;
        TextView setUnset;
        ImageView edit,delete;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            checkbox = (CheckBox)itemView.findViewById(R.id.todocheckbox);
            date=(TextView)itemView.findViewById(R.id.dateEdit) ;
            time=(TextView)itemView.findViewById(R.id.timeEdit) ;
            setUnset = (TextView)itemView.findViewById(R.id.setUnset);
            edit = (ImageView)itemView.findViewById(R.id.editicon);
            delete = (ImageView)itemView.findViewById(R.id.deleteicon);

        }
    }
}
