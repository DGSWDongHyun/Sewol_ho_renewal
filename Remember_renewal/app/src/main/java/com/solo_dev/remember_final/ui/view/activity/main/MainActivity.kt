package com.solo_dev.remember_final.ui.view.activity.main

import android.Manifest
import android.content.Context
import android.content.Intent
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
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.ui.adapter.slider.Main_SimpleAdapter
import com.solo_dev.remember_final.ui.view.activity.intro.LoadingActivity
import com.solo_dev.remember_final.ui.view.activity.login.Google_Login
import com.solo_dev.remember_final.ui.view.activity.remember.RememberActivity
import com.solo_dev.remember_final.ui.view.activity.write.WriteActivity
import com.wajahatkarim3.easyflipview.EasyFlipView.OnFlipAnimationListener
import com.wooplr.spotlight.SpotlightView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var NORMAL = 0;
    private var CLOSED = 1;

    private var mAuth: FirebaseAuth? = null
    var firstrun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        Initialize()

        //more method defined.. **
        Initialize_music()
        flipMusic()
        clickListener_FAB()

        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser == null) {
            finish()
            startActivity(Intent(this, Google_Login::class.java))
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
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "승인이 허가되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "승인 후에 게시글에 이미지를 첨부하실 수 있습니다.", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun clickListener_FAB() {
        material_design_floating_action_menu_item1!!.setOnClickListener {
            if (firstrun) {
                SpotlightView.Builder(this@MainActivity)
                        .introAnimationDuration(400)
                        .performClick(true)
                        .fadeinTextDuration(400)
                        .headingTvColor(Color.parseColor("#FFFFFF"))
                        .headingTvSize(32)
                        .headingTvText("Remember 2.0을 소개합니다.")
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
                            firstrun = false
                        }
                        .show()
                getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("firstrun", false)
                        .commit()
            } else {
                startActivity(Intent(applicationContext, WriteActivity::class.java))
            }
        }
        material_design_floating_action_menu_item2!!.setOnClickListener {
            if (firstrun) {
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
                            firstrun = false
                        }
                        .show()
                getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("firstrun", false)
                        .commit()
            } else {
                startActivity(Intent(applicationContext, RememberActivity::class.java))
            }
        }
    }

    private fun Initialize() {
        // when we initialize our layout, almost used this method **
        // my auto flip card library initialize**
        flip_card!!.setToHorizontalType()
        flip_card!!.isAutoFlipBack = false
        flip_card!!.flipDuration = 2500
        firstrun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean("firstrun", true)
        slider!!.setSliderAdapter(Main_SimpleAdapter(applicationContext))
        slider!!.setIndicatorAnimation(IndicatorAnimations.WORM)
        // set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        slider!!.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        slider!!.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        slider!!.indicatorSelectedColor = Color.WHITE
        slider!!.indicatorUnselectedColor = Color.GRAY
        slider!!.scrollTimeInSec = 3
        // set scroll delay in seconds :
        slider!!.startAutoCycle()

        // text-view hyper link within html.
    }

    private fun Initialize_music() {
        mp_flip = MediaPlayer.create(this, R.raw.front_flip)
        mp_flip?.isLooping = true
        mp_flip?.start()
    }


    private fun flipMusic() {

        // this method is when card flip back / front, play or change music **
        flip_card!!.onFlipListener = OnFlipAnimationListener { flipView, newCurrentSide ->
            if (flip_card!!.isBackSide) {
                mp_flip!!.stop()
                mp_flip = MediaPlayer.create(applicationContext, R.raw.back_flip)
                mp_flip?.isLooping = true
                mp_flip?.start()
            } else {
                mp_flip!!.stop()
                mp_flip = MediaPlayer.create(applicationContext, R.raw.front_flip)
                mp_flip?.isLooping = true
                mp_flip?.start()
            }
        }
    }

    override fun onBackPressed() {
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
        ok.setOnClickListener { finish() }
        dialog.show()
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