package com.example.gurukulguru;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurukulguru.ModelClasses.Student;
import com.example.gurukulguru.ModelClasses.Subjects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailsClassActivity extends AppCompatActivity {
    RecyclerView subjects_recycler,students_recycler;
    ArrayList<Student> studentArrayList;
    ArrayList<String> subjects;
    ArrayList<Subjects> subjectsArrayList;
    DatabaseReference databaseReference,databaseReference_subjects,databaseReferencedetails_subjects;
    String class_no;
    TextView nmaeofclass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_class);
        subjects_recycler=findViewById(R.id.subjects_recyclerview);
        students_recycler=findViewById(R.id.students_recycleview);
        studentArrayList=new ArrayList<>();
        subjects=new ArrayList<>();
        subjectsArrayList=new ArrayList<>();

        nmaeofclass=findViewById(R.id.class_number);
        nmaeofclass.setText(MyStaticClass.classname);
        databaseReferencedetails_subjects=FirebaseDatabase.getInstance().getReference().child("ALL_SUBJECTS");
        databaseReference_subjects=FirebaseDatabase.getInstance().getReference().child("CLASSES").child(MyStaticClass.classname).child("SUBJECTS");
        databaseReference_subjects.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    subjects.add(snapshot.getValue().toString());
                }
                databaseReferencedetails_subjects.addListenerForSingleValueEvent(new ValueEventListener() {
                    Subjects subs;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (int i = 0; i < subjects.size(); i++) {
                            subs = dataSnapshot.child(subjects.get(i)).getValue(Subjects.class);
                            subjectsArrayList.add(subs);
                        }
                        detail_subject_adapter subject_adapter=new detail_subject_adapter(DetailsClassActivity.this,subjectsArrayList,"Classrom");
                        subjects_recycler.setAdapter(subject_adapter);
                        subjects_recycler.setLayoutManager(new LinearLayoutManager(DetailsClassActivity.this));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference().child("CLASSES").child(MyStaticClass.classname).child("ROLLNO");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student;
                if (dataSnapshot.getChildrenCount() == 0) {
                    students_recycler.setPadding(250,250,250,250);
                    students_recycler.setBackground(getDrawable(R.drawable.no_available_student));
                } else {
                    students_recycler.setBackground(null);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        student = snapshot.getValue(Student.class);
                        studentArrayList.add(student);
                    }
                    detail_student_adapter student_adapter = new detail_student_adapter(DetailsClassActivity.this, studentArrayList);
                    students_recycler.setAdapter(student_adapter);
                    students_recycler.setLayoutManager(new LinearLayoutManager(DetailsClassActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
