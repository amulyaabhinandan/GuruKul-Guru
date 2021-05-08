package com.example.gurukulguru.cards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurukulguru.Add_Class;
import com.example.gurukulguru.Classes_Adapter;
import com.example.gurukulguru.R;
import com.example.gurukulguru.TeacherHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassRoom_card extends AppCompatActivity {
    TextView add_class_new;
    RecyclerView myclass;
    ArrayList<String> mylist;
    DatabaseReference databaseReference;
    int backpress=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room_card);

        myclass=findViewById(R.id.myclass_holder);
        mylist=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("CLASSES");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    mylist.add(snapshot.getKey());
                }
                Classes_Adapter classes_adapter=new Classes_Adapter(ClassRoom_card.this,mylist,"ClassRoom");
                myclass.setAdapter(classes_adapter);
                myclass.setLayoutManager(new LinearLayoutManager(ClassRoom_card.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        add_class_new=findViewById(R.id.add_class);
        add_class_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClassRoom_card.this, Add_Class.class));
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (backpress > 1) {
            startActivity(new Intent(this, TeacherHome.class));
            finishAffinity();
        } else {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
    }
}
