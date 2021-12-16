package com.jie.tungcheung.utils.binding

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.BuildConfig
import androidx.viewbinding.ViewBinding
import java.lang.ref.SoftReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class ViewBindRef<T : ViewBinding> : ReadWriteProperty<Any, T>,
    LifecycleObserver {

    private var binding: T? = null
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "$thisRef  Set ViewBinding $value")
        }
        binding = value
        if (thisRef is LifecycleOwner) {
            thisRef.lifecycle.removeObserver(this)
            thisRef.lifecycle.addObserver(this)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = binding
        ?: throw IllegalAccessException("Do not try to call the ViewDataBinding outside the Activity view lifecycle")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "onDestroy ViewDataBindRef")
        }
        binding = null
    }
}

class ViewDataBindRef<T : ViewBinding> : ReadWriteProperty<Any, T>,
    LifecycleObserver {

    private var binding: T? = null
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "$thisRef  Set DataBinding $value")
        }
        binding = value
        if (thisRef is LifecycleOwner) {
            if (binding is ViewDataBinding) {
                (binding as ViewDataBinding).lifecycleOwner = thisRef
            }
            thisRef.lifecycle.removeObserver(this)
            thisRef.lifecycle.addObserver(this)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = binding
        ?: throw IllegalAccessException("Do not try to call the ViewDataBinding outside the Activity view lifecycle")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "onDestroy ViewDataBindRef")
        }
        binding = null
    }
}

class DialogFragmentRef<T : DialogFragment>(val clazz: Class<T>) : Lazy<T> {

    private var soft: SoftReference<T>? = null

    override val value: T
        get() = if (soft != null && soft?.get() != null) {
            soft?.get()!!
        } else {
            soft = SoftReference(clazz.newInstance())
            soft?.get()!!
        }

    override fun isInitialized(): Boolean {
        return (soft == null || soft?.get() == null)
    }
}