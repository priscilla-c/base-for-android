package com.jie.tungcheung.utils.kInject.core.register

import com.jie.tungcheung.utils.kInject.core.Logger
import com.jie.tungcheung.utils.kInject.core.module.Module
import com.jie.tungcheung.utils.kInject.core.qualifier.Qualifier
import com.jie.tungcheung.utils.kInject.core.qualifier.StringQualifier
import java.util.concurrent.ConcurrentHashMap



class ModuleRegister private constructor(){

    companion object{
        val instant by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ModuleRegister() }
    }

    private val entry = ConcurrentHashMap<Qualifier<*>, Module>()


    fun addEntry(qualifier: StringQualifier, any: Module){
        any.qualifier = qualifier
        entry[qualifier] = any
        Logger.log("ModuleRegister : ${entry}")
    }

    fun getEntry(qualifier: StringQualifier) : Module? = entry[qualifier]

    fun removeEntry(qualifier: Qualifier<*>){
        val module = entry[qualifier]
        module?.clear()
        entry.remove(qualifier)
    }

}