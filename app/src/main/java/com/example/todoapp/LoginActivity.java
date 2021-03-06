package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.ROOM.MyDatabase;
import com.example.todoapp.ROOM.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText user_name;
    EditText password;
    TextView link;
    Button login;
    Button register;
    SharedPreferences sp;
    SharedPreferences spLog;
    boolean connected;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_name=(EditText)findViewById(R.id.user_name);
        password=(EditText)findViewById(R.id.password);
        link=(TextView)findViewById(R.id.link);
        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.register);
        sp = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        spLog = getSharedPreferences("Login", Context.MODE_PRIVATE);
        connected = false;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
        finish();
        System.exit(0);
    }

    private Boolean validateUserName()
    {
        String b = user_name.getText().toString();
        if(b.isEmpty())
        {
            login.setEnabled(false);
            //user_name.setError("Field cannot be empty");
            return false;
        }
        else
        {
            user_name.setError(null);
            return true;
        }
    }

    private Boolean validatePassword()
    {
        String e = password.getText().toString();
        if(e.isEmpty())
        {
            login.setEnabled(false);
            //password.setError("Field cannot be empty");
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }


    public void forgotPassword(View view) {
        Intent i =new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        i.putExtra("_username",user_name.getText().toString());
        startActivity(i);
        finish();
    }

    public void btn_signup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);

    }

    public void login_user(View view) {
        if(!validateUserName() | !validatePassword())
        {
            login.setEnabled(true);
            return;
        }
        else
        {
            isUser();
        }
    }

    private void isUser() {



        final String userEnteredUsername = user_name.getText().toString().trim();  //srishti
        final String userEnteredPassword = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.keepSynced(true);

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);    //yes
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user_name.setError(null);
                    //get fields from database
                    final String passwordFromDB = snapshot.child(userEnteredUsername).child("pass").getValue().toString();
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        //Toast.makeText(LoginActivity.this, "Yes",Toast.LENGTH_LONG).show();
                        String fullnameFromDB = snapshot.child(userEnteredUsername).child("fullname").getValue().toString();
                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue().toString();
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue().toString();
                        String phonenoFromDB = snapshot.child(userEnteredUsername).child("phoneno").getValue().toString();
                        String genderFromDB = snapshot.child(userEnteredUsername).child("gender").getValue().toString();



                        //String taskToBeShifted =  model.getTask();
                        //String dateToBeShifted =  model.getDate();
                        // String timeToBeShifted =  model.getTime();

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("fullnameFromDB", fullnameFromDB);
                        editor.putString("usernameFromDB", usernameFromDB);
                        editor.putString("emailFromDB", emailFromDB);
                        editor.putString("phonenoFromDB", phonenoFromDB);
                        editor.putString("genderFromDB", genderFromDB);
                        editor.commit();


                        Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                        startActivity(intent);
                        finish();
                        Shared shared = new Shared(getApplicationContext());
                        shared.secondTime();
                    } else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
                    user_name.setError("No such User");
                    user_name.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }}




