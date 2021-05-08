package com.example.gurukulguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gurukulguru.ModelClasses.Subjects;
import com.example.gurukulguru.cards.ClassRoom_card;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Add_Class extends AppCompatActivity {
    DatabaseReference databaseReference, databaseReference2;
    ArrayList<Subjects> mylist;
    RecyclerView sub_container;
    Subjects_Adapter subjects_adapter;
    Button add_class, check_class_btn, reset_btn, cancel_btn;
    LinearLayout check_container;
    EditText class_check_text;
    String s;
    int backpress=1;
    ArrayList<Subjects> chk_sub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__class);

        check_class_btn = findViewById(R.id.check_button);
        add_class = findViewById(R.id.add_class_btn);
        reset_btn = findViewById(R.id.reset_btn);
        cancel_btn = findViewById(R.id.cancel_btn);

        check_container = findViewById(R.id.class_check);

        class_check_text = findViewById(R.id.class_check_text);


        add_class.setEnabled(false);
        reset_btn.setEnabled(false);
        cancel_btn.setEnabled(false);
        class_check_text.setEnabled(true);
        check_class_btn.setEnabled(true);

        sub_container = findViewById(R.id.ADD_SUBJECTS);
        mylist = new ArrayList<>();
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("CLASSES");


        check_class_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = class_check_text.getText().toString();
                if (s.length() == 0) {
                    Toast.makeText(Add_Class.this, "please enter class", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("CLASS" + s).exists()) {
                                Toast.makeText(Add_Class.this, "exist", Toast.LENGTH_SHORT).show();
                            } else {
                                add_class.setEnabled(true);
                                reset_btn.setEnabled(true);
                                cancel_btn.setEnabled(true);
                                class_check_text.setEnabled(false);
                                check_class_btn.setEnabled(false);
                                sub_container.setEnabled(true);
                                sub_container.setVisibility(View.VISIBLE);
                                load_subjects();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_class.setEnabled(false);
                reset_btn.setEnabled(false);
                cancel_btn.setEnabled(false);
                class_check_text.setEnabled(true);
                check_class_btn.setEnabled(true);
                sub_container.setEnabled(false);
                sub_container.setVisibility(View.INVISIBLE);
                class_check_text.setText("");
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add_Class.this, ClassRoom_card.class));
            }
        });

        add_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getsubjects();
                if (chk_sub.size() == 0) {
                    Toast.makeText(Add_Class.this, "please select atleast one subject", Toast.LENGTH_SHORT).show();
                } else {
                    createclass();
                }

            }
        });
    }

    private void createclass() {
        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> class_data=new HashMap<>();
        for(int i=0;i<chk_sub.size();i++) {
            class_data.put(String.valueOf(chk_sub.get(i).getS_NAME()),chk_sub.get(i).getS_CODE());
        }
        rootref.child("CLASSES").child("CLASS"+s).child("SUBJECTS").updateChildren(class_data);
        Toast.makeText(Add_Class.this,"Class "+s+" created ",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Add_Class.this,TeacherHome.class));
        finishAffinity();
    }

    private void getsubjects() {
        chk_sub = new ArrayList<>();
        for (int i = 0; i < subjects_adapter.getItemCount(); i++) {
            if (subjects_adapter.getitem(i).getIschk()) {
                chk_sub.add(subjects_adapter.getitem(i));
            }
        }
    }

    private void load_subjects() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ALL_SUBJECTS");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Subjects subjects;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    subjects = snapshot.getValue(Subjects.class);
                    mylist.add(subjects);
                }
                subjects_adapter = new Subjects_Adapter(getApplicationContext(), mylist);
                sub_container.setLayoutManager(new LinearLayoutManager(Add_Class.this));
                sub_container.setAdapter(subjects_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {

        if (backpress > 1) {
            startActivity(new Intent(Add_Class.this,TeacherHome.class));
            finishAffinity();
        } else {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
    }
}
