package com.solo_dev.remember_final.ui.adapter.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.data.data.CardData

class CardAdapter(val cardList : ArrayList<CardData>) : RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide, parent, false)

        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(cardList[position].img).into(holder.mainImg)
        holder.subTitle.text = cardList[position].subTitle
    }

    override fun getItemCount(): Int {
        return cardList.size
    }
}

class CardViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val subTitle = view.findViewById<TextView>(R.id.subTitle)
    val mainImg = view.findViewById<ImageView>(R.id.mainImg)
}