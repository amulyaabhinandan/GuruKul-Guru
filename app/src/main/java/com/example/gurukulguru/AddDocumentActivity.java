package com.example.gurukulguru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gurukulguru.ModelClasses.Documents;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDocumentActivity extends AppCompatActivity {
    LinearLayout layout;
    EditText topic_name;
    TextView addtext;
    Uri imguri;
    String topic_text;
    StorageReference storageref;
    DatabaseReference datareference;
    private static final int gpick = 1;
    Button add_btn, reset_btn;
    ImageView addlogo;
    String docurl,mydate;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        add_btn = findViewById(R.id.add_docs);
        addlogo = findViewById(R.id.add_logo);
        addtext = findViewById(R.id.add_text);
        reset_btn = findViewById(R.id.reset_docs);
        topic_name = findViewById(R.id.topicof_document);
        layout = findViewById(R.id.document_selector_layout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading.....");
        mydate= String.valueOf(System.currentTimeMillis());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opengalary();
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                topic_text = topic_name.getText().toString();
                if (imguri == null) {
                    addtext.setError("Please add file");
                } else {
                    if (topic_text.length() <= 3) {
                        topic_name.setError("Please add topic");
                    } else {
                       myfunction();
                    }
                }
            }
        });
    }
    public  void myfunction()
    {
        switch (MyStaticClass.actname)
        {
            case "exampaper" :addlogo.setImageResource(R.drawable.pdf);
                addtext.setVisibility(View.INVISIBLE);
                layout.setEnabled(false);
                topic_name.setEnabled(false);
                progressDialog.show();
                storageref = FirebaseStorage.getInstance().getReference().child("EXAMPAPER").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                datareference = FirebaseDatabase.getInstance().getReference().child("EXAMPAPER").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                SaveDocInDatabase();
                break;
            case "notes" : addlogo.setImageResource(R.drawable.pdf);
                addtext.setVisibility(View.INVISIBLE);
                layout.setEnabled(false);
                topic_name.setEnabled(false);
                progressDialog.show();
                storageref = FirebaseStorage.getInstance().getReference().child("NOTES").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                datareference = FirebaseDatabase.getInstance().getReference().child("NOTES").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                SaveDocInDatabase();
                break;
            case "homework":addlogo.setImageResource(R.drawable.pdf);
                addtext.setVisibility(View.INVISIBLE);
                layout.setEnabled(false);
                topic_name.setEnabled(false);
                progressDialog.show();
                storageref = FirebaseStorage.getInstance().getReference().child("HOMEWORK").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                datareference = FirebaseDatabase.getInstance().getReference().child("HOMEWORK").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                SaveDocInDatabase();
                break;
            case "lecture":   addlogo.setImageResource(R.drawable.video);
                addtext.setVisibility(View.INVISIBLE);
                layout.setEnabled(false);
                topic_name.setEnabled(false);
                progressDialog.show();
                storageref = FirebaseStorage.getInstance().getReference().child("LECTURE").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                datareference = FirebaseDatabase.getInstance().getReference().child("LECTURE").child(MyStaticClass.classname).child(MyStaticClass.subjectname);
                SaveDocInDatabase();
                break;
            default:Log.d("chutiya","chutiya");
        }

    }


    private void Opengalary() {
        Intent galaryint = new Intent();
        galaryint.setAction(Intent.ACTION_GET_CONTENT);
        galaryint.setType(MyStaticClass.type + "/*");
        startActivityForResult(Intent.createChooser(galaryint, "select"), gpick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gpick && resultCode == RESULT_OK && data.getData() != null) {
            imguri = data.getData();
        }
    }

    private void SaveDocInDatabase() {
        final StorageReference storageReference = storageref.child(mydate);
        storageReference.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        docurl = String.valueOf(uri);
                        AddUserData();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddDocumentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AddUserData() {
        Documents docs = new Documents(topic_text, docurl, getDate(),mydate);
        datareference.child(mydate).setValue(docs);
        Toast.makeText(this, "File Uploaded", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        startActivity(new Intent(AddDocumentActivity.this, TeacherHome.class));
        finish();
    }

    private String getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }
}