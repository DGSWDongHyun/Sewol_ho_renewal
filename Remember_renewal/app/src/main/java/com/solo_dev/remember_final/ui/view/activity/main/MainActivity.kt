package com.solo_dev.remember_final.ui.view.activity.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.ui.view.activity.intro.LoadingActivity
import com.solo_dev.remember_final.ui.view.activity.login.GoogleLoginActivity
import com.solo_dev.remember_final.ui.view.activity.remember.RememberActivity
import com.solo_dev.remember_final.ui.view.activity.write.WriteActivity
import com.wooplr.spotlight.SpotlightView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var dateTextView : TextView?= null
    private var leftDateTextView : TextView?= null
    private var mAuth: FirebaseAuth? = null
    private var firstRun : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLayout()
        initializeMusic()
        clickFAB()

        mAuth = FirebaseAuth.getInstance()
       if (mAuth!!.currentUser == null) {
           finish()
            startActivity(Intent(this, GoogleLoginActivity::class.java))
        }
        val i = Intent(applicationContext, LoadingActivity::class.java)
        startActivity(i)
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_FILES)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_FILES -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "승인이 허가되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "승인 후에 게시글에 이미지를 첨부하실 수 있습니다.", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun initLayout() {
        val dateNow = Date(System.currentTimeMillis())
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, 4)
        calendar.set(Calendar.DAY_OF_MONTH, 16)

        val date = Date()
        dateTextView = findViewById(R.id.dateTextView)
        leftDateTextView = findViewById(R.id.leftDateTextView)
        dateTextView?.text = "오늘은 "+returnDate("MM")+"월 "+returnDate("dd")+"일 입니다."
        leftDateTextView?.text = calculateDate(calendar.time, dateNow)

    }

    private fun calculateDate(dateF : Date, dateS : Date) : String {

        val calculateLong = dateF.time - dateS.time
        val calculateDays = calculateLong / (24*60*60*1000)

        val dateCal = Math.abs(calculateDays)

        return "4월 16일까지.. '$dateCal'일 남았습니다."
    }

    private fun returnDate(format : String) : String {
        return SimpleDateFormat(format).format(Date(System.currentTimeMillis()))
    }

    private fun clickFAB() {
        firstRun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean("firstRun", true)
        material_design_floating_action_menu_item1!!.setOnClickListener {
            if (firstRun) {
                buildSpotlight()
            } else {
                startActivity(Intent(applicationContext, WriteActivity::class.java))
            }
        }
        material_design_floating_action_menu_item2!!.setOnClickListener {
            if (firstRun) {
                buildSpotlight()
            } else {
                startActivity(Intent(applicationContext, RememberActivity::class.java))
            }
        }
    }

    private fun buildSpotlight() {
        SpotlightView.Builder(this@MainActivity)
                .introAnimationDuration(400)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#FFFFFF"))
                .headingTvSize(32)
                .headingTvText("Remember 2.0을\n소개합니다.")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("해당 공간은 한 마디 씩 길게, 짧게 쓰는,\n작은 공간입니다.")
                .maskColor(Color.parseColor("#dc000000"))
                .target(material_design_floating_action_menu_item1)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#FFFFFF"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("FDE!@@#") //UNIQUE ID
                .setListener {
                    SpotlightView.Builder(this@MainActivity)
                            .introAnimationDuration(400)
                            .performClick(true)
                            .fadeinTextDuration(400)
                            .headingTvColor(Color.parseColor("#FFFFFF"))
                            .headingTvSize(32)
                            .headingTvText("Remember 2.0을 소개합니다.")
                            .subHeadingTvColor(Color.parseColor("#FFFFFF"))
                            .subHeadingTvSize(16)
                            .subHeadingTvText("해당 공간은 개발자 소감,\n세월호 304명을 추모하기 위한 곳입니다.")
                            .maskColor(Color.parseColor("#dc000000"))
                            .target(material_design_floating_action_menu_item2)
                            .lineAnimDuration(400)
                            .lineAndArcColor(Color.parseColor("#FFFFFF"))
                            .dismissOnTouch(true)
                            .dismissOnBackPress(true)
                            .enableDismissAfterShown(true)
                            .usageId("FDE!@@#2") //UNIQUE ID
                            .show()
                }
                .show()

                getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("firstRun", false)
                .commit()
    }

    private fun buildEndDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.turn_off, null)
        builder.setCancelable(true)
        builder.setView(view)
        val ok = view.findViewById<View>(R.id.ok) as Button
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val videoView = view.findViewById<VideoView>(R.id.videoView)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" +
                R.raw.turnoff_))
        videoView.start()
        ok.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }


    private fun initializeMusic() {
        mp_flip = MediaPlayer.create(this, R.raw.front_flip)
        mp_flip?.isLooping = true
        mp_flip?.start()
    }


    override fun onBackPressed() {
        buildEndDialog()
    }



    public override fun onDestroy() {
        mp_flip!!.stop()
        super.onDestroy()
    }

    companion object {
        private var mp_flip: MediaPlayer? = null
        private const val MY_PERMISSIONS_REQUEST_FILES = 653
    }
}