package com.solo_dev.remember_final.ui.view.activity.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.solo_dev.remember_final.R;
import com.solo_dev.remember_final.databinding.ActivityLoadingBinding;

public class LoadingActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2500;
    private ActivityLoadingBinding loadingBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingBinding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(loadingBinding.getRoot());

        Glide.with(getApplicationContext()).load(R.drawable.yellow).into(loadingBinding.ship);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    finish();
                }
            }, SPLASH_TIME_OUT);
    }
    @Override
    public void onBackPressed(){
        return;
    }
}