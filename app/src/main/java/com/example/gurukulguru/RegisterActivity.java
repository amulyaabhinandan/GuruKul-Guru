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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private static final int gpick = 1;
    EditText name, phone, aadhaar, confirm_password, password, email;
    Button verify_btn, register_btn;
    LinearLayout add_image_layout;
    ImageView profile_pic;
    String name_txt, phone_txt, aadhaaar_txt, confirm_password_txt, password_txt, email_txt;
    Uri imguri;
    StorageReference storageref;
    static String profilePicUrl;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        add_image_layout = findViewById(R.id.profile_add_layout);
        profile_pic = findViewById(R.id.profile_pic_add);
        verify_btn = findViewById(R.id.verify);
        register_btn = findViewById(R.id.register);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        aadhaar = findViewById(R.id.aadhaar);
        confirm_password = findViewById(R.id.confirm_password);
        password = findViewById(R.id.reg_password);
        email = findViewById(R.id.email);

        reference = FirebaseDatabase.getInstance().getReference().child("TEACHERS");
        storageref = FirebaseStorage.getInstance().getReference("TEACHERS");
        register_btn.setVisibility(View.INVISIBLE);
        register_btn.setEnabled(false);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_txt = name.getText().toString();
                phone_txt = phone.getText().toString();
                password_txt = password.getText().toString();
                confirm_password_txt = confirm_password.getText().toString();
                email_txt = email.getText().toString();
                aadhaaar_txt = aadhaar.getText().toString();
                if (check(name_txt, password_txt, phone_txt, aadhaaar_txt, email_txt, confirm_password_txt)) {
                    //if all are filled then here
                    if (!(phone_txt.length() == 10)) {
                        phone.setError("ENTER CORRECT PHONE");
                    } else {
                        if ((password_txt.equals(confirm_password_txt))) {

                            if (aadhaaar_txt.length() == 12) {
                                name.setEnabled(false);
                                phone.setEnabled(false);
                                confirm_password.setEnabled(false);
                                password.setEnabled(false);
                                aadhaar.setEnabled(false);
                                email.setEnabled(false);
                                add_image_layout.setEnabled(false);
                                verify_btn.setEnabled(false);
                                verify_btn.setVisibility(View.INVISIBLE);
                                checkAccount();

                            } else {
                                aadhaar.setError("ENTER CORRECT AADHAAR");
                            }
                        } else {
                            password.setText("");
                            confirm_password.setText("");
                            confirm_password.setError("BOTH PASSWORDS MUST BE SAME");
                        }
                    }
                }

            }
        });
        add_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imguri == null) {
                    profilePicUrl = "https://firebasestorage.googleapis.com/" +
                            "v0/b/gurukul-f0593.appspot.com/o/USER%2Fstudent.png?alt=media&token=b6196674-18d7-4899-9dab-a33eddab93ae";
                    AddUserData();
                } else {
                    //if upload pic
                    SavePicInDatabase();
                }
            }
        });
    }

    private void checkAccount() {
        DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("TEACHERS").child(phone_txt).exists()) {
                    Toast.makeText(RegisterActivity.this,"Account Already Exists,Redirecting to Login Page",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,TeacherLoginActivity.class));
                }
                else {
                    VerifyMyNumber("+91" + phone_txt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddUserData() {
        Teachers teachers = new Teachers(name_txt, phone_txt, aadhaaar_txt, profilePicUrl, email_txt, password_txt);
        reference.child(phone_txt).setValue(teachers);
        Toast.makeText(this, "NOW U CAN LOGIN", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, TeacherLoginActivity.class));
        finish();
    }

    private void SavePicInDatabase() {
        final StorageReference storageReference = storageref.child(phone_txt);
        storageReference.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profilePicUrl = String.valueOf(uri);
                        AddUserData();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            profile_pic.setImageURI(imguri);
        }
    }

    private void VerifyMyNumber(final String s) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(s, 30L, TimeUnit.SECONDS, RegisterActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(RegisterActivity.this, "Phone Verified", Toast.LENGTH_SHORT).show();
                register_btn.setEnabled(true);
                register_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(RegisterActivity.this, "OTP Sent...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean check(String name_txt, String password_txt, String phone_txt, String aadhaaar_txt, String email_txt, String confirm_password_txt) {
        Boolean result = false;
        int n = 0;
        if (name_txt.length() == 0) {
            name.setError("PLEASE ENTER NAME");
            n = 1;
        }
        if (password_txt.length() == 0) {
            password.setError("PLEASE ENTER PASSWORD");
            n = n + 1;
        }
        if (phone_txt.length() == 0) {
            phone.setError("PLEASE ENTER NUMBER");
            n = n + 1;
        }
        if (aadhaaar_txt.length() == 0) {
            aadhaar.setError("PLEASE ENTER AADHAAR");
            n = n + 1;
        }
        if (email_txt.length() == 0) {
            email.setError("PLEASE ENTER EMAIL");
            n = n + 1;
        }
        if (confirm_password_txt.length() == 0) {
            confirm_password.setError("PLEASE ENTER PASSWORD AGAIN");
            n = n + 1;
        }
        if (n == 0) {
            result = true;
        }
        return result;
    }
}