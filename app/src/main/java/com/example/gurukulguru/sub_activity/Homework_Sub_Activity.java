package com.example.gurukulguru.sub_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.gurukulguru.AddDocumentActivity;
import com.example.gurukulguru.Document_Adapter;
import com.example.gurukulguru.ModelClasses.Documents;
import com.example.gurukulguru.MyStaticClass;
import com.example.gurukulguru.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Homework_Sub_Activity extends AppCompatActivity {

    TextView add, subname;
    RecyclerView myrecycle;
    ArrayList<Documents> documents,docs2;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework__sub_);

        add=findViewById(R.id.add_homework);
        subname=findViewById(R.id.homeworksubject);
        subname.setText(MyStaticClass.subjectname);
        myrecycle=findViewById(R.id.add_homework_recycler);
        documents=new ArrayList<>();
        docs2=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("HOMEWORK").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Documents docs;
                if (dataSnapshot.getChildrenCount() == 0) {
                    myrecycle.setPadding(250,250,250,250);
                    myrecycle.setBackground(getDrawable(R.drawable.no_available_student));
                } else {
                    myrecycle.setBackground(null);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        docs = snapshot.getValue(Documents.class);
                        documents.add(docs);
                    }
                    for(int i=documents.size()-1;i>=0;i--)
                    {
                        docs2.add(documents.get(i));
                    }
                    Document_Adapter student_adapter = new Document_Adapter(Homework_Sub_Activity.this,"HOMEWORK", docs2);
                    myrecycle.setAdapter(student_adapter);
                    myrecycle.setLayoutManager(new LinearLayoutManager(Homework_Sub_Activity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStaticClass.type="application/pdf";
                MyStaticClass.actname="homework";
                startActivity(new Intent(Homework_Sub_Activity.this, AddDocumentActivity.class));
            }
        });
    }
}
