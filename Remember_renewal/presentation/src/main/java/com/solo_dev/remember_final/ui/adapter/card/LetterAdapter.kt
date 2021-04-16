package com.solo_dev.remember_final.ui.adapter.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.simple.data.data.data.CardData
import com.simple.data.data.data.ChatData
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.databinding.ItemLetterBinding
import com.solo_dev.remember_final.databinding.ItemMyLetterBinding

class LetterAdapter(private var chatList : ArrayList<ChatData>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var user = FirebaseAuth.getInstance().currentUser
    private lateinit var letterUserBinding: ItemLetterBinding
    private lateinit var letterMyBinding: ItemMyLetterBinding

    fun setData(chatList : ArrayList<ChatData>) {
        this.chatList = chatList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_MY -> {
                letterMyBinding =
                        ItemMyLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemMyLetter(letterMyBinding)
            }
            TYPE_USER -> {
                letterUserBinding =
                        ItemLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemUserLetter(letterUserBinding)
            }
        }
        return ItemUserLetter(letterUserBinding)
    }

    override fun getItemCount(): Int {
        return chatList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemUserLetter) {
            holder.bind(chatList?.get(position)!!)
        } else if (holder is ItemMyLetter) {
            holder.bind(chatList?.get(position)!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        chatList?.let {
            return if(user!!.email == it[position].userEmail) TYPE_MY else TYPE_USER
        } ?: kotlin.run {
            return super.getItemViewType(position)
        }
    }

    companion object {
        const val TYPE_MY = 1;
        const val TYPE_USER = 2;
    }

    class ItemUserLetter(val binding: ItemLetterBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatData : ChatData) {
            binding.userName.text = "${chatData.userName}"
            binding.userContents.text = "${chatData.contents}"
            binding.userDatetime.text = "${chatData.dateTime}"
        }
    }

    class ItemMyLetter(val binding : ItemMyLetterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatData : ChatData) {
            binding.myName.text = "${chatData.userName}"
            binding.myContents.text = "${chatData.contents}"
            binding.myDatetime.text = "${chatData.dateTime}"
        }
    }
}