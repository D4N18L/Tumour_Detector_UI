package com.example.tumour_detector_conv;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class DashBoard extends AppCompatActivity {

        DrawerLayout drawerLayout;
        ActionBarDrawerToggle actionBarDrawerToggle;
        private ImageView Upload;
        private ImageView Capture;
        NavigationView navigationView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
                setUpToolbar();


                navigationView = (NavigationView) findViewById(R.id.navigationview);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                switch (menuItem.getItemId())
                                {
                                        case  R.id.dashboard:

                                                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                                                break;

                                        case R.id.upload_pic:
                                                startActivity(new Intent(getApplicationContext(), Upload.class));
                                                break;
                                        case R.id.capture_pic:
                                                startActivity(new Intent(getApplicationContext(), Capture.class));
                                                break;
                                        case R.id.profile:
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                break;


//Paste your privacy policy link

//                    case  R.id.nav_Policy:{
//
//                        Intent browserIntent  = new Intent(Intent.ACTION_VIEW , Uri.parse(""));
//                        startActivity(browserIntent);
//
//                    }
                                        //       break;
                                }
                                return false;
                        }
                });
        }





        public void setUpToolbar() {
                drawerLayout = findViewById(R.id.drawer);
                Toolbar toolbar = findViewById(R.id.toolbar);
                setTitle("DashBoard");
                setSupportActionBar(toolbar);
                actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
                drawerLayout.addDrawerListener(actionBarDrawerToggle);
                actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
                actionBarDrawerToggle.syncState();

        }



}





