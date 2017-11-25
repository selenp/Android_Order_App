package com.example.bohan.finaldemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * Created by bohan on 11/22/17.
 */

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class SignUpActivity extends Activity {

    private static final String REQUIRED = "Required";
    private DatabaseReference mDatabase;
    private EditText tUsername;
    private EditText tPassword;
    private EditText tEmail;
    private AppCompatButton bUserCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_client);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        tUsername = findViewById(R.id.input_name);
        tPassword = findViewById(R.id.input_password);
        tEmail = findViewById(R.id.input_email);

        bUserCreate = findViewById(R.id.user_create);
        bUserCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }


    private void createAccount(){
        final String name = tUsername.getText().toString();
        final String password = tPassword.getText().toString();
        final String email = tEmail.getText().toString();
        final String userId = "0001";

        if(TextUtils.isEmpty(name)){
            tUsername.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(password)){
            tPassword.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(email)){
            tEmail.setError(REQUIRED);
            return;
        }

        writeNewUser(name, password,email);
        setEditingEnabled(false);

        //final String userId = getUid();
    }

    private void writeNewUser( String name, String password, String email) {
        UserAccount userInfo = new UserAccount(name,password, email);

        mDatabase.child("users").child(name).setValue(userInfo);
    }

    private void setEditingEnabled(boolean enabled) {
        tUsername.setEnabled(enabled);
        tPassword.setEnabled(enabled);
        tEmail.setEnabled(enabled);
        if (enabled) {
            bUserCreate.setVisibility(View.VISIBLE);
        } else {
            bUserCreate.setVisibility(View.GONE);
        }
    }
}
