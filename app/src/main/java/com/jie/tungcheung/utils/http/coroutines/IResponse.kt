package com.jie.tungcheung.utils.http.coroutines

interface IResponse<T> {

    suspend fun doOnLoading()

    suspend fun doOnTimeOut()

    suspend fun doOnError(e:Throwable,t: T?)

    suspend fun doOnSuccess(t: T?)

}