package com.example.tumour_detector_conv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{
    public static final String TAG = "TAG";
    TextView fullName,email;
    FirebaseAuth fire_auth;
    FirebaseFirestore fire_store;
    String userID;
    private CircleImageView Profilepic;
    private static final int PickImage=1;
    private Button ProfileToDash;
    Uri imageUri;


    //Button ProfileToDash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullName = findViewById(R.id.FullName);
        email = findViewById(R.id.Email);
        fire_auth = FirebaseAuth.getInstance();
        fire_store = FirebaseFirestore.getInstance();
        userID = fire_auth.getCurrentUser().getUid();
        Profilepic = (CircleImageView) findViewById(R.id.ProfileP);

        //Navigates to Dashboard
        ProfileToDash = (Button)findViewById(R.id.ToDash);
        ProfileToDash.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                GotoDashBoard();

            }
        });

        DocumentReference documentReference = fire_store.collection("Medical Practitioners").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>(){

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                email.setText(documentSnapshot.getString("email"));
                fullName.setText(documentSnapshot.getString("fullName"));
            }
        });
            //Profile Picture - Image View
            Profilepic.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallery,"Pick your picture"),PickImage);
                }
            });

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
            if (user1.getPhotoUrl() != null){
                Glide.with(this)
                        .load(user1.getPhotoUrl())
                        .into(Profilepic);
            }
    }


    //NAVIGATES THE USER TO THE DASHBOARD
    private void GotoDashBoard() {
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
    }


    //CODE LOGS OUT USER
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PickImage && resultCode == RESULT_OK){
            imageUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                Profilepic.setImageBitmap(bitmap);
                UploadProfileImg(bitmap);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void UploadProfileImg(Bitmap bitmap){
        ByteArrayOutputStream b_output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,b_output);

        String Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child("ProfileImages")
                .child(Userid +".jpeg");

        ref.putBytes(b_output.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(ref);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure",e.getCause());
                    }
                });

    }

    private void getDownloadUrl (StorageReference ref){
        ref.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG,"onSuccess" + uri);
                        setProfile(uri);

                    }
                });
    }

    private void setProfile (Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Profile image loaded successfully" , Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Profile Image could not load ", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}





