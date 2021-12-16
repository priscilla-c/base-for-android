package com.jie.tungcheung.bean

import com.jie.tungcheung.utils.http.main.JsonResult

class BaseData<T> : JsonResult {
    var code: Int = 400
    var msg: String? = null
    var data :T? = null
    override fun isLegal(): Boolean {
        return code == 200
    }
}