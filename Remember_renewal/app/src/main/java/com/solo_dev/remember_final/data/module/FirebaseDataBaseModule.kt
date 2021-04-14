package com.solo_dev.remember_final.data.module

import android.app.Activity
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.solo_dev.remember_final.data.data.DataWrite
import com.solo_dev.remember_final.ui.adapter.write.WriteAdapter
import com.solo_dev.remember_final.ui.view.fragment.WriteFragment
import kotlinx.coroutines.*

class FirebaseDataBaseModule {
        companion object {
            private val data = ArrayList<DataWrite>()
            private val databaseReference = FirebaseDatabase.getInstance().reference
            const val CODE_UPLOAD = 0

            fun loadWriteList(activity: Activity, adapter: WriteAdapter) {
                CoroutineScope(Dispatchers.Default).async {
                    val getDatabaseBoardList: Job = GlobalScope.async {
                        databaseReference.child("write").addChildEventListener(object : ChildEventListener {
                            override fun onChildAdded(result: DataSnapshot, s: String?) {
                                    val writeData = result.getValue(DataWrite::class.java)!!

                                    Log.d("TAG", "tag_onAdded")
                                    data.add(0, DataWrite(writeData.title, writeData.date, writeData.contents, writeData.imgContents,
                                            writeData.liked, writeData.reported, writeData.viewing, writeData.users, writeData.displayName))
                            }

                            override fun onChildChanged(result: DataSnapshot, s: String?) {
                            }

                            override fun onChildRemoved(result: DataSnapshot) {
                            }

                            override fun onChildMoved(result: DataSnapshot, s: String?) {
                            }

                            override fun onCancelled(result: DatabaseError) {
                                Log.d("Error", result.message)
                            }
                        })
                        for (index in data) {
                            if (!index.imgContents!!.contains("images/")) {
                                index.imgContents = null
                            }
                        }

                    }
                    getDatabaseBoardList.join()

                    delay(2000)

                    if (getDatabaseBoardList.isCompleted) {
                        withContext(Dispatchers.Main) {
                            UIModule.progressDialog(activity, UIModule.ShowCode.REQUEST_DISMISS)
                            adapter.setData(data)
                        }
                    }

                    Log.d("TAG_", "inCage : ${getDatabaseBoardList.isActive}, ${getDatabaseBoardList.isCompleted}, ${getDatabaseBoardList.isCancelled}")

                    true
                }
            }
        }
}