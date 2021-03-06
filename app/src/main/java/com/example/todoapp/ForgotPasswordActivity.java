package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email_id;
    Button reset_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email_id = (EditText)findViewById(R.id.email_id_for_reset_password);
        reset_password = (Button)findViewById(R.id.reset_password_button);


    }

    public void passwordReset(View view) {
        final String email= email_id.getText().toString();
        Intent i = getIntent();
        //user entered during Login
        final String userEnteredUsername = i.getStringExtra("_username");
        Log.d("USERNAME",userEnteredUsername);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.keepSynced(true);
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue().toString();
                String phoneFromDB = dataSnapshot.child(userEnteredUsername).child("phoneno").getValue().toString();
                String phoneFormat = "+91" + phoneFromDB;
                Log.d("PHONENO", phoneFormat);

                Log.d("CHECKUSER",emailFromDB);
                if(emailFromDB.equals(email))
                {
                    Toast.makeText(getApplicationContext(),"It will send One Time Password to your mobile linked with this email",Toast.LENGTH_LONG);
                    Intent intent = new Intent(ForgotPasswordActivity.this,VerifyOTPActivity.class);
                    intent.putExtra("emailaccessed",email);
                    intent.putExtra("phoneaccessed",phoneFormat);
                    intent.putExtra("usernameaccessed",userEnteredUsername);
                    intent.putExtra("whatToDo","updateData");
                    startActivity(intent);
                    finish();

                    /*firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),"Password send to your email",Toast.LENGTH_LONG).show();

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            }); */
                }
                else
                {
                    email_id.setError("No such user exists!");
                    email_id.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}