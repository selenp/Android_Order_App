package com.example.bohan.finaldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by bohan on 11/23/17.
 */

public class TimeActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_check);

        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /*
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                    finish();
                }
            }
        };
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {


            Intent signInUser = new Intent(this,ClientLoginActivity.class);
            startActivity(signInUser);

        } else if (id == R.id.nav_settings) {
            Intent logOutUser = new Intent(this,ClientLoginActivity.class);
            startActivity(logOutUser);
        } else if (id == R.id.client_menu_logout) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
