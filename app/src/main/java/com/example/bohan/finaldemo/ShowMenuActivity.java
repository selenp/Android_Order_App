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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bohan on 11/17/17.
 */

public class ShowMenuActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener{


    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
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
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (id == R.id.client_menu_logout)
        {
            mAuth.signOut();
            Intent turnSignOut = new Intent(ShowMenuActivity.this,CheckActivity.class);
            mDatabase.child("customers").child(EncodeString(user.getEmail())).setValue("SignOut");
            startActivity(turnSignOut);
        }

        else if(id == R.id.client_menu_viewcart)
        {
            Intent turnViewCart = new Intent(ShowMenuActivity.this,ViewCartActivity.class);
            startActivity(turnViewCart);
        }


        return true;
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}
