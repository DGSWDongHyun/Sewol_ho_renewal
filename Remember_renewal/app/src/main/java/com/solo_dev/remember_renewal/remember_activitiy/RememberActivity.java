package com.solo_dev.remember_renewal.remember_activitiy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.solo_dev.remember_renewal.R;

import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.view.WindowManager;
import android.widget.ScrollView;

public class RememberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_remember);

        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scroll));

        scrollview.fullScroll(ScrollView.FOCUS_UP);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator.ofInt(scrollview, "scrollY", scrollview.getBottom()+100).setDuration(80000).start();

            }
        });
    }
}