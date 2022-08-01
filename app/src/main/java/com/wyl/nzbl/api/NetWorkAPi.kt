package com.wyl.nzbl.api

import com.wyl.nzbl.model.BaseResponse
import com.wyl.nzbl.model.home.*
import com.wyl.nzbl.model.login.UserData
import retrofit2.http.*

interface NetWorkAPi {

    //注册
    @FormUrlEncoded
    @POST("user/register")
    suspend fun toRegister(@Field("username")username:String,@Field("password")password:String,@Field("repassword")repassword:String):BaseResponse<UserData>

    //登陆
    @FormUrlEncoded
    @POST("user/login")
    suspend fun toLogin(@Field("username")username: String,@Field("password")password: String):BaseResponse<UserData>

    //首页banner
    @GET("banner/json")
    suspend fun getBanner():BaseResponse<List<HomeBannerData>>

    //搜索热词
    @GET("/hotkey/json")
    suspend fun getHotkey():BaseResponse<List<SearchHotKeyData>>

    //获取首页tab列表
    @GET("wxarticle/chapters/json")
    suspend fun getTabs():BaseResponse<List<HomeTabData>>

    //搜索
    @FormUrlEncoded
    @POST
    suspend fun toSearch(@Url()url:String,@Field("k")key:String): BaseResponse<SearchData>
}