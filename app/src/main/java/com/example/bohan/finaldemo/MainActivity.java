package com.example.bohan.finaldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {

    private static int SLEEP_TIME = 5000;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Intent turn = new Intent(MainActivity.this, TimeActivity.class);
                startActivity(turn);
                finish();
            }
        };
        mHandler.postDelayed(r, SLEEP_TIME);
    }
}




