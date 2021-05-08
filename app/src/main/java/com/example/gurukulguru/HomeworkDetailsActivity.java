package com.example.gurukulguru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.gurukulguru.ModelClasses.Student;
import com.example.gurukulguru.ModelClasses.Subjects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeworkDetailsActivity extends AppCompatActivity {
    RecyclerView subjects_recycler;
    ArrayList<String> subjects;
    ArrayList<Subjects> subjectsArrayList;
    DatabaseReference databaseReference_subjects,databaseReferencedetails_subjects;
    String class_no;
    TextView nmaeofclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_details);

        subjects_recycler=findViewById(R.id.subjectinHomework);
        subjects=new ArrayList<>();
        subjectsArrayList=new ArrayList<>();

        class_no=MyStaticClass.classname;
        nmaeofclass=findViewById(R.id.classname_Homework);
        nmaeofclass.setText(class_no);
        databaseReferencedetails_subjects= FirebaseDatabase.getInstance().getReference().child("ALL_SUBJECTS");
        databaseReference_subjects=FirebaseDatabase.getInstance().getReference().child("CLASSES").child(class_no).child("SUBJECTS");
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
                        detail_subject_adapter subject_adapter=new detail_subject_adapter(HomeworkDetailsActivity.this,subjectsArrayList,"Homework");
                        subjects_recycler.setAdapter(subject_adapter);
                        subjects_recycler.setLayoutManager(new LinearLayoutManager(HomeworkDetailsActivity.this));
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
    }
}
