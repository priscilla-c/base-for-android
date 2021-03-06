package com.jie.tungcheung.utils.http.coroutines

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KResponse<T>(val data: MutableLiveData<KResult<T>>? = null) : IResponse<T> {


    override suspend fun doOnLoading() {
        postData(KResult(KResult.Loading))
        onLoading?.apply {
            withContext(Dispatchers.Main) {
                invoke()
            }
        }
    }

    override suspend fun doOnTimeOut() {
        val out = KResult<T>(KResult.TimeOut)
        postData(out)
        loadingTimeOut?.apply {
            withContext(Dispatchers.Main) {
                invoke(out)
            }
        }
    }


    override suspend fun doOnError(e: Throwable, t: T?) {
        val out = KResult<T>(KResult.Failure, exception = Exception(e), response = t)
        postData(out)
        onError?.apply {
            withContext(Dispatchers.Main) {
                invoke(out)
            }
        }
    }

    override suspend fun doOnSuccess(t: T?) {
        val out = KResult(KResult.Success, t)
        postData(out)
        onSuccess?.apply {
            withContext(Dispatchers.Main) {
                invoke(out)
            }
        }
    }


    private var onLoading: (() -> Unit)? = null

    fun loading(onLoading: (() -> Unit)): KResponse<T> {
        this.onLoading = onLoading
        return this
    }

    private var loadingTimeOut: ((result: KResult<T>) -> Unit)? = null
    fun timeOut(loadingTimeOut: (KResult<T>.() -> Unit)): KResponse<T> {
        this.loadingTimeOut = loadingTimeOut
        return this
    }


    private var onSuccess: ((result: KResult<T>) -> Unit)? = null

    private var onError: ((result: KResult<T>) -> Unit)? = null

    fun success(onSuccess: (KResult<T>.() -> Unit)): KResponse<T> {
        this.onSuccess = onSuccess
        return this
    }


    fun error(onError: (KResult<T>.() -> Unit)): KResponse<T> {
        this.onError = onError
        return this
    }

    private fun setData(call: KResult<T>): KResult<T> {
        data?.value = call
        return call
    }

    private fun postData(call: KResult<T>): KResult<T> {
        data?.postValue(call)
        return call
    }


}