package com.solo_dev.remember_final.ui.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.simple.data.data.data.ChatData
import com.simple.data.data.module.UIModule
import com.solo_dev.remember_final.databinding.FragmentLetterBinding
import com.solo_dev.remember_final.ui.adapter.card.LetterAdapter
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LetterFragment : Fragment() {

    private lateinit var chatList : ArrayList<ChatData>
    private lateinit var adapter: LetterAdapter

    private lateinit var fragmentLetterBinding: FragmentLetterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        fragmentLetterBinding = FragmentLetterBinding.inflate(layoutInflater)

        return fragmentLetterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatList = ArrayList<ChatData>()
        adapter = LetterAdapter(chatList)

        updateWidget()

    }

    private fun updateWidget() {

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                UIModule.progressDialog(requireActivity(), UIModule.ShowCode.REQUEST_SHOW)
            }
            withContext(Dispatchers.IO) {
                refreshData()
            }
        }

        fragmentLetterBinding.sendMessage.setOnClickListener {
            if(!fragmentLetterBinding.sendInput.text.toString().isEmpty()) {
                val user = FirebaseAuth.getInstance().currentUser
                FirebaseDatabase.getInstance().reference.child("chatData").push().setValue(ChatData(user!!.displayName,
                        user.email, fragmentLetterBinding.sendInput.text.toString(), SimpleDateFormat("yyyy.MM.dd").format(Date(System.currentTimeMillis()))))

                fragmentLetterBinding.sendInput.setText("")
                fragmentLetterBinding.sendInput.clearFocus()

                val imm: InputMethodManager? = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?

                imm?.hideSoftInputFromWindow(fragmentLetterBinding.sendInput.windowToken, 0);
            }
        }
    }

    private fun refreshData() {
            CoroutineScope(Dispatchers.Default).async {
                val getDatabaseBoardList: Job = GlobalScope.async {
                    FirebaseDatabase.getInstance().reference.child("chatData").addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(result: DataSnapshot, s: String?) {
                            val chat = result.getValue(ChatData::class.java)!!
                            chatList.add(ChatData(chat.userName, chat.userEmail, chat.contents, chat.dateTime))

                            fragmentLetterBinding.rvCardViewList.smoothScrollToPosition(chatList.size)
                        }

                        override fun onChildChanged(result: DataSnapshot, s: String?) {
                            Log.d("TAG", "tag_onChanged")
                        }

                        override fun onChildRemoved(result: DataSnapshot) {
                            Log.d("TAG", "tag_onRemoved")
                        }

                        override fun onChildMoved(result: DataSnapshot, s: String?) {
                        }

                        override fun onCancelled(result: DatabaseError) {
                            Log.d("Error", result.message)
                        }
                    })

                }
                getDatabaseBoardList.join()

                delay(2000)

                if (getDatabaseBoardList.isCompleted) {
                    withContext(Dispatchers.Main) {
                        fragmentLetterBinding.rvCardViewList.adapter = adapter
                        fragmentLetterBinding.rvCardViewList.layoutManager = LinearLayoutManager(requireContext())

                        UIModule.progressDialog(requireActivity(), UIModule.ShowCode.REQUEST_DISMISS)
                        adapter.setData(chatList)
                        fragmentLetterBinding.rvCardViewList.smoothScrollToPosition(chatList.size)
                    }
                }

                Log.d("TAG_", "inCage : ${getDatabaseBoardList.isActive}, ${getDatabaseBoardList.isCompleted}, ${getDatabaseBoardList.isCancelled}")

                true
            }
    }
}