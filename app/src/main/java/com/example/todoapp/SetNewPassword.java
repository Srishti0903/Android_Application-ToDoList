package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    EditText password,confirmPassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
    }

    public void setPasswordButton(View view) {
          if(!validatePassword() | !validateConfirmPassword())
          {
                return;
          }

          else {

              String _newPassword = password.getText().toString().trim();
              String _username = getIntent().getStringExtra("username");

              rootNode = FirebaseDatabase.getInstance();
              reference = rootNode.getReference("users");
              reference.keepSynced(true);

              reference.child(_username).child("pass").setValue(_newPassword);
              Intent i = new Intent(SetNewPassword.this,PasswordUpdatedActivity.class);
              startActivity(i);
              finish();
          }
    }

    private Boolean validatePassword()
    {
        String e = password.getText().toString();
        //String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$";
        String passwordPattern = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        if(e.isEmpty())
        {
            password.setError("Field cannot be empty");
            return false;
        }
        else if(!e.matches(passwordPattern))
        {
            password.setError("This Password is not allowed. Your Password must be atleast 8 characters long and must contain one uppercase letter, one digit and one special character");
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }

    private Boolean validateConfirmPassword()
    {
        String f = confirmPassword.getText().toString();
        String g = password.getText().toString();
        if(f.isEmpty())
        {
            confirmPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!f.matches(g))
        {
            confirmPassword.setError("Password not matches");
            return false;
        }
        else
        {
            confirmPassword.setError(null);
            return true;
        }
    }

}