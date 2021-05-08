package com.example.gurukulguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gurukulguru.ModelClasses.Teachers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class TeacherLoginActivity extends AppCompatActivity {
    Button teacher_login_btn;
    TextView ForgetPassword,not_register;
    EditText phone,password;
    String Phone_text,Password_text;
    DatabaseReference rootref;
    int backpress=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);


        teacher_login_btn = findViewById(R.id.teacher_login_button);
        ForgetPassword = findViewById(R.id.forget_password);
        not_register=findViewById(R.id.not_registered_text);
        phone=findViewById(R.id.phone_number);
        password=findViewById(R.id.password);

        Paper.init(this);


        not_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherLoginActivity.this, RegisterActivity.class));
            }
        });

        rootref= FirebaseDatabase.getInstance().getReference().child("CLASSES");

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherLoginActivity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
        teacher_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone_text=phone.getText().toString();
                Password_text=password.getText().toString();
                if((Phone_text.length()==10))
                {
                    if((Password_text.length()!=0))
                    {
                        password.setEnabled(false);
                        phone.setEnabled(false);
                        teacher_login_btn.setEnabled(false);
                        LoginUser();
                    }
                    else {
                        password.setError("Password can't be Null");
                    }
                }
                else {

                 phone.setError("Please Enter Correct Phone Number");
                }
            }
        });
    }

    private void LoginUser() {
        final DatabaseReference rootref;
        rootref=FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("TEACHERS").child(Phone_text).exists()) {
                    Teachers teachers=dataSnapshot.child("TEACHERS").child(Phone_text).getValue(Teachers.class);
                    if(teachers.getPassword().equals(Password_text))
                    {
                        MyStaticClass.currentOnlineTeacher=teachers;
                        Paper.book().write(MyStaticClass.userPhoneKey,Phone_text);
                        Paper.book().write(MyStaticClass.userPasswordKey,Password_text);
                        startActivity(new Intent(TeacherLoginActivity.this,TeacherHome.class));
                        finishAffinity();
                    }else {
                        Toast.makeText(TeacherLoginActivity.this, "Wrong Passowrd !!!", Toast.LENGTH_SHORT).show();
                        password.setEnabled(true);
                        phone.setEnabled(true);
                        teacher_login_btn.setEnabled(true);
                    }
                }
                else {
                    Toast.makeText(TeacherLoginActivity.this, "Account With This Number Don't Exists", Toast.LENGTH_SHORT).show();
                    password.setEnabled(true);
                    phone.setEnabled(true);
                    teacher_login_btn.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {

        if (backpress > 1) {
            finishAffinity();
        } else {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
    }
}