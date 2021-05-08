package com.example.gurukulguru.cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.gurukulguru.Classes_Adapter;
import com.example.gurukulguru.R;
import com.example.gurukulguru.TeacherHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notes_Card extends AppCompatActivity {
    RecyclerView myclass;
    ArrayList<String> mylist;
    DatabaseReference databaseReference;
    int backpress=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes__card);
        myclass=findViewById(R.id.myclass_holder_notes);
        mylist=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("CLASSES");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    mylist.add(snapshot.getKey());
                }
                Classes_Adapter classes_adapter=new Classes_Adapter(Notes_Card.this,mylist,"Notes");
                myclass.setAdapter(classes_adapter);
                myclass.setLayoutManager(new LinearLayoutManager(Notes_Card.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
