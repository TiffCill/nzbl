package com.wyl.nzbl.repository

import com.wyl.nzbl.api.NetWorkClient

class HomeRepository {

    suspend fun getBanner() = NetWorkClient.createNetWorkClient()?.getBanner()

    suspend fun getHotKey() = NetWorkClient.createNetWorkClient()?.getHotkey()

    suspend fun getHomeTabs() = NetWorkClient.createNetWorkClient()?.getTabs()

    suspend fun toSearch(page:Int,key:String) = NetWorkClient.createNetWorkClient()?.toSearch("/article/query/$page/json",key)
}