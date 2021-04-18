package com.example.tumour_detector_conv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText FullName ,Password , Email;
    Button SignUpB ;
    TextView Welcome, ToLogin;
    FirebaseAuth  fAuth;
    FirebaseFirestore fire_store;
    String userID;
    ImageView name_i, Password_i;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hide Action Bar when you load register
        //getActionBar().hide();

        FullName = findViewById(R.id.SignUpFullName);
        Password = findViewById(R.id.SignUpPassword);
        Email = findViewById(R.id.SignUpEmail);
        SignUpB = findViewById(R.id.SignUpButton);
        Welcome = findViewById(R.id.WelcomeTitle);
        ToLogin = findViewById(R.id.Link_to_Login);
        name_i = findViewById(R.id.Full_name_icon);
        Password_i = findViewById(R.id.Password_icon);

        fAuth = FirebaseAuth.getInstance();
        fire_store = FirebaseFirestore.getInstance();
        progress_bar = findViewById(R.id.progressBar);





        SignUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  email = Email.getText().toString().trim();
                String full_name = FullName.getText().toString();
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

                progress_bar.setVisibility(View.VISIBLE);

                //CODE SENDS USER TO THE MAIN ACTIVITY IF THEY HAVE ALREADY LOGGED IN
                if(fAuth.getCurrentUser() != null ){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }

                //CODE SAVES USERS DETAILS INTO FIREBASE AUTHENTICATION SECTION
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //CODE OUTPUTS THIS IF THE USER HAS BEEN CREATED SUCCESSFULLY
                            Toast.makeText(Register.this, "User Successfully Added", Toast.LENGTH_SHORT).show();
                            //CODE RETRIEVES THE USER ID OF THE CURRENT USER
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fire_store.collection("Medical Practitioners").document(userID);

                            //CODE USED TO INSTALL THE DATA USING A HASH MAP
                            Map<String,Object> user = new HashMap<>();
                            user.put("fullName" ,full_name);
                            user.put("email", email);
                            documentReference.set(user).addOnSuccessListener((OnSuccessListener)  (aVoid) -> {
                                Log.d(TAG, "OnSuccess: Medical Prationer Profile has been created"+ userID);

                            }).addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure" + e.toString());

                                }
                            });

                            //CODE REDIRECTS USER TO THE MAIN PAGE
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{ //OR ELSE .......
                            Toast.makeText(Register.this, "Something is wrong" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);
                        }

                    }

                });
            }
        });

        
        ToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Login.class));
            }

        });



    }
}