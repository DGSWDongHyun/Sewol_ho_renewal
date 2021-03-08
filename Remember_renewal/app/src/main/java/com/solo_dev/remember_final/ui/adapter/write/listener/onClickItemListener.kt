package com.solo_dev.remember_final.ui.adapter.write.listener

import com.solo_dev.remember_final.data.write.DataWrite

interface onClickItemListener {
    fun onClick(position : Int, data : DataWrite)
}