package com.solo_dev.remember_final.ui.view.activity.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.data.module.UIModule
import com.solo_dev.remember_final.ui.view.activity.intro.IntroActivity
import com.solo_dev.remember_final.ui.view.activity.login.GoogleLoginActivity
import com.solo_dev.remember_final.ui.view.activity.remember.RememberActivity
import com.solo_dev.remember_final.ui.view.activity.write.WriteActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var music : MediaPlayer
    private var dateTextView : TextView?= null
    private var leftDateTextView : TextView?= null
    private var updateTextView : TextView ?= null
    private var mAuth: FirebaseAuth? = null
    var firstRun : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initLayout()
        initMusic()
        clickFAB()

        mAuth = FirebaseAuth.getInstance()
       if (mAuth!!.currentUser == null) {
            startActivity(Intent(this, GoogleLoginActivity::class.java))
        }

        val i = Intent(applicationContext, IntroActivity::class.java)
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
        val calendar = Calendar.getInstance()

        updateTextView = findViewById(R.id.updateDate)
        dateTextView = findViewById(R.id.dateTextView)
        leftDateTextView = findViewById(R.id.leftDateTextView)
        dateTextView?.text = "오늘은 "+returnDate("MM")+"월 "+returnDate("dd")+"일 입니다."
        updateTextView?.text = "업데이트까지.. ${calculateDate(calendar.get(Calendar.YEAR), 4, 16)}일 남았습니다."
        leftDateTextView?.text = "4월 16일까지.. ${calculateDate(calendar.get(Calendar.YEAR), 4, 16)}일 남았습니다."

    }

    private fun calculateDate(year: Int, month: Int, day: Int) : String {
        return try {
            var yyear = year
            var mmonth = month
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val todaCal = Calendar.getInstance() //오늘날자 가져오
            val ddayCal = Calendar.getInstance() //오늘날자를 가져와 변경시킴

            mmonth -= 1 // 받아온날자에서 -1을 해줘야함.

            ddayCal[year, mmonth] = day // D-day의 날짜를 입력

            val today = todaCal.timeInMillis / 86400000 //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)

            var dDay = ddayCal.timeInMillis / 86400000
            var count = dDay - today // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.

            if(count < 0) {
                yyear += 1

                ddayCal[yyear, mmonth] = day

                dDay = ddayCal.timeInMillis / 86400000
                count = dDay - today // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            }


            if(count > 0) {"${dDay - today}"} else { "4월 16일입니다." }
        } catch (e: Exception) {
            e.printStackTrace()
            (-1).toString()
        }
    }

    private fun returnDate(format: String) : String {
        return SimpleDateFormat(format).format(Date(System.currentTimeMillis()))
    }

    private fun clickFAB() {
        firstRun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean("firstRun", true)
        material_design_floating_action_menu_item1!!.setOnClickListener {
            if (firstRun) {
                UIModule.buildSpotlight(this, material_design_floating_action_menu_item1, material_design_floating_action_menu_item2, firstRun)
            } else {
                startActivity(Intent(applicationContext, WriteActivity::class.java))
            }
        }
        material_design_floating_action_menu_item2!!.setOnClickListener {
            if (firstRun) {
                UIModule.buildSpotlight(this, material_design_floating_action_menu_item1, material_design_floating_action_menu_item2, firstRun)
            } else {
                startActivity(Intent(applicationContext, RememberActivity::class.java))
            }
        }
    }
    private fun initMusic() {
        music = MediaPlayer.create(this, R.raw.front_flip)
        music.isLooping = true
        music.start()
    }

    override fun onStop() {
        super.onStop()
        music.stop()
    }

    override fun onBackPressed() {
        UIModule.buildEndDialog(this)
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_FILES = 653
    }
}