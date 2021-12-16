package com.jie.tungcheung.utils.http

import com.jie.tungcheung.bean.BaseData
import com.jie.tungcheung.bean.Test
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("Index/getfact")
    suspend fun test(@QueryMap body: HashMap<String, Int>): Response<BaseData<List<Test>>>

}