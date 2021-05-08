package com.example.gurukulguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgetPassword extends AppCompatActivity {
    EditText phoneNumber;
    Button send_otp;
    String phone_text;
    int backpress=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        phoneNumber=findViewById(R.id.forget_password_text);
        send_otp = findViewById(R.id.Send_Otp_btn);
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              phone_text=phoneNumber.getText().toString();
              if(phone_text.length()==0)
              {
                  phoneNumber.setError("Enter The Registered Number");
              }
              if(phone_text.length()<10)
              {
                  phoneNumber.setError("Enter the Full Indian Number");
              }
              if(phone_text.length()==10)
              {
                  send_otp.setEnabled(false);
                  phoneNumber.setEnabled(false);
                  CheckNumber();

              }
            }
        });
    }

    private void CheckNumber() {
        DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("TEACHERS").child(phone_text).exists())
                {
                    varifyNumber("+91"+phone_text);
                }
                else {
                    Toast.makeText(ForgetPassword.this, "Account with "+phone_text+" Doesn't exists", Toast.LENGTH_SHORT).show();
                    send_otp.setEnabled(true);
                    phoneNumber.setEnabled(true);
                    phoneNumber.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void varifyNumber(String s) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(s, 30l, TimeUnit.SECONDS, ForgetPassword.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(ForgetPassword.this, "OTP Sent", Toast.LENGTH_SHORT).show();            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(ForgetPassword.this, "Phone Verified", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ForgetPassword.this, ResetPassword.class);
                intent.putExtra("phoneno",phone_text);
                startActivity(intent);
                finish();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        });
    }
    @Override
    public void onBackPressed() {

        if (backpress > 1) {
            startActivity(new Intent(this, TeacherLoginActivity.class));
            finishAffinity();
        } else {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
    }
}
