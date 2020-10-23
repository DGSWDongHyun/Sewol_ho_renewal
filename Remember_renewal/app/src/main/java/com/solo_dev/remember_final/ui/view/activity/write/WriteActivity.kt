package com.solo_dev.remember_final.ui.view.activity.write

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.data.write.Data_Write
import com.solo_dev.remember_final.ui.view.fragment.WriteFragment
import java.text.SimpleDateFormat
import java.util.*

class WriteActivity : AppCompatActivity() {
    private var filePath: Uri? = null
    var fm: FragmentManager? = null
    var tran: FragmentTransaction? = null
    var writeFragment: WriteFragment? = null
    var innerView: View? = null
    var fab_write: FloatingActionButton? = null
    var select_image: Button? = null
    var storage2: String? = null
    var viewing = true
    var storageReference: StorageReference? = null
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var databaseReference = firebaseDatabase.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_write)
        fab_write = findViewById(R.id.fab_write)
        fab_write!!.setOnClickListener(View.OnClickListener { create_Dialog() })
        writeFragment = WriteFragment()
        setFrag(0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data!!.data
            uploadFile(innerView!!.findViewById<View>(R.id.img_select) as TextView)
        }
    }

    private fun uploadFile(text: TextView) {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("업로드중...")
            progressDialog.show()
            val storage = FirebaseStorage.getInstance()
            val formatter = SimpleDateFormat("yyyyMMHH_mmss")
            val now = Date()
            val filename = formatter.format(now) + ".png"
            //storage 주소와 폴더 파일명을 지정해 준다.
            val storageRef = storage.getReferenceFromUrl("gs://rememberenweal.appspot.com/").child("images/$filename")
            storage2 = "images/$filename"
            storageReference = storageRef
            //올라가거라...
            storageRef.putFile(filePath!!) //성공시
                    .addOnSuccessListener {
                        progressDialog.dismiss() //업로드 진행 Dialog 상자 닫기
                        text.text = "이미지 선택됨."
                        Toast.makeText(applicationContext, "업로드 완료!", Toast.LENGTH_SHORT).show()
                    } //실패시
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "업로드 실패!", Toast.LENGTH_SHORT).show()
                    } //진행중
                    .addOnProgressListener { taskSnapshot ->
                        val progress//이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                = 100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount.toDouble()
                        //dialog에 진행률을 퍼센트로 출력해 준다
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "% ...")
                    }
        } else {
            Toast.makeText(applicationContext, "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    fun create_Dialog() {
        innerView = layoutInflater.inflate(R.layout.writting_dialog, null)
        val send_btn = innerView!!.findViewById<Button>(R.id.send)
        val ed_title: EditText
        val ed_contents: EditText
        select_image = innerView!!.findViewById(R.id.select)
        ed_title = innerView!!.findViewById(R.id.title_write)
        ed_contents = innerView!!.findViewById(R.id.write_content)
        val mDialog = AlertDialog.Builder(this@WriteActivity)
                .setView(innerView)
                .create()
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        select_image!!.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0)
        })
        send_btn.setOnClickListener { v: View? ->
            if (!ed_title.text.toString().isEmpty() && !ed_contents.text.toString().isEmpty()) {
                val task = databaseReference.child("write").push()
                val user = FirebaseAuth.getInstance().currentUser
                val data = Data_Write(ed_title.text.toString(), ed_contents.text.toString(), time, storage2, 0, 0, task.key!!, viewing, user!!.email!!, user.displayName!!)
                task.setValue(data)
                storage2 = null
                val inflater = layoutInflater
                val toastDesign = inflater.inflate(R.layout.toast, findViewById<View>(R.id.toast_design_root) as ViewGroup) //toast_design.xml 파일의 toast_design_root 속성을 로드
                val imgs = toastDesign.findViewById<ImageView>(R.id.img_toast)
                Glide.with(applicationContext).load(R.drawable.yellow).into(imgs)
                val toast = Toast(applicationContext)
                toast.setGravity(Gravity.CENTER, 0, 0) // CENTER를 기준으로 0, 0 위치에 메시지 출력
                toast.duration = Toast.LENGTH_LONG
                toast.view = toastDesign
                toast.show()
                mDialog.cancel()
            }
        }
        mDialog.show()
    }

    val time: String
        get() {
            val now2 = System.currentTimeMillis()
            val date = Date(now2)
            val CurTimeFormat = SimpleDateFormat("aa HH:mm")
            return CurTimeFormat.format(date)
        }

    fun setFrag(n: Int) {    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        fm = supportFragmentManager
        tran = fm!!.beginTransaction()
        when (n) {
            0 -> {
                tran!!.replace(R.id.fragment_container, writeFragment!!) //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran!!.commit()
            }
            1 -> {
            }
        }
    }
}