package com.solo_dev.remember_final.ui.adapter.write

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.data.write.DataWrite
import com.solo_dev.remember_final.ui.adapter.write.listener.onClickItemListener


class WriteAdapter(private val aContext: Context, private val listener: onClickItemListener) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var boardData: List<DataWrite>? = null

    fun setData(boardData: ArrayList<DataWrite>?) {
        this.boardData = boardData
        notifyDataSetChanged()
    }

    fun updateData(boardData: ArrayList<DataWrite>?, adapter: WriteAdapter) {
        adapter.setData(boardData)
    }

    fun getData() : List<DataWrite>{
        return boardData!!
    }

    fun clearData() : Boolean {
        (boardData as ArrayList).clear()
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var root : View ?= null

        root = LayoutInflater.from(parent.context).inflate(R.layout.write_item, parent, false)
        return WriteViewHolder(root!!)

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (title, contents, dateTime, imgContents, liked, reported, viewing, users, displayName) = boardData!![position]

        if(holder is WriteViewHolder){

            holder.title.text = title
            holder.contents.text = contents
            if(imgContents != null) {
                holder.cardOfImage.visibility = View.VISIBLE
                val storageReference = FirebaseStorage.getInstance().reference.child(imgContents!!)
                storageReference.downloadUrl.addOnCompleteListener {
                    if(it.isSuccessful) {
                        Glide.with(aContext).load(it.result).centerCrop().into(holder.imgContents)
                    }else{
                        Log.d("TASK_", it.exception?.message)
                    }
                }
            }

            holder.itemView.setOnClickListener { v: View? -> listener.onClick(position, boardData!![position]) }
        }

    }

    override fun getItemCount(): Int {
        return if (boardData != null) boardData!!.size else 0
    }

    inner class WriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val contents: TextView = itemView.findViewById(R.id.contents)
        val imgContents : ImageView = itemView.findViewById(R.id.imgContents)
        val cardOfImage : CardView = itemView.findViewById(R.id.cardView)
    }

}
