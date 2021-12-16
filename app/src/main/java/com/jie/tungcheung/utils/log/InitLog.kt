package com.jie.tungcheung.utils.log

import android.util.Log

internal object InitLog {

    var enableLog = false

    fun log(text:String){
        if(enableLog){
            Log.d("InitLog",text)
        }
    }
}