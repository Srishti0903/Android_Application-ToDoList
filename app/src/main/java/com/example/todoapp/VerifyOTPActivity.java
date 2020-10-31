package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    String codeBySystem;
    EditText pin;
    String phoneno,username;
    String whatToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p2);

        pin = (EditText)findViewById(R.id.pinId);

        //String email_id = getIntent().getStringExtra("emailaccessed");
        phoneno = getIntent().getStringExtra("phoneaccessed");
        username = getIntent().getStringExtra("usernameaccessed");
        sendVerificationCode(phoneno);
    }

    private void sendVerificationCode(String phoneno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneno,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallbacks);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
              pin.setText(code);
              verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyOTPActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    updatePassword();
                    Toast.makeText(VerifyOTPActivity.this,"Verification completed.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(VerifyOTPActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void updatePassword() {
        Intent i = new Intent(VerifyOTPActivity.this,SetNewPassword.class);
        i.putExtra("username",username);
        startActivity(i);
        finish();
    }


    public void callNextScreenFromOTP(View view) {
        String code = pin.getText().toString();
        if(!code.isEmpty())
        {
            verifyCode(code);
        }
    }
}