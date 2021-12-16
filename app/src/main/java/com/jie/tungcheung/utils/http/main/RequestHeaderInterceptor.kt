package com.jie.tungcheung.utils.http.main

import androidx.annotation.Keep
import com.jie.tungcheung.utils.enshrine.KeyValueStore
import com.jie.tungcheung.utils.enshrine.Store
import com.jie.tungcheung.utils.log.Logger
import okhttp3.Interceptor
import java.io.IOException

@Keep
class RequestNewHeaderInterceptor : Interceptor {

    var sessionId = ""

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val originalHttpUrl = request.url
        val htbuilder = originalHttpUrl.newBuilder()


        val url = htbuilder.build()
        val builder = request.newBuilder().url(url)

        this.sessionId = KeyValueStore.getString("sessionId", "") ?: ""
        Logger.dLog("RequestNewHeaderInterceptor","JSESSIONID=$sessionId")
        builder.addHeader("Cookie", "JSESSIONID=$sessionId")
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept", "application/json")
        return chain.proceed(builder.build())
    }
}