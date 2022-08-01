package com.wyl.nzbl.api

import android.util.Log
import com.wyl.nzbl.api.NetWorkAPi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetWorkClient {
    private const val BASE_URL="https://www.wanandroid.com/"

    val TAG = NetWorkClient::class.java.simpleName

    var logger  = HttpLoggingInterceptor(){
        Log.e(TAG, ": $it")
    }

    private val client = OkHttpClient.Builder()
        .readTimeout(10,TimeUnit.SECONDS)
        .writeTimeout(10,TimeUnit.SECONDS)
        .addInterceptor(logger)
        .build()

    fun createNetWorkClient(): NetWorkAPi? {
        logger.level =HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetWorkAPi::class.java)
    }
}