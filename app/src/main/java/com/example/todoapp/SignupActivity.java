package com.example.todoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.Model.UserHelperClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText full_name, user_name, email_id, phone_no, password, confirm_password;
    Button register_user;
    RadioGroup gender;
    RadioButton selectedRadioButton;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
       // mAuth = FirebaseAuth.getInstance();
        full_name = (EditText)findViewById(R.id.full_name);
        user_name = (EditText)findViewById(R.id.user_name);
        email_id = (EditText)findViewById(R.id.email_id);
        phone_no = (EditText)findViewById(R.id.phone_no);
        password = (EditText)findViewById(R.id.password);
        confirm_password = (EditText)findViewById(R.id.confirm_password);
        gender = (RadioGroup)findViewById(R.id.gender);
        register_user = (Button)findViewById(R.id.register);


    }

    private Boolean validateFullName()
    {
        String a =full_name.getText().toString();
        if(a.isEmpty())
        {
            full_name.setError("Field cannot be empty");
            return false;
        }
        else
        {
            full_name.setError(null);
            return true;
        }
    }

    private Boolean validateUserName()
    {
        String b = user_name.getText().toString();
        String noWhiteSpaces = "\\A\\S{4,20}\\Z";
       // String noWhiteSpaces = "(?=\\s+$)";
        if(b.isEmpty())
        {
            user_name.setError("Field cannot be empty");
            return false;
        }
        else if(b.length() >= 10)
        {
            user_name.setError("Username too long");
            return false;
        }
        else if(!b.matches(noWhiteSpaces))
        {
            user_name.setError("White spaces are not allowed");
            return false;
        }
        else
        {
            user_name.setError(null);
            return true;
        }
    }

    private Boolean validateEmailId()
    {
        String c = email_id.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(c.isEmpty())
        {
            email_id.setError("Field cannot be empty");
            return false;
        }
        else if(!c.matches(emailPattern))
        {
            email_id.setError("Invalid email address");
            return false;
        }
        else
        {
            email_id.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String d = phone_no.getText().toString();
        boolean check = false;
        if(d.isEmpty())
        {
            phone_no.setError("Field cannot be empty");
            return false;
        }
        else if (!Pattern.matches("[a-zA-Z]+", d))

            if (d.length() < 6 || d.length() > 13)
            {
                phone_no.setError("Enter valid phone No");
                check = false;
                return check;
            }
            else {
                phone_no.setError(null);
                check = true;
                return check;

            }
            return check;
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
            password.setError("This Password is not allowed");
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
        String f = confirm_password.getText().toString();
        String g = password.getText().toString();
        if(f.isEmpty())
        {
            confirm_password.setError("Field cannot be empty");
            return false;
        }
        else if(!f.matches(g))
        {
            confirm_password.setError("Password not matches");
            return false;
        }
        else
        {
            confirm_password.setError(null);
            return true;
        }
    }

    private Boolean validateGender()
    {
       // String male = male.getText().toString();
        if(gender.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),"Select Gender",Toast.LENGTH_LONG).show();
            return  false;
        }
        else
        {
            int selectedid = gender.getCheckedRadioButtonId();
            selectedRadioButton = (RadioButton)findViewById(selectedid);
            selectedRadioButton.setError(null);
            return true;
        }
    }


    public void registerUser(View view) {
        if(!validateFullName() | !validateUserName() | !validateEmailId() | !validatePhoneNumber() | !validatePassword() | !validateConfirmPassword() | !validateGender())
        {
            return;
        }
        else
        {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");

            String fullname = full_name.getText().toString();
            String username = user_name.getText().toString();
            String email = email_id.getText().toString();
            String phoneno = phone_no.getText().toString();
            String pass = password.getText().toString();
            String gender = selectedRadioButton.getText().toString();

            UserHelperClass helperClass = new UserHelperClass(fullname,username,email,phoneno,pass,gender);
            reference.child(username).setValue(helperClass);
            Toast.makeText(SignupActivity.this, "You are Registered Successfully", Toast.LENGTH_SHORT).show();
        }
    }

}


