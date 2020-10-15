/*package com.example.todoapp.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.todoapp.AlarmReceiver;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.TaskActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ToDoAdapter extends FirebaseRecyclerAdapter<ToDoModel,ToDoAdapter.MyViewHolder>
{

   // TaskActivity tsk;
  //  Context context;
  //  String ampm;

    public ToDoAdapter(@NonNull FirebaseRecyclerOptions<ToDoModel> options) {
        super(options);
    }

   public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position,@NonNull ToDoModel ToDomodel) {
        holder.checkbox.setText(ToDomodel.getTask());
        holder.checkbox.setChecked(toBoolean(ToDomodel.getStatus()));
        holder.date.setText(ToDomodel.getDate());
        holder.time.setText(ToDomodel.getTime());

    /*    holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view = View.inflate(tsk, R.layout.new_task, null);
                final DialogPlus dialogPlus = DialogPlus.newDialog(tsk)
                        .setContentHolder((Holder) new ViewHolder(view))
                        .setExpanded(true,300)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText taskText = myview.findViewById(R.id.newTaskText);
                final EditText dateInput = myview.findViewById(R.id.dateInput);
                final EditText timeInput = myview.findViewById(R.id.timeInput);
                Button saveText = myview.findViewById(R.id.newTaskButton);
                Button setAlarm = myview.findViewById(R.id.setAlarm);
              //  Button dateButton = myview.findViewById(R.id.dateButton);
               // Button timeButton = myview.findViewById(R.id.timeButton);

                taskText.setText(ToDoModel.getTask());
                dateInput.setText(ToDoModel.getDate());
                timeInput.setText(ToDoModel.getTime());

               dateInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        DatePickerDialog picker = new DatePickerDialog(tsk,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        dateInput.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });

                timeInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cldr = Calendar.getInstance();
                        int currenthour = cldr.get(Calendar.HOUR_OF_DAY);
                        int currentminute = cldr.get(Calendar.MINUTE);


                        TimePickerDialog tpicker = new TimePickerDialog(tsk, new TimePickerDialog.OnTimeSetListener() {
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
                            }
                        },currenthour,currentminute,false);
                        tpicker.show();

                    }
                });


                dialogPlus.show();

   /*             setAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cldr = Calendar.getInstance();
                        cldr.set(
                            dateInput.getText();

                        );
                        setAlarm(cldr.getTimeInMillis());
                    }
                    )
                });    */

     /*           saveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("textToBeUpdated",taskText.getText().toString());
                        map.put("dateSelected",dateInput.getText().toString());
                        map.put("timeSelected",timeInput.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("tasks")
                        .child(getRef(position).getKey()).updateChildren(map)
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

                    }
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
                        FirebaseDatabase.getInstance().getReference().child("tasks")
                                .child(getRef(position).getKey()).removeValue();
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
    }                            */

  /*  private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(tsk,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(tsk,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent);
    }
*/
    /*private boolean toBoolean(int n)
    {
      return n!=0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {

        //single row things (from task_layout)
        CheckBox checkbox;
        EditText date;
        EditText time;
        ImageView edit,delete;
        MyViewHolder(View view) {
            super(view);
            checkbox = (CheckBox)view.findViewById(R.id.todocheckbox);
            date=(EditText)view.findViewById(R.id.dateEdit) ;
            time=(EditText)view.findViewById(R.id.timeEdit) ;
            edit = (ImageView)view.findViewById(R.id.editicon);
            delete = (ImageView)view.findViewById(R.id.deleteicon);
        }
    }

}
*/