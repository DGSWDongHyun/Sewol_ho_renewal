package com.solo_dev.remember_final.ui.view.activity.main

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val i = Intent(this, IntroActivity::class.java)
        startActivity(i)

        val navBottomView : BottomNavigationView = findViewById(R.id.navBottomView)
        val navController = findNavController(R.id.nav_host_fragment)


        navBottomView.setupWithNavController(navController)

    }
    override fun onBackPressed() {
        UIModule.buildEndDialog(this)
    }
}