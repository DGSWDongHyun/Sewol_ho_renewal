package com.solo_dev.remember_final.ui.view.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.data.write.Data_Write
import com.solo_dev.remember_final.ui.adapter.write.Data_Adapter
import com.solo_dev.remember_final.ui.list.SwipeDismissListViewTouchListener
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class WriteFragment : Fragment() {
    var mRefresh: SwipeRefreshLayout? = null
    var write: MutableList<Data_Write> = CopyOnWriteArrayList()
    var adapter_write: Data_Adapter? = null
    var list_written: ListView? = null
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var databaseReference = firebaseDatabase.reference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_write, container, false)
        mRefresh = inflate.findViewById(R.id.swipe)
        list_written = inflate.findViewById(R.id.write_list)
        adapter_write = Data_Adapter(activity, write)
        list_written!!.setAdapter(adapter_write)
        val user = FirebaseAuth.getInstance().currentUser
        val touchListener = SwipeDismissListViewTouchListener(list_written,
                object : SwipeDismissListViewTouchListener.DismissCallbacks {
                    override fun canDismiss(position: Int): Boolean {
                        return true
                    }

                    override fun onDismiss(listView: ListView?, reverseSortedPositions: IntArray) {
                        for (position in reverseSortedPositions) {
                            if (write[position].users == user!!.email) {
                                Toast.makeText(context, write[position].getkeys, Toast.LENGTH_LONG).show()
                                databaseReference.child("write").child(write[position].getkeys).removeValue().addOnCompleteListener {
                                    Toast.makeText(context, "Completed", Toast.LENGTH_LONG).show()
                                    adapter_write!!.notifyDataSetChanged()
                                    write.removeAt(position)
                                    adapter_write!!.notifyDataSetChanged()
                                }.addOnFailureListener { e -> Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
                            } else {
                                Toast.makeText(context, "글쓴이가 다릅니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                })
        list_written!!.setOnTouchListener(touchListener)
        list_written!!.setOnScrollListener(touchListener.makeScrollListener())
        mRefresh!!.setOnRefreshListener(OnRefreshListener {
            reading_data()
            mRefresh!!.setRefreshing(false)
        })
        initialize()
        list_written!!.setOnItemClickListener(OnItemClickListener { parent, v, position, id ->
            val (Title, contents1, Date, img_contents) = parent.getItemAtPosition(position) as Data_Write
            val innerView = layoutInflater.inflate(R.layout.details_dialog, null)
            val mDialog = AlertDialog.Builder(activity)
                    .setView(innerView)
                    .create()
            mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val title = innerView.findViewById<TextView>(R.id.title)
            val contents = innerView.findViewById<TextView>(R.id.contents)
            val date = innerView.findViewById<TextView>(R.id.date)
            val img_one = innerView.findViewById<ImageView>(R.id.img_contents)
            val rela = innerView.findViewById<RelativeLayout>(R.id.rela_img)
            title.text = Title
            contents.text = Date
            date.text = contents1
            if (img_contents != null) {
                val storageReference = FirebaseStorage.getInstance().reference
                Glide.with(activity!!).load(storageReference.child(img_contents)).into(img_one)
            } else {
                rela.visibility = View.GONE
            }
            mDialog.show()
        })
        return inflate
    }

    val time: String
        get() {
            val now2 = System.currentTimeMillis()
            val date = Date(now2)
            val CurTimeFormat = SimpleDateFormat("aa HH:mm")
            return CurTimeFormat.format(date)
        }

    fun initialize() {
        databaseReference.child("write").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val write_data = dataSnapshot.getValue(Data_Write::class.java)!!
                if (write_data.viewing) {
                    write.add(Data_Write(write_data.Title, write_data.Date, write_data.contents, write_data.img_contents, write_data.liked, write_data.reported, write_data.getkeys, write_data.viewing, write_data.users, write_data.Display_Name))
                    adapter_write!!.notifyDataSetChanged()
                    repoerted_out(write_data)
                }
                adapter_write!!.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun reading_data() {
        for (idx in write) {
            write.remove(idx)
        }
        databaseReference.child("write").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val write_data = dataSnapshot.getValue(Data_Write::class.java)!!
                if (write_data.viewing) {
                    write.add(Data_Write(write_data.Title, write_data.Date, write_data.contents, write_data.img_contents, write_data.liked, write_data.reported, write_data.getkeys, write_data.viewing, write_data.users, write_data.Display_Name))
                    adapter_write!!.notifyDataSetChanged()
                    repoerted_out(write_data)
                }
                adapter_write!!.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun repoerted_out(data: Data_Write) {
        if (data.reported > 10) {
            databaseReference.child("write").child(data.getkeys).child("viewing").setValue(false)
        }
    }
}