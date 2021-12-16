package com.jie.tungcheung.utils.binding

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.jie.tungcheung.base.BaseActivity
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType


object Binding {


    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    fun <V : ViewBinding> getBinding(activity: BaseActivity<V, *>): V? {
        var binding: V? = null
        val types = (activity.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[0] as Class<V>
        if (ViewBinding::class.java.isAssignableFrom(clazz) && !ViewDataBinding::class.java.isAssignableFrom(clazz)) {
            val method = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
            binding = method.invoke(clazz, activity.layoutInflater) as V?
            activity.setContentView(binding!!.root)
        } else if (ViewDataBinding::class.java.isAssignableFrom(clazz)) {
            binding = DataBindingUtil.setContentView<ViewDataBinding>(activity, activity.getViewId()) as V?
        }
        return binding
    }



    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    fun <V : ViewBinding> getBinding(any: Any, view: View,typeIndex:Int): V? {
        var binding: V? = null
        val types = (any.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[typeIndex] as Class<V>
        if (ViewBinding::class.java.isAssignableFrom(clazz) && !ViewDataBinding::class.java.isAssignableFrom(clazz)) {
            val method = clazz.getDeclaredMethod("bind", View::class.java)
            binding = method.invoke(clazz, view) as V?
        } else if (ViewDataBinding::class.java.isAssignableFrom(clazz)) {
            binding = DataBindingUtil.bind<ViewDataBinding>(view) as V?
        }
        return binding
    }




    inline fun <reified V : ViewBinding> binding(view: View): V? {
        val clazz = V::class.java
        return if (ViewBinding::class.java.isAssignableFrom(clazz)
            && !ViewDataBinding::class.java.isAssignableFrom(clazz)
        ) {
            /**
             * For ViewBinding Only
             */
            val method = clazz.getDeclaredMethod("bind", View::class.java)
            val binding = method.invoke(clazz, view) as V?
            binding
        } else {
            /**
             * For ViewDataBinding Only
             */
            DataBindingUtil.bind<ViewDataBinding>(view) as V?
        }
    }

}