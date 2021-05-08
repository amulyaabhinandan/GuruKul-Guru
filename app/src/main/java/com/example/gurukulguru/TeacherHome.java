package com.example.gurukulguru;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gurukulguru.cards.ClassRoom_card;
import com.example.gurukulguru.cards.Exampaper_Card;
import com.example.gurukulguru.cards.Homework_Card;
import com.example.gurukulguru.cards.Lectures_Card;
import com.example.gurukulguru.cards.Notes_Card;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class TeacherHome extends AppCompatActivity {

    //home
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    int backpress = 1;
    Button logout, yes, no, go,update_btn;
    ActionBarDrawerToggle drawerToggle;
    TextView tech_name, tech_phone;
    ImageView tech_profile_image;
    //cards
    CardView homework_card, notes_card,class_card,lectures_card,exampaper_card;
    CheckBox first_chk, second_chk;

    //navigation
    TextView teacher_name,  teacher_aadhaar, teacher_phoneno, teacher_email;
    ImageView teacher_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        //home
        drawerLayout = findViewById(R.id.teacher_home_navigation);
        navigationView = findViewById(R.id.teacher_navigation_view);
        toolbar = findViewById(R.id.teacher_home_toolbar);
        tech_name = findViewById(R.id.teacher_home_name);
        tech_phone = findViewById(R.id.teacher_home_phone);
        tech_profile_image = findViewById(R.id.teacher_home_profile_image);


        //cards declare
        homework_card = findViewById(R.id.homework);
        notes_card = findViewById(R.id.notes);
        class_card = findViewById(R.id.classroom);
        lectures_card=findViewById(R.id.lectures);
        exampaper_card=findViewById(R.id.question_paper);

        Picasso.get().load(MyStaticClass.currentOnlineTeacher.getPicUrl()).into(tech_profile_image);
        tech_name.setText(MyStaticClass.currentOnlineTeacher.getName());
        tech_phone.setText(MyStaticClass.currentOnlineTeacher.getPhone());



        //cards click

        lectures_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStaticClass.actname="";
                MyStaticClass.type="";
                MyStaticClass.subjectname="";
                MyStaticClass.classname="";
                startActivity(new Intent(TeacherHome.this, Lectures_Card.class));
            }
        });
        homework_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherHome.this, Homework_Card.class));
            }
        });
        notes_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherHome.this, Notes_Card.class));
            }
        });
        class_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherHome.this, ClassRoom_card.class));
            }
        });
        exampaper_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherHome.this, Exampaper_Card.class));
            }
        });

        //toolbar
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        View view = navigationView.getHeaderView(0);
        teacher_pic = view.findViewById(R.id.teacher_profile_image);
        teacher_name = view.findViewById(R.id.teacherName);
        teacher_aadhaar = view.findViewById(R.id.teacherAadhaar);
        teacher_phoneno = view.findViewById(R.id.teacherPhoneNo);
        teacher_email = view.findViewById(R.id.teacherEMail);


        //toolbar values
       Picasso.get().load(MyStaticClass.currentOnlineTeacher.getPicUrl()).into(teacher_pic);
        teacher_name.setText(MyStaticClass.currentOnlineTeacher.getName());
        teacher_email.setText(MyStaticClass.currentOnlineTeacher.getEmail());
        teacher_phoneno.setText(MyStaticClass.currentOnlineTeacher.getPhone());
        teacher_aadhaar.setText(MyStaticClass.currentOnlineTeacher.getAadhaar());


        //buttons in navigation bar
        logout = view.findViewById(R.id.teacher_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(TeacherHome.this); // Context, this, etc.
                dialog.setContentView(R.layout.logout_check_dialog);
                dialog.show();
                yes = dialog.findViewById(R.id.custom_dialog_yes);
                no = dialog.findViewById(R.id.custom_dialog_no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Paper.book().destroy();
                        MyStaticClass.currentOnlineTeacher=null;
                        Intent intent = new Intent(TeacherHome.this, TeacherLoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        update_btn=view.findViewById(R.id.teacher_update_btn);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherHome.this, UpdateActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
            backpress = 1;
        } else {
            if (backpress > 1) {
                finishAffinity();
            } else {
                backpress = (backpress + 1);
                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}