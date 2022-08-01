package com.wyl.nzbl

import android.app.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import cn.jiguang.ads.nativ.api.JNativeAdApi
import cn.jiguang.ads.notify.api.JNotifyAdApi
import cn.jiguang.api.JCoreInterface
import cn.jiguang.api.JCoreManager
import cn.jiguang.api.utils.JCollectionAuth
import cn.jiguang.internal.JConstants
import cn.jiguang.junion.JUnionInterface
import cn.jiguang.union.ads.base.api.JAdApi
import cn.jiguang.union.ads.base.api.JAdConfig
import cn.jiguang.verifysdk.api.JVerificationInterface
import cn.jiguang.verifysdk.api.PreLoginListener
import cn.jiguang.verifysdk.api.RequestCallback
import cn.jpush.android.api.JPushInterface
import org.greenrobot.eventbus.EventBus


class MyApp : Application() {
    val TAG = "MyApp"

    companion object {
        var context: Application? = null
        fun getContext(): Context {
            return context!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        JConstants.CMD_TO_PRINT_ALL_LOG = true
        addActivityLifecycleListener()
        if (context == null) {
            context = this
        }

        var bundle = Bundle()
// 设置心跳30s，心跳间隔默认是4min50s
        bundle.putInt("heartbeat_interval", 30)
        JCoreManager.setSDKConfigs(this,bundle)
        JCoreInterface.setDebugMode(true)
//        JCollectionAuth.setAuth(this,false)  //不同意隐私协议
        JPushInterface.setDebugMode(true)
        JVerificationInterface.setDebugMode(true)
        JPushInterface.setLbsEnable(this,false)
        JPushInterface.init(this)
        JVerificationInterface.init(this, RequestCallback { i, t ->
            if (i == 8000) {
                Log.e(TAG, "认证初始化成功")
                JVerificationInterface.preLogin(context, 10000, PreLoginListener { i, s ->
                    Log.e(TAG, "onCreate: $i  + $s")
                    EventBus.getDefault().postSticky("可以启动")
                })
            } else {
                Log.e(TAG, "认证初始化失败 $i   $t")
            }
        })
        JUnionInterface.init(this)

        //JAd初始化
        JAdApi.config(
            this, JAdConfig.Builder() // 是否开启日志，默认false
                .setLoggerEnable(true)
                .build()
        )
        // 应用内广告初始化
        JNativeAdApi.init(this)
        // 通知栏广告初始化
        JNotifyAdApi.init(this)
    }

    private fun addActivityLifecycleListener() {
        context?.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.e(TAG, "onActivityCreated: $activity::class.java.simpleName")
            }

            override fun onActivityStarted(activity: Activity) {
                Log.e(TAG, "onActivityStarted: $activity::class.java.simpleName")
            }

            override fun onActivityResumed(activity: Activity) {
                Log.e(TAG, "onActivityResumed: $activity::class.java.simpleName")
            }

            override fun onActivityPaused(activity: Activity) {
                Log.e(TAG, "onActivityPaused: $activity::class.java.simpleName")
            }

            override fun onActivityStopped(activity: Activity) {
                Log.e(TAG, "onActivityStopped: $activity::class.java.simpleName")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.e(TAG, "onActivitySaveInstanceState: $activity::class.java.simpleName")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.e(TAG, "onActivityDestroyed: $activity::class.java.simpleName")
            }
        })
    }

}