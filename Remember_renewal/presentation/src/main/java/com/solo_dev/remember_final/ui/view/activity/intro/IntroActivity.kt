package com.solo_dev.remember_final.ui.view.activity.intro

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.databinding.ActivityLoadingBinding

class IntroActivity : AppCompatActivity() {
    private var loadingBinding: ActivityLoadingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        loadingBinding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(loadingBinding!!.root)
        Glide.with(applicationContext).load(R.drawable.yellow).into(loadingBinding!!.ship)
        Handler().postDelayed({

            setResult(RESULT_OK)
            finish()

         }, SPLASH_TIME_OUT.toLong())
    }

    override fun onPause() {
        super.onPause()
        if(isFinishing) {
            this.overridePendingTransition(0, R.anim.slowly_gone)
        }
    }

    override fun onBackPressed() {
        return
    }

    companion object {
        private const val SPLASH_TIME_OUT = 2500
    }
}