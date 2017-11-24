package com.example.bohan.finaldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by bohan on 11/17/17.
 */

public class ChefLoginActivity extends Activity implements View.OnClickListener{

    TextView chefName;
    TextView chefPassword;
    Button chefLogin;
    Button chefCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_chef);
        chefName = (TextView) findViewById(R.id.tChefName);
        chefPassword = (TextView) findViewById(R.id.tChefPassword);
        chefLogin = (Button) findViewById(R.id.bChefLogin);
        chefCancel = (Button) findViewById(R.id.bChefCancel);
        chefLogin.setOnClickListener(this);
        chefCancel.setOnClickListener(this);

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bChefLogin:
                validateChef();
                break;
            case R.id.bChefCancel:
                Intent back = new Intent(ChefLoginActivity.this,CheckActivity.class);
                startActivity(back);
        }

    }




    public void validateChef(){
        boolean valid = true;
        //TextView chefName = (TextView) findViewById(R.id.tChefName);
        //TextView chefPassword = (TextView) findViewById(R.id.tChefPassword);
        String nameChef = chefName.getText().toString();
        String passwordChef = chefPassword.getText().toString();

        if(nameChef.equals("Bo")&&passwordChef.equals("AAA"))
        {
            Intent notifyMenu = new Intent(ChefLoginActivity.this,SignUpActivity.class);
            startActivity(notifyMenu);
        }

    }
}
