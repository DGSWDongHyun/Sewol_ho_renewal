package com.solo_dev.remember_final.ui.adapter.write

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.data.write.Data_Write
import com.solo_dev.remember_final.data.write.Reported_Data

class Data_Adapter(private val mContext: Context?, listItem: List<Data_Write>?) : BaseAdapter() {
    var dialog: Dialog? = null
    private var listItem: List<Data_Write>? = null
    var mLayoutInflater: LayoutInflater
    var holder: ViewHolder? = null
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var databaseReference = firebaseDatabase.reference
    override fun getCount(): Int {
        return listItem!!.size
    }

    override fun getItem(i: Int): Data_Write {
        return listItem!![i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    inner class ViewHolder {
        var title: TextView? = null
        var contents: TextView? = null
        var date: TextView? = null
        var img_contents: ImageView? = null
        var rela: CardView? = null
        var report: ImageButton? = null
        var display: TextView? = null
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            holder = ViewHolder()
            val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.write_item, null)
            holder!!.title = convertView.findViewById<View>(R.id.title) as TextView
            holder!!.contents = convertView.findViewById<View>(R.id.contents) as TextView
            holder!!.date = convertView.findViewById<View>(R.id.date) as TextView
            holder!!.img_contents = convertView.findViewById<View>(R.id.img_contents) as ImageView
            holder!!.rela = convertView.findViewById<View>(R.id.img_view) as CardView
            holder!!.report = convertView.findViewById<View>(R.id.re) as ImageButton
            holder!!.display = convertView.findViewById<View>(R.id.display) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val (Title, contents, Date, img_contents, _, _, getkeys, _, _, Display_Name) = listItem!![position]
        holder!!.report!!.isFocusable = false
        holder!!.title!!.text = Title
        holder!!.contents!!.text = Date
        holder!!.date!!.text = contents
        holder!!.display!!.text = Display_Name
        if (img_contents != null) {
            holder!!.rela!!.visibility = View.VISIBLE
            Log.e("Debuged_sewol", img_contents)
        } else {
            holder!!.rela!!.visibility = View.GONE
        }
        holder!!.report!!.setOnClickListener {
            val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val innerView = inflater.inflate(R.layout.report_dialog, null)
            val send_btn = innerView.findViewById<Button>(R.id.report_send)
            val ed_contents: EditText
            ed_contents = innerView.findViewById(R.id.text_report)
            val mDialog = AlertDialog.Builder(mContext)
                    .setView(innerView)
                    .create()
            mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            send_btn.setOnClickListener {
                if (!ed_contents.text.toString().isEmpty()) {
                    FirebaseDatabase.getInstance().reference.child("write").child(getkeys).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val write_data = dataSnapshot.getValue(Data_Write::class.java)!!
                            var reported = write_data.reported
                            reported++
                            val task = databaseReference.child("write").child(getkeys).child("reported")
                            task.setValue(reported)
                            val task_report = databaseReference.child("reported").child(write_data.getkeys)
                            val user = FirebaseAuth.getInstance().currentUser
                            task_report.setValue(Reported_Data(user!!.email!!, write_data.contents))
                            if (task_report.key == write_data.getkeys) {
                                val task_reported = task_report.child("reportedBy").push()
                                task_reported.setValue(write_data.users)
                            } else {
                                val task_reported = task_report.child("reportedBy").push()
                                task_reported.setValue(write_data.users)
                            }
                            mDialog.cancel()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }
            mDialog.show()
        }
        return convertView
    }

    init {
        this.listItem = listItem
        mLayoutInflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}