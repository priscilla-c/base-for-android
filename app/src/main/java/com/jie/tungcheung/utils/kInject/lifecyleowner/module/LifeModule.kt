package com.jie.tungcheung.utils.kInject.lifecyleowner.module

import androidx.lifecycle.LifecycleOwner
import com.jie.tungcheung.utils.kInject.core.module.Module
import com.jie.tungcheung.utils.kInject.core.qualifier.Qualifier
import com.jie.tungcheung.utils.kInject.lifecyleowner.observer.LifeModuleObserver

fun LifecycleOwner.lifeModule(scope: LifeModule.()->Unit): LifeModule {
    val moduleBean = LifeModule(this)
    scope.invoke(moduleBean)
    return moduleBean
}

class LifeModule(var lifecycleOwner: LifecycleOwner) : Module() {

    override fun setParentKey(qualifier: Qualifier<*>?) {
        qualifier?.apply {
            lifecycleOwner.lifecycle.addObserver(LifeModuleObserver(qualifier))
        }
    }

    fun scopeLifeOwner(scopeClazz:Any){
        scopeByName(scopeClazz.toString()) { lifecycleOwner }
    }

}