package com.solo_dev.remember_final.ui.view

import androidx.lifecycle.*
import com.solo_dev.remember_final.data.data.DataWrite

class WriteViewModel : ViewModel() {
    val dataListOfBoard = MutableLiveData<ArrayList<DataWrite>>()


}
