package com.jie.tungcheung.utils.http.interceptor

import android.util.Log
import com.jie.tungcheung.utils.log.Logger

import java.nio.charset.Charset

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

class RequestLogInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestBody = request.body
        val responseBody = response.body
        val responseBodyString = responseBody?.string()
        var requestMessage: String = request.method + ' '.toString() + request.url

        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            requestMessage += "?\n" + buffer.readString(UTF8)
        }
        Logger.eLog("RequestLogInterceptor", requestMessage)
        Log.e("TAG", "intercept: ${response.newBuilder().body(responseBodyString?.toByteArray()?.toResponseBody(responseBody.contentType())).build()}", )
        return response.newBuilder().body(responseBodyString?.toByteArray()?.toResponseBody(responseBody.contentType())).build()
    }

    companion object {

        private val UTF8 = Charset.forName("UTF-8")
    }


}
