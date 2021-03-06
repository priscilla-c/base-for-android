package com.jie.tungcheung.utils.http.coroutines

import com.jie.tungcheung.utils.log.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class KRequest<T>(private val request: suspend () -> Response<T>) {

    companion object {
        private val TAG = "Request"
    }

    private var timeOut = 15000L
    private var repeatTime = 1
    private var tryCount = 0

    fun timeOut(time: Long): KRequest<T> {
        timeOut = time
        return this
    }

    fun repeatTime(repeat: Int): KRequest<T> {
        repeatTime = repeat
        return this
    }


    suspend fun addAsync(
        scope: CoroutineScope,
        onError: (suspend Throwable.() -> Unit)?
    ): Response<T>? {
        return tryRepeat(scope, onError)
    }

    private suspend fun <T> Deferred<T>.awaitWithTimeout(time: Long): T? {
        return withTimeoutOrNull(time) { await() }
    }


    private suspend fun tryRepeat(
        scope: CoroutineScope,
        onError: (suspend Throwable.() -> Unit)?
    ): Response<T>? {
        return kotlin.runCatching {
            val out = scope.async {
                request.invoke()
            }.awaitWithTimeout(timeOut)
            Logger.dLog(TAG, "response = $out")
            if (out == null && tryCount < repeatTime) {
                tryCount++
                tryRepeat(scope, onError)
            } else {
                out
            }
        }.onFailure {
            it.printStackTrace()
            onError?.invoke(it)
        }.getOrNull()
    }

}