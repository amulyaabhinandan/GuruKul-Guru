package com.example.gurukulguru;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gurukulguru.ModelClasses.Teachers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class UpdateActivity extends AppCompatActivity {
    EditText update_name, update_aadhaar, update_confirm_password, update_password, update_email;
    Button update_btn;
    LinearLayout update_add_image_layout;
    ImageView update_profile_pic;
    String update_name_txt, update_aadhaaar_txt, update_confirm_password_txt, update_password_txt, update_email_txt;
    Uri imguri;
    StorageReference storageref;
    static String profilePicUrl;
    DatabaseReference reference;
    private static final int gpick = 1;
    int backpress=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        update_aadhaar = findViewById(R.id.update_aadhaar);
        update_add_image_layout = findViewById(R.id.update_profile_add_layout);
        update_confirm_password = findViewById(R.id.update_confirm_password);
        update_name = findViewById(R.id.update_name);
        update_password = findViewById(R.id.update_password);
        update_email = findViewById(R.id.update_email);
        update_btn = findViewById(R.id.update_info_btn);
        update_profile_pic = findViewById(R.id.update_profile_pic_add);

        update_name.setText(MyStaticClass.currentOnlineTeacher.getName());
        update_password.setText(MyStaticClass.currentOnlineTeacher.getPassword());
        update_confirm_password.setText(MyStaticClass.currentOnlineTeacher.getPassword());
        update_email.setText(MyStaticClass.currentOnlineTeacher.getEmail());
        update_aadhaar.setText(MyStaticClass.currentOnlineTeacher.getAadhaar());
        Picasso.get().load(MyStaticClass.currentOnlineTeacher.getPicUrl()).into(update_profile_pic);
        storageref= FirebaseStorage.getInstance().getReference();
        reference= FirebaseDatabase.getInstance().getReference();

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update_name_txt = update_name.getText().toString();
                update_password_txt = update_password.getText().toString();
                update_confirm_password_txt = update_confirm_password.getText().toString();
                update_email_txt = update_email.getText().toString();
                update_aadhaaar_txt = update_aadhaar.getText().toString();
                if (check(update_name_txt, update_password_txt, update_aadhaaar_txt, update_email_txt, update_confirm_password_txt)) {
                    if ((update_password_txt.equals(update_confirm_password_txt))) {

                        if (update_aadhaaar_txt.length() == 12) {
                            update_name.setEnabled(false);
                            update_confirm_password.setEnabled(false);
                            update_password.setEnabled(false);
                            update_aadhaar.setEnabled(false);
                            update_email.setEnabled(false);
                            update_add_image_layout.setEnabled(false);
                            if (MyStaticClass.currentOnlineTeacher.getPicUrl().equals("https://firebasestorage.googleapis.com/v0/b/gurukul-f0593.appspot.com/o/USER%2Fstudent.png?alt=media&token=b6196674-18d7-4899-9dab-a33eddab93ae")) {
                                Toast.makeText(UpdateActivity.this, "Please Add Profile Picture", Toast.LENGTH_SHORT).show();
                                update_add_image_layout.setEnabled(true);
                            }
                            else {
                                SavePicInDatabase();
                            }
                        } else {
                            update_aadhaar.setError("ENTER CORRECT AADHAAR");
                        }
                    } else {
                        update_password.setText("");

                        update_confirm_password.setText("");
                        update_confirm_password.setError("BOTH PASSWORDS MUST BE SAME");
                    }
                }

            }
        });
        update_add_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
    }

    private void SavePicInDatabase() {
        final StorageReference storageReference = storageref.child("TEACHERS").child(MyStaticClass.currentOnlineTeacher.getPhone());
        if(imguri==null) {
            profilePicUrl=MyStaticClass.currentOnlineTeacher.getPicUrl();
            updateAccount();
        }
        else {
            storageReference.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profilePicUrl = String.valueOf(uri);
                            updateAccount();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateAccount() {
       Teachers teachers=new Teachers(update_name_txt,MyStaticClass.currentOnlineTeacher.getPhone(),update_aadhaaar_txt,profilePicUrl,update_email_txt,update_password_txt);
        reference.child("TEACHERS").child(MyStaticClass.currentOnlineTeacher.getPhone()).setValue(teachers).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UpdateActivity.this, "Account info Updated", Toast.LENGTH_SHORT).show();
                Paper.book().destroy();
                MyStaticClass.currentOnlineTeacher=null;
                startActivity(new Intent(UpdateActivity.this,TeacherLoginActivity.class));
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateActivity.this,TeacherHome.class));
                finish();
            }
        });
    }

    private void OpenGallery() {
        Intent galaryint = new Intent();
        galaryint.setAction(Intent.ACTION_GET_CONTENT);
        galaryint.setType("image/*");
        startActivityForResult(Intent.createChooser(galaryint, "select"), gpick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gpick && resultCode == RESULT_OK && data.getData() != null) {
            imguri = data.getData();
            update_profile_pic.setImageURI(imguri);
            MyStaticClass.currentOnlineTeacher.setPicUrl("okay");
        }
    }

    private boolean check(String update_name_txt, String update_password_txt, String update_aadhaaar_txt, String update_email_txt, String update_confirm_password_txt) {
        Boolean result = false;
        int n = 0;
        if (update_name_txt.length() == 0) {
            update_name.setError("PLEASE ENTER NAME");
            n = 1;
        }
        if (update_password_txt.length() == 0) {
            update_password.setError("PLEASE ENTER PASSWORD");
            n = n + 1;
        }
        if (update_aadhaaar_txt.length() == 0) {
            update_aadhaar.setError("PLEASE ENTER AADHAAR");
            n = n + 1;
        }
        if (update_email_txt.length() == 0) {
            update_email.setError("PLEASE ENTER EMAIL");
            n = n + 1;
        }
        if (update_confirm_password_txt.length() == 0) {
            update_confirm_password.setError("PLEASE ENTER PASSWORD AGAIN");
            n = n + 1;
        }
        if (n == 0) {
            result = true;
        }
        return result;
    }
    @Override
    public void onBackPressed() {

        if (backpress > 1) {
            startActivity(new Intent(this,TeacherHome.class));
            finishAffinity();
        } else {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to go back ", Toast.LENGTH_SHORT).show();
        }
    }
}
