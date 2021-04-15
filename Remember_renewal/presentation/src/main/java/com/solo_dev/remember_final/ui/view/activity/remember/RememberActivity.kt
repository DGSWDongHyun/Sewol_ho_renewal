package com.solo_dev.remember_final.ui.view.activity.remember

import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.solo_dev.remember_final.R

class RememberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_remember)
        val scrollview = findViewById<View>(R.id.scroll) as ScrollView
        scrollview.fullScroll(ScrollView.FOCUS_UP)
        scrollview.post { ObjectAnimator.ofInt(scrollview, "scrollY", scrollview.bottom + 100).setDuration(80000).start() }
    }
}