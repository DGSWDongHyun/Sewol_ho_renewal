package com.solo_dev.remember_final.ui.view.activity.main

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.solo_dev.remember_final.R
import com.simple.data.data.module.UIModule
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.solo_dev.remember_final.ui.view.activity.intro.IntroActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var music : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivityForResult(Intent(this, IntroActivity::class.java), COMPLETE)

        music = MediaPlayer.create(this, R.raw.front_flip)
        initMusic()

        val navBottomView : BottomNavigationView = findViewById(R.id.navBottomView)
        val navController = findNavController(R.id.nav_host_fragment)

        navBottomView.setupWithNavController(navController)

    }
    override fun onBackPressed() {
        UIModule.buildEndDialog(this)
    }

    private fun updateIntro() {
        val introLayout : ConstraintLayout = findViewById(R.id.introLayout)
        val animVisible = AnimationUtils.loadAnimation(this, R.anim.slowly_visible)
        val animGone = AnimationUtils.loadAnimation(this, R.anim.slowly_invisible)

        animVisible.fillAfter = true
        animGone.fillAfter = true

        introLayout.animation = animVisible

        Handler().postDelayed(Runnable {
            introLayout.animation = animGone
        }, 2500)
    }

    private fun initMusic() {
        music.isLooping = true
        music.setOnPreparedListener {
            it.start()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == COMPLETE && resultCode == RESULT_OK) {
            updateIntro()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        music.pause()
    }

    override fun onStop(){
        super.onStop()
    }




    override fun onResume() {
        super.onResume()
        music.start()
    }



    override fun onDestroy() {
        super.onDestroy()
        music.pause()
    }

    companion object {
        const val COMPLETE = 1
    }
}