package com.example.bohan.finaldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by bohan on 11/17/17.
 */

public class ChefLoginActivity extends Activity implements View.OnClickListener{

    TextView chefEmail;
    TextView chefPassword;
    Button chefLogin;
    Button chefCancel;
    TextView chefStatus;

    private final String TAG = "FB_SIGNIN";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_chef);
        chefEmail = (TextView) findViewById(R.id.tChefName);
        chefPassword = (TextView) findViewById(R.id.tChefPassword);
        chefLogin = (Button) findViewById(R.id.bChefLogin);
        chefCancel = (Button) findViewById(R.id.bChefCancel);
        chefLogin.setOnClickListener(this);
        chefCancel.setOnClickListener(this);

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

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bChefLogin:
                signChefIn();
                break;
            case R.id.bChefCancel:
                Intent back = new Intent(ChefLoginActivity.this,CheckActivity.class);
                startActivity(back);
        }

    }

    private void signChefIn() {
        if (!checkFormFields())
            return;

        String email = chefEmail.getText().toString();
        String password = chefPassword.getText().toString();

        // TODO: sign the user in with email and password credentials
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ChefLoginActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                            Intent showMenu = new Intent(ChefLoginActivity.this, ShowMenuActivity.class);
                            startActivity(showMenu);
                        }
                        else
                        {
                            Toast.makeText(ChefLoginActivity.this, "Signed In Failed", Toast.LENGTH_SHORT).show();
                        }
                        updateStatus();
                    }
                });

    }

    private boolean checkFormFields() {
        String email, password;

        email = chefEmail.getText().toString();
        password = chefPassword.getText().toString();

        if (email.isEmpty()) {
            chefEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()){
            chefPassword.setError("Password Required");
            return false;
        }

        return true;
    }

    private void updateStatus() {
        chefStatus = (TextView)findViewById(R.id.chefSignStatus);
        // TODO: get the current user

        FirebaseUser user = mAuth.getCurrentUser();


        if (user != null) {
            chefStatus.setText("Signed in: " + user.getEmail());
        }
        else {
            chefStatus.setText("Signed Out");
        }

    }

    private void updateStatus(String stat) {
        chefStatus = (TextView)findViewById(R.id.chefSignStatus);
        chefStatus.setText(stat);
    }





}
