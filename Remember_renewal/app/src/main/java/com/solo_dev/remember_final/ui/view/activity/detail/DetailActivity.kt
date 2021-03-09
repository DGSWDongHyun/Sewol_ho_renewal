package com.solo_dev.remember_final.ui.view.activity.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.solo_dev.remember_final.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var title : String
    private lateinit var contents : String
    private lateinit var dateName : String
    private lateinit var imgContents : String
    private lateinit var detailBinding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        getData()

        detailBinding.title.text = title
        detailBinding.contents.text = contents
        detailBinding.nameDate.text = dateName
    }

    private fun getData() {
        title = intent.getStringExtra("title")
        contents = intent.getStringExtra("contents")
        dateName = intent.getStringExtra("dateName")
        if(intent.hasExtra("img")) {
            imgContents = intent.getStringExtra("img")

            if(imgContents != null) {
                val storageReference = FirebaseStorage.getInstance().reference.child(imgContents)
                storageReference.downloadUrl.addOnCompleteListener {
                    Glide.with(this).load(it.result).into(detailBinding.img)
                }
            }
        }
    }
}