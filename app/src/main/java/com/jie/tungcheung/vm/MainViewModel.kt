package com.jie.tungcheung.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jie.tungcheung.bean.BaseDao
import com.jie.tungcheung.bean.BaseDao_
import com.jie.tungcheung.bean.BaseData
import com.jie.tungcheung.bean.Test
import com.jie.tungcheung.utils.enshrine.DataBaseStore
import com.jie.tungcheung.utils.http.coroutines.KResultData
import io.objectbox.query.QueryBuilder

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository by lazy { MainRepository(this) }
    val test by lazy { KResultData<BaseData<List<Test>>>() }

    fun test() {
        repository.test(test)
       /* val list = DataBaseStore.expertQuery<BaseDao>{
            equal(BaseDao_.id,10)
            notEqual(BaseDao_.id,10)
            contains(BaseDao_.id,"10",QueryBuilder.StringOrder.CASE_INSENSITIVE)
        }*/
    }
}