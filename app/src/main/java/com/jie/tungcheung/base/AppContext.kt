package com.jie.tungcheung.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference


class AppContext private constructor(){


     var context: Context = MyApplication.myApplication!!

    private  var ctx: WeakReference<AppCompatActivity>? = null

    companion object{

        private val instant by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){ AppContext() }

        fun get() = instant
    }


    fun attach(context: Context){
        this.context = context
    }


    fun attach(activity: AppCompatActivity){
        ctx = WeakReference(activity)
    }

    fun getActivity() = ctx?.get()
}