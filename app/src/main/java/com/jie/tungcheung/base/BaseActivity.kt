package com.jie.tungcheung.base

import android.app.ActivityOptions
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.jie.tungcheung.utils.binding.ViewDataBindRef
import com.jie.tungcheung.utils.kInject.core.initScope
import com.jie.tungcheung.utils.kInject.lifecyleowner.module.lifeModule
import com.jie.tungcheung.utils.binding.Binding
import com.jie.tungcheung.utils.enshrine.hideSoftKeyboard
import com.jie.tungcheung.utils.enshrine.toast
import com.jie.tungcheung.utils.log.Logger
import java.lang.reflect.ParameterizedType
import java.util.*



abstract class BaseActivity<V : ViewBinding, VM : AndroidViewModel> : AppCompatActivity() {
    private val receiver by lazy { LifeActiveReceiver() }
    var binding: V by ViewDataBindRef()

    val viewModel: VM by lazy {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[1] as Class<VM>
        ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(clazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        Binding.getBinding(this)?.apply {
            binding = this
        }

        AppManager.get().addActivityCls(this::class.java.name)

        initScope {
            module(viewModel, lifeModule {
                scopeLifeOwner(viewModel)
            })
        }

        intent.apply {
            extras?.apply {
                BundleAutoInject.inject(this@BaseActivity)
                onBundle(this)
            }
        }
        registerReceiver(receiver, IntentFilter().apply {
            addAction(this@BaseActivity::class.java.name)
        })
        observerUI()
        init(savedInstanceState)
        initListener()
    }


    inline fun <reified VM : ViewModel> createViewModel(): VM {
        return viewModels<VM> { ViewModelProvider.AndroidViewModelFactory(application) }.value
    }


    abstract fun getViewId(): Int

    //abstract fun initViewModel() : VM

    abstract fun onBundle(bundle: Bundle)

    abstract fun observerUI()

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initListener()


    override fun onBackPressed() {
        finishAfterTransition()
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideSoftKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    // 如果焦点不是EditText则忽略
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }



    override fun onDestroy() {
        AppManager.get().removeActivity(this::class.java.name)
        unregisterReceiver(receiver)
        super.onDestroy()
    }


    inner class LifeActiveReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.apply {
                if (this@BaseActivity::class.java.name == action) {
                    Logger.dLog("LifeActiveReceiver", "finishAfterTransition")
                    onBackPressed()
                }
            }
        }
    }

    fun binding(block: V.() -> Unit) {
        block.invoke(binding)
    }


}