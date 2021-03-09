package com.solo_dev.remember_final.ui.view.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.solo_dev.remember_final.ui.view.activity.detail.DetailActivity
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.data.module.FirebaseDataBaseModule
import com.solo_dev.remember_final.data.module.FirebaseStorageModule
import com.solo_dev.remember_final.data.module.UIModule
import com.solo_dev.remember_final.data.data.DataWrite
import com.solo_dev.remember_final.databinding.FragmentWriteBinding
import com.solo_dev.remember_final.ui.adapter.write.WriteAdapter
import com.solo_dev.remember_final.ui.adapter.write.listener.onClickItemListener
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class WriteFragment : Fragment() {

    var adapterWrite: WriteAdapter? = null
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var databaseReference = firebaseDatabase.reference
    private lateinit var binding : FragmentWriteBinding
    val SCROLLING_UP = -1
    var selectImage: Button? = null
    var storagePath : String? = null
    var viewing = true
    private var filePath: Uri? = null
    var innerView: View? = null
    private lateinit var imagePreview : ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWriteBinding.inflate(inflater, container, false)
        
        return binding.root
      }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewInit()
        initLayout()

    }

    private fun initLayout(){
        binding.swipeLayout.setOnRefreshListener {
            GlobalScope.launch {
                databaseWithoutDialog()
                delay(1000)
                withContext(Dispatchers.Main) {
                    binding.swipeLayout.isRefreshing = false
                }
            }
        }
    }

    private fun recyclerViewInit() {
        adapterWrite = WriteAdapter(requireActivity(), object : onClickItemListener {
            override fun onClick(position: Int, data: DataWrite) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("title", data.title)
                intent.putExtra("contents", data.date)
                intent.putExtra("dateName", "${data.contents} , ${data.displayName}님")
                if (data.imgContents != null) {
                    intent.putExtra("img", data.imgContents)
                }
                startActivity(intent)
            }
        })

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                UIModule.progressDialog(requireActivity(), UIModule.ShowCode.REQUEST_SHOW)
            }
            withContext(Dispatchers.IO) {
                getJobOfDatabase()
            }
        }

        binding.writeList.layoutManager = LinearLayoutManager(requireContext())
        binding.writeList.adapter = adapterWrite

        binding.floatingActionButton.setOnClickListener {
            createDialog()
        }


    }
    private suspend fun databaseWithoutDialog() {
        val getDatabase = FirebaseDataBaseModule.jobGetDatabase
        val getDatabaseAwait = getDatabase.await()

        delay(1200)

        if(getDatabase.isCompleted) {
            withContext(Dispatchers.Main) {
                adapterWrite!!.setData(getDatabaseAwait)
            }
        }
        Log.d("TAG_", "inCage : ${getDatabase.isActive}, ${getDatabase.isCompleted}, ${getDatabase.isCancelled}")
    }

    private suspend fun getJobOfDatabase() {
        val getDatabase = FirebaseDataBaseModule.jobGetDatabase
        val getDatabaseAwait = getDatabase.await()

        delay(2000)

        if(getDatabase.isCompleted) {
            withContext(Dispatchers.Main) {
                UIModule.progressDialog(requireActivity(), UIModule.ShowCode.REQUEST_DISMISS)
                adapterWrite!!.setData(getDatabaseAwait)
            }
        }
        Log.d("TAG_", "inCage : ${getDatabase.isActive}, ${getDatabase.isCompleted}, ${getDatabase.isCancelled}")
    }


    private fun createDialog() {
        innerView = layoutInflater.inflate(R.layout.dialog_write, null)

        val sendBtn = innerView!!.findViewById<Button>(R.id.sendButton)
        val edTitle: EditText = innerView!!.findViewById(R.id.writeTitle)
        val edContents: EditText = innerView!!.findViewById(R.id.writeContent)

        imagePreview = innerView!!.findViewById(R.id.loadImage)
        selectImage = innerView!!.findViewById(R.id.select)
        val mDialog = AlertDialog.Builder(requireActivity())
                .setView(innerView)
                .create()

        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        selectImage!!.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), FirebaseDataBaseModule.CODE_UPLOAD)
        })

        sendBtn.setOnClickListener { v: View? ->
            if (!edTitle.text.toString().isEmpty() && !edContents.text.toString().isEmpty()) {
                val task = databaseReference.child("write").push()
                val user = FirebaseAuth.getInstance().currentUser
                val data = DataWrite(edTitle.text.toString(), edContents.text.toString(), time, storagePath, 0, 0, viewing, user?.email!!, user.displayName!!)
                task.setValue(data)
                storagePath = null

                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        UIModule.progressDialog(requireActivity(), UIModule.ShowCode.REQUEST_SHOW)
                    }
                    withContext(Dispatchers.IO) {
                        getJobOfDatabase()
                    }
                }

                mDialog.cancel()
            }
        }
        mDialog.show()
    }
    private val time: String
        get() {
            val now2 = System.currentTimeMillis()
            val date = Date(now2)
            val curTimeFormat = SimpleDateFormat("aa HH:mm")
            return curTimeFormat.format(date)
        }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            databaseWithoutDialog()
        }
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         if (requestCode == FirebaseDataBaseModule.CODE_UPLOAD && resultCode == RESULT_OK) {
             filePath = data!!.data
             storagePath = FirebaseStorageModule.uploadFile(imagePreview, filePath, requireActivity(), requireContext())
         }
     }
}