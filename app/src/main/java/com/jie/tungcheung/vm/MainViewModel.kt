package com.jie.tungcheung.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jie.tungcheung.bean.BaseData
import com.jie.tungcheung.bean.Test
import com.jie.tungcheung.utils.http.coroutines.KResultData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository by lazy { MainRepository(this) }
    val test by lazy { KResultData<BaseData<List<Test>>>() }

    fun test() {
        repository.test(test)
    }
}