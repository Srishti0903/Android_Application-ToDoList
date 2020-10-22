package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.todoapp.Model.ToDoModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LogOutActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Shared shared = new Shared(getApplicationContext());
        shared.firstTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        AlertDialog.Builder builder = new AlertDialog.Builder(LogOutActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(LogOutActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(),"Successfully Logged out!", Toast.LENGTH_LONG).show();
                Shared shared = new Shared(getApplicationContext());
                shared.firstTime();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(LogOutActivity.this,TaskActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();

    }

}

