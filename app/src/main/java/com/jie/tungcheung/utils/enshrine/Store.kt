package com.jie.tungcheung.utils.enshrine

import android.content.Context
import com.jie.tungcheung.bean.MyObjectBox
import com.tencent.mmkv.MMKV
import io.objectbox.BoxStore


object Store {
    lateinit var box: BoxStore
        private set
    fun initialize(context: Context) {
        MMKV.initialize(context)
        init(context)
    }
    private fun init(context: Context) {
        box = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
    }


}