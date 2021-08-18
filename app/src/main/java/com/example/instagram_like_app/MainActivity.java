package com.example.instagram_like_app;


/*Created by Anuj Kumar Sahu
on August 2020   :)
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText Password , Username;
    Button Login;
    private FirebaseAuth mAuth;
    TextView Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Password = findViewById(R.id.Password);
        Username = findViewById(R.id.Username);
        Login = findViewById(R.id.login);
        Register = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Username.getText().toString();
                String password = Password.getText().toString();
                if(email.isEmpty())
                {
                    Toast.makeText(MainActivity.this , "Please Enter Email Id " , Toast.LENGTH_SHORT).show();
                    Username.requestFocus();
                }
                else if(password.isEmpty())
                {
                    Toast.makeText(MainActivity.this , "Please Enter Password " , Toast.LENGTH_SHORT).show();
                    Password.requestFocus();
                }
                else if(email.isEmpty() && password.isEmpty())
                {
                    Toast.makeText(MainActivity.this , " Fields are Empty. Please Enter Credentials  " , Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && password.isEmpty()))
                    LoginUser(email , password);
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , RegisterActivity.class));
            }
        });


    }

    private void LoginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login UnSuccessful ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Login Successful ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                    finish();
                }

            }

        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        if( FirebaseAuth.getInstance().getCurrentUser() !=null){
            startActivity(new Intent( MainActivity.this, UserActivity.class));
        }
    };
}
