package com.jie.tungcheung.base

import com.jie.tungcheung.utils.enshrine.Store
import com.jie.tungcheung.utils.http.ApiService
import com.jie.tungcheung.utils.http.main.Http
import com.jie.tungcheung.utils.http.main.http
import com.jie.tungcheung.utils.process.CrashHandler
import com.jie.tungcheung.utils.http.interceptor.ReadCookieInterceptor
import com.jie.tungcheung.utils.http.interceptor.RequestLogInterceptor
import com.jie.tungcheung.utils.http.main.ReadWriteCacheInterceptor
import com.jie.tungcheung.utils.http.main.RequestNewHeaderInterceptor
import com.jie.tungcheung.utils.kInject.core.initScope

//Created by Priscilla Cheung 2021年12月15日18:09:54.
class MyApplication: BaseApplication() {
    override fun onCreate() {
        super.onCreate()


        this.http {
            config {
                baseUrl = "http://zhitongche.bailingkeji.com/api/"
                interceptors = arrayListOf(
                    RequestNewHeaderInterceptor(), ReadWriteCacheInterceptor(),
                    RequestLogInterceptor(), ReadCookieInterceptor()
                )
            }
        }
        //初始化
        initScope {
            single { Http.createApi(ApiService::class.java) }
        }
        myApplication = this
        myApplication?.let {
            Store.initialize(it)
            CrashHandler.init(it, CrashHandler.Mode.KeepAlive)
        }
    }
    companion object {
        var myApplication: MyApplication? = null
    }
}