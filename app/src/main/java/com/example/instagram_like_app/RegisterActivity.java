package com.example.instagram_like_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText Pass , Email , Name, UserName;
    Button Register;
    private FirebaseAuth mAuth;
    TextView Title;
    private DatabaseReference mRef;
    ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email = findViewById(R.id.Email);
        Pass = findViewById(R.id.pass);
        UserName = findViewById(R.id.UserName);
        Name = findViewById(R.id.Name);
        Register = findViewById(R.id.Reg);
        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth  = FirebaseAuth.getInstance();
        p =new ProgressDialog(this);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String pass = Pass.getText().toString();
                String name = Name.getText().toString();
                String username = UserName.getText().toString();

                if(email.isEmpty()|| pass.isEmpty()||name.isEmpty()||username.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }
                if(pass.length()<=6)
                {
                    Toast.makeText(RegisterActivity.this, "Password too short. length should have more than 6", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RegisterUser(email,pass,name,username);
                }

            }
        });


    }

    private void RegisterUser(final String email, final String pass, final String Name, String username) {
       p.setMessage("Please Wait. We are Working on it");
       p.show();
        mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String , Object> hash = new HashMap<>();
                hash.put("Email",email);
                hash.put("Password" ,pass);
                hash.put("Name" , Name);
                hash.put("id", mAuth.getCurrentUser().getUid());
                mRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(hash).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            p.dismiss();
                            Toast.makeText(RegisterActivity.this, "Update Profile for Better Experience", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               p.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
