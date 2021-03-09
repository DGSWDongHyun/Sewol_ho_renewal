package com.solo_dev.remember_final.ui.adapter.write

import android.content.Context
import android.net.Uri
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
import com.solo_dev.remember_final.data.data.DataWrite
import com.solo_dev.remember_final.ui.adapter.write.listener.onClickItemListener
import kotlinx.coroutines.*


class WriteAdapter(private val aContext: Context, private val listener: onClickItemListener) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var boardData: ArrayList<DataWrite> = arrayListOf()

    init {
        boardData.clear()
    }

    fun setData(boardData: ArrayList<DataWrite>) {
        this.boardData = boardData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var root : View ?= null

        root = LayoutInflater.from(parent.context).inflate(R.layout.item_write, parent, false)
        return WriteViewHolder(root!!)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (title, contents, dateTime, imgContents, liked, reported, viewing, users, displayName) = boardData[position]

        if(holder is WriteViewHolder){

            holder.title.text = title
            holder.contents.text = contents
            if(!imgContents.isNullOrBlank()) {
                holder.cardOfImage.visibility = View.VISIBLE
                val storageReference = FirebaseStorage.getInstance().reference.child(imgContents)
                storageReference.downloadUrl.addOnCompleteListener {
                    if(it.isSuccessful) {
                        GlobalScope.launch {
                            successfullyLoad(it.result, holder.imgContents)
                        }
                    }else{
                        Log.d("TASK_", "${it.exception?.message}")
                    }
                }
            } else {
                holder.cardOfImage.visibility=View.GONE
            }
            Log.d("TAG", "get : $position, ${imgContents != null}")
            holder.itemView.setOnClickListener { v: View? -> listener.onClick(position, boardData!![position]) }
        }

    }

    override fun getItemCount(): Int {
        return boardData!!.size
    }

    inner class WriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val contents: TextView = itemView.findViewById(R.id.contents)
        val imgContents : ImageView = itemView.findViewById(R.id.imgContents)
        val cardOfImage : CardView = itemView.findViewById(R.id.cardView)
    }

    private suspend fun successfullyLoad(result : Uri?, image : ImageView) : Boolean {
        CoroutineScope(Dispatchers.IO).async {
            val job: Job = GlobalScope.async {
                withContext(Dispatchers.Main) {
                    Glide.with(aContext).load(result).centerCrop().into(image)
                }
            }

            job.join()

            true
        }.await()

        return true
    }
}
