package com.solo_dev.remember_final.ui.view

import android.app.Application
import androidx.lifecycle.*
import com.solo_dev.remember_final.data.write.DataWrite

class WriteViewModel : ViewModel() {
    val dataListOfBoard = MutableLiveData<ArrayList<DataWrite>>()


}
