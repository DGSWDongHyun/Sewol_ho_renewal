package com.solo_dev.remember_final.ui.view.activity.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.firebase.storage.FirebaseStorage
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

    private fun setChips() {
        val list = arrayOf("세월호 7주기", "잊지않겠습니다.", "늘 기억하겠습니다.")
        for (index in list) {
            val chip = Chip(this)
            chip.text = index
            detailBinding.chipGroups.addView(chip)
        }
    }

    private fun getData() {

        setChips()
        title = intent.getStringExtra("title").toString()
        contents = intent.getStringExtra("contents").toString()
        dateName = intent.getStringExtra("dateName").toString()
        if(intent.hasExtra("img")) {
            imgContents = intent.getStringExtra("img").toString()

            if(imgContents != null) {
                val storageReference = FirebaseStorage.getInstance().reference.child(imgContents)
                storageReference.downloadUrl.addOnCompleteListener {
                    Glide.with(this).load(it.result).into(detailBinding.img)
                }
            }
        }
    }
}