package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView fullname,username,email,phoneno,gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fullname = (TextView) findViewById(R.id.full_name);
        username = (TextView) findViewById(R.id.user_name);
        email = (TextView) findViewById(R.id.email_id);
        phoneno = (TextView) findViewById(R.id.phone_no);
        gender = (TextView) findViewById(R.id.gender);

        showAllUserData();
    }

    private void showAllUserData() {
       /* Intent intent = getIntent();
        String user_fullname = intent.getStringExtra("fullname");
        String user_username = intent.getStringExtra("username");
        String user_email = intent.getStringExtra("email");
        String user_phoneno = intent.getStringExtra("phoneno"); */
        //String user_gender =

        Intent intent = getIntent();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String user_fullname = sp.getString("fullnameFromDB","");
        String user_username = sp.getString("usernameFromDB","");
        String user_email = sp.getString("emailFromDB","");
        String user_phoneno = sp.getString("phonenoFromDB","");
        String user_gender = sp.getString("genderFromDB","");


        fullname.setText("Name :" + " " + user_fullname);
        username.setText("Username :" + " " + user_username);
        email.setText("Email :" + " " + user_email);
        phoneno.setText("Phone No :" + " " + user_phoneno);
        gender.setText("Gender :" + " " + user_gender);

    }
}


