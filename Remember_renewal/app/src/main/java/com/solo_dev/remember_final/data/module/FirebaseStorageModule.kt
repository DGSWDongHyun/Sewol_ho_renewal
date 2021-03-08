package com.solo_dev.remember_final.data.module

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class FirebaseStorageModule {
    companion object {
        private var storageReference: StorageReference? = null
        private var storagePath : String? = null

        fun uploadFile(imageView: ImageView, filePath : Uri?, activity : Activity, context : Context) {
            //업로드할 파일이 있으면 수행
            if (filePath != null) {
                //업로드 진행 Dialog 보이기
                val progressDialog = ProgressDialog(activity)
                progressDialog.setTitle("업로드중...")
                progressDialog.show()
                val storage = FirebaseStorage.getInstance()
                val formatter = SimpleDateFormat("yyyyMMHH_mmss")
                val now = Date()
                val filename = formatter.format(now) + ".png"
                //storage 주소와 폴더 파일명을 지정해 준다.
                val storageRef = storage.getReferenceFromUrl("gs://rememberenweal.appspot.com/").child("images/$filename")
                storagePath = "images/$filename"
                storageReference = storageRef

                Glide.with(context).load(filePath).centerCrop().into(imageView)

                //올라가거라...
                storageRef.putFile(filePath!!) //성공시
                        .addOnSuccessListener {
                            progressDialog.dismiss() //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(context, "업로드 완료!", Toast.LENGTH_SHORT).show()
                        } //실패시
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(context, "업로드 실패!", Toast.LENGTH_SHORT).show()
                        } //진행중
                        .addOnProgressListener { taskSnapshot ->
                            val progress//이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    = 100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount.toDouble()
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + progress.toInt() + "% ...")
                        }
            } else {
                Toast.makeText(context, "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}