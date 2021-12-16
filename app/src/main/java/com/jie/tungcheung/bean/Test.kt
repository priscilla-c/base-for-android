package com.jie.tungcheung.bean

//Created by Priscilla Cheung 2021年12月16日14:06:15 .

data class Test(
    var area: String = "",
    var factory: String = "",
    var hx_password: String = "",
    var hx_username: String = "",
    var hx_uuid: String = "",
    var id: Int = 0,
    var imgs: List<String> = listOf(),
    var logo: String = "",
    var name: String = "",
    var officeaddr: String = "",
    var scope: String = "",
    var servicetype: Int = 0
){
    var adr = "${officeaddr} ${area}"
}