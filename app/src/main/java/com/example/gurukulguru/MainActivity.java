package com.example.gurukulguru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gurukulguru.ModelClasses.Teachers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    AnimationDrawable rocketAnimation;
    String phone_text,password_text;
    ImageView rocketImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rocketImage =findViewById(R.id.imageView);
        rocketImage.setBackgroundResource(R.drawable.loading);
        if(rocketImage!=null) {
            rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
            rocketAnimation.start();
            Paper.init(this);
            phone_text=Paper.book().read(MyStaticClass.userPhoneKey);
            password_text=Paper.book().read(MyStaticClass.userPasswordKey);
            if(TextUtils.isEmpty(phone_text))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rocketAnimation.stop();
                        Intent intent=new Intent(MainActivity.this,TeacherLoginActivity.class);
                        ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,rocketImage,"splash_animation");
                        startActivity(intent,optionsCompat.toBundle());
                        finishAffinity();
                    }
                }, 7500);
            }
            else {
                LoginUser();
            }
        }
    }


    private void LoginUser() {
        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("TEACHERS").child(phone_text).exists()) {
                    Teachers teachers=dataSnapshot.child("TEACHERS").child(phone_text).getValue(Teachers.class);
                    if(teachers.getPassword().equals(password_text))
                    {
                        MyStaticClass.currentOnlineTeacher=teachers;
                        Paper.book().write(MyStaticClass.userPhoneKey,phone_text);
                        Paper.book().write(MyStaticClass.userPasswordKey,password_text);
                        startActivity(new Intent(MainActivity.this,TeacherHome.class));
                        finishAffinity();
                    }else {
                        Toast.makeText(MainActivity.this, "Passowrd Was Changed!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rocketAnimation.stop();
                            Intent intent=new Intent(MainActivity.this,TeacherLoginActivity.class);
                            ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,rocketImage,"splash_animation");
                            startActivity(intent,optionsCompat.toBundle());
                            finishAffinity();
                        }
                    }, 6000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}