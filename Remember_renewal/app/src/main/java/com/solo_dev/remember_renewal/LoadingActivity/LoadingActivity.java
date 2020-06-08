package com.solo_dev.remember_renewal.LoadingActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.solo_dev.remember_renewal.MainActivity;
import com.solo_dev.remember_renewal.R;

public class LoadingActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        ImageView imgs = findViewById(R.id.ship);
        Glide.with(getApplicationContext()).load(R.raw.ship_going).into(imgs);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    finish();
                }
            }, SPLASH_TIME_OUT);
    }
}