package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todoapp.Adapter.MhyAdapter;
import com.example.todoapp.Adapter.PastAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PastTaskActivity extends AppCompatActivity {

    RecyclerView pastRecyclerView;
    PastAdapter pastAdapter;
    ImageView deleteallpasttask;
    SharedPreferences sp;
    SharedPreferences sp1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_task);
        sp = getApplicationContext().getSharedPreferences("ShiftedData", Context.MODE_PRIVATE);
        sp1 = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        pastRecyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String user_username = sp1.getString("usernameFromDB", "");

        FirebaseRecyclerOptions<ToDoModel> options = new FirebaseRecyclerOptions.Builder<ToDoModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("PastTasks").child(user_username), ToDoModel.class)
                .build();

        pastAdapter = new PastAdapter(options);
        pastRecyclerView.setAdapter(pastAdapter);
        deleteallpasttask = (ImageView)findViewById(R.id.deleteiconforpasttask);


        deleteallpasttask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_username = sp1.getString("usernameFromDB", "");
                FirebaseDatabase.getInstance().getReference().child("PastTasks").child(user_username).removeValue();

            }
        });


        Intent intent = getIntent();

        String taskShift = sp.getString("taskToBeShifted","");
        String dateShift = sp.getString("dateToBeShifted","");
        String timeShift = sp.getString("timeToBeShifted","");

        Map<String,Object> map = new HashMap<>();
        map.put("task",taskShift);
        map.put("date",dateShift);
        map.put("time",timeShift);
        map.put("status",0);


        FirebaseDatabase.getInstance().getReference().child("PastTasks").child(user_username).push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Could not inserted",Toast.LENGTH_LONG).show();
                    }
                });

    }



    @Override
    protected void onStart() {
        super.onStart();
        pastAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pastAdapter.stopListening();
    }
}