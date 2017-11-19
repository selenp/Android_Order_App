package com.example.bohan.finaldemo;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

/**
 * Created by bohan on 11/17/17.
 */

public class ClientLoginActivity extends Activity implements View.OnClickListener{

    Button loginButton;
    Button cancelButton;
    TextView userName;
    TextView userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_client);
        loginButton = (Button) findViewById(R.id.bUserLogin);
        cancelButton = (Button) findViewById(R.id.bUserCancel);
        userName = (TextView) findViewById(R.id.tUserName);
        userPassword = (TextView) findViewById(R.id.tUserPassword);
        //TextView signIn = (TextView) findViewById(R.id.signInUser);
        loginButton.getBackground().setAlpha(0);
        cancelButton.getBackground().setAlpha(0);
        loginButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        //signIn.setTextColor(signIn.getTextColors().withAlpha(255));
        //signIn.getBackground().setAlpha(0);
    }

    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.bUserLogin:
                validateUser();
                break;
            case R.id.bUserCancel:
                Intent back =new Intent(ClientLoginActivity.this,CheckActivity.class);
                startActivity(back);
                break;
        }

    }
    public void validateUser(){
        boolean valid = true;
        //TextView userName = (TextView) findViewById(R.id.tUserName);
        //TextView userPassword = (TextView) findViewById(R.id.tUserPassword);
        String nameUser = userName.getText().toString();
        String passwordUser = userPassword.getText().toString();

        if(nameUser.equals("Bo")&&passwordUser.equals("AAA"))
        {
            Intent notifyMenu = new Intent(ClientLoginActivity.this,ShowMenuActivity.class);
            startActivity(notifyMenu);
        }

    }
}
