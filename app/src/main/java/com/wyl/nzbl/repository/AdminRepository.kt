package com.wyl.nzbl.repository

import com.wyl.nzbl.api.NetWorkClient

class AdminRepository {

    suspend fun toLogin(username:String,password:String) = NetWorkClient.createNetWorkClient()?.toLogin(username, password)

    suspend fun toRegister(username: String,password: String,repassword:String) = NetWorkClient.createNetWorkClient()?.toRegister(username, password, repassword)

}