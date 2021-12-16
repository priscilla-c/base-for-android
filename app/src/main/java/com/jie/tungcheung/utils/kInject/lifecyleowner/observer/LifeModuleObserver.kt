package com.jie.tungcheung.utils.kInject.lifecyleowner.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jie.tungcheung.utils.kInject.core.qualifier.Qualifier
import com.jie.tungcheung.utils.kInject.core.register.ModuleRegister


class LifeModuleObserver(var qualifier: Qualifier<*>) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        ModuleRegister.instant.removeEntry(qualifier)
    }

}