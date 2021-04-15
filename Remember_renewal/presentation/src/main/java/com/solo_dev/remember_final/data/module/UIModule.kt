package com.simple.data.data.module

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import com.github.clans.fab.FloatingActionButton
import com.solo_dev.remember_final.HomeFragment
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.ui.view.activity.main.MainActivity
import com.wooplr.spotlight.SpotlightView
import kotlinx.android.synthetic.main.activity_main.*

class UIModule {
    enum class ShowCode{
        REQUEST_SHOW, REQUEST_DISMISS
    }
    companion object{
        private lateinit var alertLoading : AlertDialog
        fun progressDialog(activity : Activity, code : Enum<ShowCode>) {
            val viewInflater = activity.layoutInflater.inflate(R.layout.dialog_progress, null)

            when(code) {
                ShowCode.REQUEST_SHOW -> {
                   alertLoading = AlertDialog.Builder(activity)
                            .setView(viewInflater)
                            .setCancelable(false)
                            .create()

                    alertLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

                    alertLoading.show()
                }

                ShowCode.REQUEST_DISMISS -> {
                    alertLoading.dismiss()
                }

            }
        }

         fun buildSpotlight(activity : Activity , floatBtn2 : FloatingActionButton, fragment : HomeFragment) {
                        SpotlightView.Builder(activity)
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
                                .target(floatBtn2)
                                .lineAnimDuration(400)
                                .lineAndArcColor(Color.parseColor("#FFFFFF"))
                                .dismissOnTouch(true)
                                .dismissOnBackPress(false)
                                .enableDismissAfterShown(true)
                                .usageId("FDE!@@#2") //UNIQUE ID
                                .show()
                                .setListener {
                                    fragment.firstRun = false
                                }

            activity.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstRun", false)
                    .apply()
        }

         fun buildEndDialog(activity : Activity) {
            val builder = androidx.appcompat.app.AlertDialog.Builder(activity)
            val inflater = activity.layoutInflater
            val view = inflater.inflate(R.layout.dialog_exit, null)
            builder.setCancelable(true)
            builder.setView(view)

            val ok = view.findViewById<View>(R.id.ok) as Button
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val videoView = view.findViewById<VideoView>(R.id.videoView)

            val mediaController = MediaController(activity)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse("android.resource://" + activity.packageName + "/" +
                    R.raw.turnoff_))
            videoView.start()

            ok.setOnClickListener {
                dialog.dismiss()
                activity.finish()
            }

            dialog.show()
        }


    }
}