package com.jie.tungcheung.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jie.tungcheung.base.BaseRepository
import com.jie.tungcheung.bean.BaseData
import com.jie.tungcheung.bean.Test
import com.jie.tungcheung.utils.http.ApiService
import com.jie.tungcheung.utils.http.coroutines.KResult
import com.jie.tungcheung.utils.http.coroutines.callResult
import com.jie.tungcheung.utils.kInject.core.ext.single


class MainRepository(viewModel: ViewModel?) : BaseRepository(viewModel) {
    private val api: ApiService by single()

    fun test(data: MutableLiveData<KResult<BaseData<List<Test>>>>) {
        androidScope {
            callResult {
                hold(data) {
                    api.test(hashMapOf(Pair("page",1), Pair("limit",10)))
                }
            }
        }
    }
}