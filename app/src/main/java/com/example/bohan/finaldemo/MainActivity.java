package com.example.bohan.finaldemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends Activity {

    private static int SLEEP_TIME = 5000;
    private Handler mHandler = new Handler();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean status;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Runnable r = new Runnable() {

            @Override
            public void run() {
                if(user != null)
                {
                    Intent turn = new Intent(MainActivity.this,ShowMenuActivity.class);

                    String userEmail = user.getEmail();
                    mDatabase.child("customers").setValue(userEmail);
                    startActivity(turn);
                    finish();
                }
                else
                        {
                                Intent turn = new Intent(MainActivity.this, CheckActivity.class);
                                startActivity(turn);
                                finish();
                        }

            }
        };
        mHandler.postDelayed(r, SLEEP_TIME);
    }

    }





