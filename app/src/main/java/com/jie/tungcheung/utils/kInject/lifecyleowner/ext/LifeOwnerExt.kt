package com.jie.tungcheung.utils.kInject.lifecyleowner.ext

import androidx.lifecycle.LifecycleOwner
import com.jie.tungcheung.utils.kInject.core.Logger
import com.jie.tungcheung.utils.kInject.core.qualifier.StringQualifier
import com.jie.tungcheung.utils.kInject.core.register.ModuleRegister

inline fun <reified T> getLifeOwner(scopeClazz: T) : LifecycleOwner {
    return ModuleRegister.instant.getEntry(StringQualifier().apply {
        setKeyName(scopeClazz.toString())
        Logger.log("inject Qualifier ${this}")
    })?.get(scopeClazz.toString()) as LifecycleOwner
}

inline fun <reified T> getLifeOwnerOrNull(scopeClazz: T) : LifecycleOwner? {
    return ModuleRegister.instant.getEntry(StringQualifier().apply {
        setKeyName(scopeClazz.toString())
        Logger.log("inject Qualifier ${this}")
    })?.get(scopeClazz.toString()) as LifecycleOwner?
}


inline fun <reified T> injectLifeOwner(scopeClazz: T) : Lazy<LifecycleOwner> {
    return lazy {
        ModuleRegister.instant.getEntry(StringQualifier().apply {
            setKeyName(scopeClazz.toString())
            Logger.log("inject Qualifier ${this}")
        })?.get(scopeClazz.toString()) as LifecycleOwner
    }
}
