package com.example.gurukulguru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ResetPassword extends AppCompatActivity {
    Button reset_password;
    EditText new_password,confirm_password;
    String password_text,confirm_password_text,phone_text;
    int backpress=1;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        new_password=findViewById(R.id.reset_password_text);
        confirm_password=findViewById(R.id.confirm_reset_password_text);
        reset_password=findViewById(R.id.password_reset_button);
        phone_text=getIntent().getStringExtra("phoneno");
        reference = FirebaseDatabase.getInstance().getReference().child("TEACHERS");
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_text=new_password.getText().toString();
                confirm_password_text=confirm_password.getText().toString();
                if(password_text.length()==0)
                {
                    new_password.setError("Enter the Password");
                }
                else {
                    if(confirm_password_text.length()==0)
                    {
                        confirm_password.setError("Enter Confirm Password");
                    }
                    else {
                        if(!password_text.equals(confirm_password_text))
                        {
                            confirm_password.setError("Both the passwords did not matched");
                            new_password.setText("");
                            confirm_password.setText("");
                        }
                        else {
                           ChangePasswordinDatabase();
                        }
                    }
                }


            }
        });
    }

    private void ChangePasswordinDatabase() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("password",password_text);
        reference.child(phone_text).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ResetPassword.this, "Password Changed,Now You Can Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPassword.this,TeacherLoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if(backpress>1)
        {
            Intent intent=new Intent(ResetPassword.this, TeacherLoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        else
        {
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to go on Login Page ", Toast.LENGTH_SHORT).show();
        }

    }
}
