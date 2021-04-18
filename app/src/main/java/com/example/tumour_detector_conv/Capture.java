package com.example.tumour_detector_conv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Capture extends AppCompatActivity {
    public static final String TAG = "TAG";
    ImageView CimgView;
    Button Img_ClassifyButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        CimgView = findViewById(R.id.camView);
        Img_ClassifyButton = findViewById(R.id.CclassifyB);

        //CAMERA PERMISSION
        if (ContextCompat.checkSelfPermission(Capture.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Capture.this, new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }

        CimgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        Img_ClassifyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


            }
                                              }
        );
    }

    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap cImage = (Bitmap) data.getExtras().get("data");
            CimgView.setImageBitmap(cImage);
            Captureimg(cImage);
        }
    }

    public void Captureimg(Bitmap bitmap){
        ByteArrayOutputStream b_output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,b_output);

        String Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child("CaptureImages")
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
                    }
                });
    }
}

