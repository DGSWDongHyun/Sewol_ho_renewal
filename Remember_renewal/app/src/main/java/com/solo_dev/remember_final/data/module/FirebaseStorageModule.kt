package com.solo_dev.remember_final.data.module

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.solo_dev.remember_final.R
import java.text.SimpleDateFormat

class FirebaseStorageModule {
    companion object {
        private var storagePath : String? = null

        fun uploadFile(imageView: ImageView, filePath : Uri?, activity : Activity, context : Context) : String? {
            //업로드할 파일이 있으면 수행
            if (filePath != null) {
                //업로드 진행 Dialog 보이기

                val progressDialog = AlertDialog.Builder(activity)
                        .setView(R.layout.dialog_progress)
                        .setCancelable(false)
                        .create()

                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                progressDialog.show()

                val storage = FirebaseStorage.getInstance()
                val filename = SimpleDateFormat("yyyyMMHH_mmss_").format(System.currentTimeMillis()) + ".png"
                //storage 주소와 폴더 파일명을 지정해 준다.
                val storageRef = storage.getReferenceFromUrl("gs://rememberenweal.appspot.com/").child("images/$filename")
                storagePath = "images/$filename"

                Glide.with(context).load(filePath).centerCrop().into(imageView)

                //올라가거라...
                storageRef.putFile(filePath) //성공시
                        .addOnSuccessListener {
                            progressDialog.dismiss() //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(context, "업로드 완료!", Toast.LENGTH_SHORT).show()
                        } //실패시
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(context, "업로드 실패!", Toast.LENGTH_SHORT).show()
                        } //진행중
            } else {
                Toast.makeText(context, "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show()
            }
            return storagePath
        }
    }
}