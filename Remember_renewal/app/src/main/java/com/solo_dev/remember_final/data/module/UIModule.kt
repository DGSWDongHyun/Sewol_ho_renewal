package com.solo_dev.remember_final.data.module

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.solo_dev.remember_final.R

class UIModule {
    enum class ShowCode{
        REQUEST_SHOW, REQUEST_DISMISS
    }
    companion object{
        private lateinit var alertLoading : AlertDialog
        fun progressDialog(activity : Activity, code : Enum<ShowCode>) {
            val viewInflater = activity.layoutInflater.inflate(R.layout.progress_dialog, null)

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
    }
}