package com.jie.tungcheung.base

import android.app.Activity
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.multidex.MultiDexApplication
import com.jie.tungcheung.utils.process.isMainProcess
import com.jie.tungcheung.utils.log.Launch
import com.jie.tungcheung.utils.process.SimpleLifecycleCallbacks


@Keep
open class BaseApplication : MultiDexApplication(),LifecycleObserver,ViewModelStoreOwner {

    private val modelStore by lazy { ViewModelStore() }

    override fun onCreate() {
        super.onCreate()
        if(!isMainProcess()){
            Launch.attach(this).enableLog().doInit()
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerActivityLifecycleCallbacks(object : SimpleLifecycleCallbacks(){
            override fun onActivityResumed(activity: Activity) {
                if(activity is AppCompatActivity){
                    AppContext.get().attach(activity)
                }
            }
        })

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun foreground() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun background() {

    }

    override fun getViewModelStore(): ViewModelStore = modelStore


}