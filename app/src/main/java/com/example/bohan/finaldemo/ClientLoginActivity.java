package com.example.bohan.finaldemo;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bohan on 11/17/17.
 */

public class ClientLoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button loginButton;
    Button cancelButton;
    Button createButton;
    TextView userEmail;
    TextView userPassword;
    TextView userStatus;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private final String TAG = "FB_SIGNIN";

    //private DatabaseReference mUserReference;
    //private DataSnapshot snapShot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_client);
        loginButton = (Button) findViewById(R.id.bUserLogin);
        cancelButton = (Button) findViewById(R.id.bUserCancel);
        createButton = (Button) findViewById(R.id.bUserCreate);
        userEmail = (TextView) findViewById(R.id.tUserName);
        userPassword = (TextView) findViewById(R.id.tUserPassword);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loginButton.getBackground().setAlpha(0);
        cancelButton.getBackground().setAlpha(0);
        createButton.getBackground().setAlpha(0);

        loginButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        createButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Log.d(TAG, "Singed in" + user.getUid());
                }
                else
                {
                    Log.d(TAG,"Currently Singed Out");
                }
            }
        };

        updateStatus();

    }


    @Override
    public void onStart() {
        super.onStart();
        // TODO: add the AuthListener
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO: Remove the AuthListener
        if(mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }




    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.bUserLogin:
                signUserIn();
                break;

            case R.id.bUserCreate:
                createUserAccount();
                break;

            case R.id.bUserCancel:
                Intent back =new Intent(ClientLoginActivity.this,CheckActivity.class);
                startActivity(back);
                break;
        }

    }

    private boolean checkFormFields() {
        String email, password;

        email = userEmail.getText().toString();
        password = userPassword.getText().toString();

        if (email.isEmpty()) {
            userEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()){
            userPassword.setError("Password Required");
            return false;
        }

        return true;
    }

    private void updateStatus() {
        userStatus = (TextView)findViewById(R.id.userSignStatus);
        // TODO: get the current user

        FirebaseUser user = mAuth.getCurrentUser();


        if (user != null) {
            userStatus.setText("Signed in: " + user.getEmail());
        }
        else {
            userStatus.setText("Signed Out");
        }

    }

    private void signUserIn() {
        if (!checkFormFields())
            return;

        final String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        // TODO: sign the user in with email and password credentials
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ClientLoginActivity.this, "Signed In", Toast.LENGTH_SHORT).show();

                            mDatabase.child("customers").child(EncodeString(email)).setValue("SignIn");
                            Intent showMenu = new Intent(ClientLoginActivity.this, ShowMenuActivity.class);
                            startActivity(showMenu);
                        }
                        else
                        {
                            Toast.makeText(ClientLoginActivity.this, "Signed In Failed", Toast.LENGTH_SHORT).show();
                        }
                        updateStatus();
                    }
                });

    }

    private void createUserAccount() {
        if (!checkFormFields())
            return;

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ClientLoginActivity.this, "User was created", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ClientLoginActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    private void updateStatus(String stat) {
        userStatus = (TextView)findViewById(R.id.userSignStatus);
        userStatus.setText(stat);
    }

    private void signUserOut() {
        // TODO: sign the user out
        final String email = userEmail.getText().toString();
        mAuth.signOut();
        mDatabase.child("customers").child(EncodeString(email)).setValue("SignOut");
        updateStatus();
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}
