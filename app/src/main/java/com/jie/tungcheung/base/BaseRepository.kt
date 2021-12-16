package com.jie.tungcheung.base

import androidx.lifecycle.*
import com.jie.tungcheung.utils.kInject.lifecyleowner.ext.getLifeOwner
import com.jie.tungcheung.utils.log.Logger

open class BaseRepository(var viewModel: ViewModel?) : LifecycleObserver {


    private var owner : LifecycleOwner? = null
    init {
        init()
        Logger.eLog(BaseRepository::class.java.name,"Inject $viewModel")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        onClear()
    }

    private fun init() {
        if(owner == null){
            owner = getLifeOwner(viewModel)
        }
        owner?.lifecycle?.addObserver(this)
    }


    /**
     * 适当使用避免造成内存泄漏
     */
    private fun onClear() {
        owner?.lifecycle?.removeObserver(this)
        owner = null
    }


    fun androidScope(scope:LifecycleOwner?.()->Unit){
        if(owner == null){
            owner = getLifeOwner(viewModel)
            owner?.lifecycle?.addObserver(this)
        }
        scope.invoke(owner)
    }


}
