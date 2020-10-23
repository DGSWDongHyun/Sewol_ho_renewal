package com.solo_dev.remember_final.ui.adapter.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.data.slider.Main_SimpleItems
import com.solo_dev.remember_final.ui.adapter.slider.Main_SimpleAdapter.SliderAdapterVH
import java.util.*

class Main_SimpleAdapter(private val context: Context) : SliderViewAdapter<SliderAdapterVH>() {
    private var mSliderItems: MutableList<Main_SimpleItems> = ArrayList()
    fun renewItems(sliderItems: MutableList<Main_SimpleItems>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: Main_SimpleItems) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.main_simpleslider, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        when (position) {
            0 -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_1)
                    .into(viewHolder.imageViewBackground)
            1 -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_2)
                    .into(viewHolder.imageViewBackground)
            2 -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_3)
                    .into(viewHolder.imageViewBackground)
            3 -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_4)
                    .into(viewHolder.imageViewBackground)
            4 -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_5)
                    .into(viewHolder.imageViewBackground)
            5 -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_6)
                    .into(viewHolder.imageViewBackground)
            6 -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_7)
                    .into(viewHolder.imageViewBackground)
            7 -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_8)
                    .into(viewHolder.imageViewBackground)
            else -> Glide.with(viewHolder.itemView)
                    .load(R.drawable.images_9)
                    .into(viewHolder.imageViewBackground)
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return 9
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        var itemView: View
        var imageViewBackground: ImageView

        init {
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
            this.itemView = itemView
        }
    }
}