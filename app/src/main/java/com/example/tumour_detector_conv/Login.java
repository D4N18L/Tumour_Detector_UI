package com.example.tumour_detector_conv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText Email , Password;
    Button LoginB;
    ImageView email_i , pass_i;
    ProgressBar progressbar;
    TextView login_title , ToSignUp;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.login_email);
        Password= findViewById(R.id.login_password);
        LoginB = findViewById(R.id.Loginb);
        email_i = findViewById(R.id.email_icon);
        pass_i = findViewById(R.id.pass_icon);
        progressbar = findViewById(R.id.progressBar_login);
        login_title = findViewById(R.id.login_title);
        ToSignUp = findViewById(R.id.Link_To_SignUp);
        fAuth = FirebaseAuth.getInstance();

        LoginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                //CODE  CHECKS IF EMAIL  IS  FILLED
                if(TextUtils.isEmpty(email)) {
                    Email.setError("Your email is required to continue");
                    return;
                }
                //CODE CHECKS IF PASSWORD  IS FILLED
                if (TextUtils.isEmpty(password)){
                    Password.setError("Your password is required to continue");
                    return;
                }
                //CODE CHECKS PASSWORD LENGTH
                if(password.length() < 5){
                    Password.setError("Your password must be 5 characters or higher");
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

                //Validate User
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //CODE OUTPUTS THIS IF THE USER HAS BEEN CREATED SUCCESSFULLY
                            Toast.makeText(Login.this, "User has Logged In", Toast.LENGTH_SHORT).show();
                            //CODE REDIRECTS USER TO THE MAIN PAGE
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{ //OR ELSE .......
                            Toast.makeText(Login.this, "Something stopped you from logging in: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }

                    }

                });

            }
        });

        ToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Register.class));
            }

        });

    }
}