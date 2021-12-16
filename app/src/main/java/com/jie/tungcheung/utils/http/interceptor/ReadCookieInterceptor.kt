package com.jie.tungcheung.utils.http.interceptor
import com.jie.tungcheung.utils.enshrine.KeyValueStore
import com.jie.tungcheung.utils.enshrine.Store
import okhttp3.Interceptor
import okhttp3.Response


class ReadCookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url.toString()
        val host = request.url.host
        if(requestUrl.contains("user/login") && response.headers("set-cookie").isNotEmpty()){
            val cookies = response.headers("set-cookie")
            encodeCookie(cookies)
        }
        return response
    }
}

fun encodeCookie(cookies:List<String>){
    cookies.forEach Continuing@{
        if(it.contains("JSESSIONID")){
            val cookie = it.substringAfter("JSESSIONID=").substringBefore(";")
            KeyValueStore.put("sessionId",cookie)
            return@Continuing
        }
    }
}