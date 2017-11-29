package com.example.bohan.finaldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by bohan on 11/17/17.
 */

public class ShowMenuActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener{


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showmenu);

        mAuth = FirebaseAuth.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_client);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.client_menu_logout)
        {
            mAuth.signOut();
            Intent turn = new Intent(ShowMenuActivity.this,CheckActivity.class);
            startActivity(turn);
        }


        return true;
    }
}
