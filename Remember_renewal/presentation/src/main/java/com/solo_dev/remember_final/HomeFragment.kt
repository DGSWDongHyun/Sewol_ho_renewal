package com.solo_dev.remember_final

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.simple.data.data.data.CardData
import com.simple.data.data.module.UIModule
import com.solo_dev.remember_final.ui.adapter.card.CardAdapter
import com.solo_dev.remember_final.ui.view.activity.intro.IntroActivity
import com.solo_dev.remember_final.ui.view.activity.login.GoogleLoginActivity
import com.solo_dev.remember_final.ui.view.activity.main.MainActivity
import com.solo_dev.remember_final.ui.view.activity.remember.RememberActivity
import com.solo_dev.remember_final.ui.view.activity.write.WriteActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var music : MediaPlayer
    private var dateTextView : TextView?= null
    private var leftDateTextView : TextView?= null
    private lateinit var rvCardList : RecyclerView
    private var mAuth: FirebaseAuth? = null
    var firstRun : Boolean = true
    val imageArray = arrayOf(R.drawable.card, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5)
    val titleArray = arrayOf("다음으로 넘어가려면 슬라이드..", "1명이라도 더 살아오길..", "세월호 보도를 하다가 울컥한 아나운서.", "세월호, 지상 위로 올라오다.", "뭐가 그리 힘들었니..")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        music = MediaPlayer.create(requireActivity(), R.raw.front_flip)

        initLayout(view)
        initMusic()
        clickFAB()

        checkUpdate()

        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser == null) {
            startActivity(Intent(requireActivity(), GoogleLoginActivity::class.java))
            requireActivity().finish()
        }


        if (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_FILES)
            }
        }

    }
    override fun                                                                                                                                                                                                 onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_FILES -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireActivity(), "승인이 허가되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireActivity(), "승인 후에 게시글에 이미지를 첨부하실 수 있습니다.", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
        private fun checkUpdate() {
            FirebaseDatabase.getInstance().reference.child("app_ver").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val packageVersion = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0).versionCode
                    val p = p0.value.toString()

                    if(p.toInt() > packageVersion) {
                        AlertDialog.Builder(requireActivity())
                                .setTitle("업데이트 발견")
                                .setCancelable(false)
                                .setMessage("새로운 업데이트가 발견 되었습니다. 업데이트 하시겠어요?")
                                .setPositiveButton("확인") { d, i ->
                                    requireActivity().finish()
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.setData(Uri.parse("market://details?id=${requireActivity().packageName}"))
                                    startActivity(intent)
                                }
                                .setNegativeButton("닫기") { d, i ->
                                    requireActivity().finish()
                                    Toast.makeText(requireActivity(), "최신버전으로 업데이트 해주세요.", Toast.LENGTH_LONG).show()
                                }
                                .create()
                                .show()
                    }else if(p.toInt() == 0) {
                        AlertDialog.Builder(requireActivity())
                                .setTitle("서버 점검 중 ..")
                                .setCancelable(false)
                                .setMessage("더 나은 서비스를 위해 서버를 점검 중입니다. 양해부탁드립니다.")
                                .setPositiveButton("확인") { d, i ->
                                    requireActivity().finish()
                                }
                                .create()
                                .show()
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
        }

        private fun initLayout(view : View) {
            val calendar = Calendar.getInstance()

            rvCardList = view.findViewById(R.id.rvSlideCard)
            dateTextView = view.findViewById(R.id.dateTextView)
            leftDateTextView = view.findViewById(R.id.leftDateTextView)

            rvCardList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvCardList.adapter = CardAdapter(initCardArray())

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvCardList)

            dateTextView?.text = "오늘은 "+returnDate("MM")+"월 "+returnDate("dd")+"일 입니다."
            leftDateTextView?.text = "4월 16일까지.. ${calculateDate(calendar.get(Calendar.YEAR), 4, 16)}일 남았습니다."

        }

        private fun calculateDate(year: Int, month: Int, day: Int) : String {
            return try {
                var yyear = year
                var mmonth = month
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                val todaCal = Calendar.getInstance() //오늘날자 가져오
                val ddayCal = Calendar.getInstance() //오늘날자를 가져와 변경시킴

                mmonth -= 1 // 받아온날자에서 -1을 해줘야함.

                ddayCal[year, mmonth] = day // D-day의 날짜를 입력

                val today = todaCal.timeInMillis / 86400000 //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)

                var dDay = ddayCal.timeInMillis / 86400000
                var count = dDay - today // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.

                if(count < 0) {
                    yyear += 1

                    ddayCal[yyear, mmonth] = day

                    dDay = ddayCal.timeInMillis / 86400000
                    count = dDay - today // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
                }


                if(count > 0) {"${dDay - today}"} else { "4월 16일입니다." }
            } catch (e: Exception) {
                e.printStackTrace()
                (-1).toString()
            }
        }

        private fun returnDate(format: String) : String {
            return SimpleDateFormat(format).format(Date(System.currentTimeMillis()))
        }

        private fun clickFAB() {
            firstRun = requireActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean("firstRun", true)
            material_design_floating_action_menu_item2!!.setOnClickListener {
                if (firstRun) {
                    UIModule.buildSpotlight(requireActivity(), material_design_floating_action_menu_item2, this)
                } else {
                    startActivity(Intent(requireActivity(), RememberActivity::class.java))
                }
            }
        }

        private fun initCardArray() : ArrayList<CardData> {
            val arrayList = ArrayList<CardData>()
            for(index in imageArray.indices) {
                arrayList.add(CardData(imageArray[index], titleArray[index]))
            }

            return arrayList
        }

        private fun initMusic() {
            music.isLooping = true
            music.setOnPreparedListener {
                it.start()
            }
        }


        override fun onStop(){
            super.onStop()
        }




        override fun onResume() {
            super.onResume()
            music.start()
        }



        override fun onDestroy() {
            super.onDestroy()
            music.pause()
        }

        companion object {
            private const val MY_PERMISSIONS_REQUEST_FILES = 653
        }
}