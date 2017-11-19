package com.example.bohan.finaldemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by bohan on 11/17/17.
 */

public class CheckActivity extends Activity{

    Button user, chef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkuser);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        final Context context = this;

        user = (Button) findViewById(R.id.bUser);
        chef = (Button) findViewById(R.id.bChef);

        user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intentClient = new Intent(context, ClientLoginActivity.class);
                startActivity(intentClient);

            }

        });

        chef.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intentChef = new Intent(context, ChefLoginActivity.class);
                startActivity(intentChef);

            }

        });

    }
}
