package com.wyl.nzbl

import android.app.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import cn.jiguang.api.JCoreInterface
import cn.jiguang.api.JCoreManager
import cn.jiguang.internal.JConstants
import cn.jiguang.joperate.api.JOperateInterface
import cn.jiguang.verifysdk.api.JVerificationInterface
import cn.jiguang.verifysdk.api.PreLoginListener
import cn.jiguang.verifysdk.api.RequestCallback
import cn.jpush.android.api.JPushInterface
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.MessageEvent
import com.wyl.nzbl.controller.OnMessageEvent
import com.wyl.nzbl.util.Constant
import com.wyl.nzbl.view.Logger
import org.greenrobot.eventbus.EventBus


class MyApp : Application() {
    val TAG = "MyApp"

    companion object {
        var context: Application? = null
        var onMessageEventListener : OnMessageEvent ?= null
        fun getContext(): Context {
            return context!!
        }
        fun addOnMessageEventListener(onMessageEventListener: OnMessageEvent){
            this.onMessageEventListener = onMessageEventListener
        }
    }


    override fun onCreate() {
        super.onCreate()
        //动态获取appkey
        val applicationInfo =
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        Constant.appkey = applicationInfo.metaData?.getString("JPUSH_APPKEY")
        addActivityLifecycleListener()
        if (context == null) {
            context = this
        }

        //设置JCore
        JConstants.CMD_TO_PRINT_ALL_LOG = true  //开启全量日志
        var bundle = Bundle()
        bundle.putInt("heartbeat_interval", 30)  // 设置心跳30s，心跳间隔默认是4min50s
        JCoreManager.setSDKConfigs(this,bundle)
        JCoreInterface.setDebugMode(true)       //debug模式
//        JCollectionAuth.setAuth(this,false)  //不同意隐私协议
        JPushInterface.setDebugMode(true)
        JVerificationInterface.setDebugMode(true)
//        JPushInterface.setLbsEnable(this,false)  //关闭采集地理位置信息
        JPushInterface.init(this)
        JMessageClient.setDebugMode(true)
        JMessageClient.init(this)
        JMessageClient.registerEventReceiver(this)  //注册消息接收
        JVerificationInterface.init(this, 10000,RequestCallback { i, t ->
            if (i == 8000) {
                Logger.e(TAG, "认证初始化成功")
                JVerificationInterface.preLogin(context, 10000, PreLoginListener { i, s ->
                    Log.e(TAG, "onCreate: $i  + $s")
                    EventBus.getDefault().postSticky("可以启动")
                })
            } else {
                Log.e(TAG, "认证初始化失败 $i   $t")
            }
        })

        JOperateInterface.setDebug(true)
        JOperateInterface.getInstance(applicationContext).init()


        Constant.cuid = JOperateInterface.getInstance(this).cuid
        Logger.d("JOperate","cuid == ${Constant.cuid}")


//        JUnionInterface.init(this)
//
//        //JAd初始化
//        JAdApi.config(
//            this, JAdConfig.Builder() // 是否开启日志，默认false
//                .setLoggerEnable(true)
//                .build()
//        )
//        // 应用内广告初始化
//        JNativeAdApi.init(this)
//        // 通知栏广告初始化
//        JNotifyAdApi.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        JMessageClient.unRegisterEventReceiver(this)
    }
    fun onEvent(event : MessageEvent){
        Logger.d("MyApp  getNewMessage : ", "${event.message.toJson()}")
        if (onMessageEventListener!=null){
            onMessageEventListener!!.getNewMessage(event)
        }else{
            Logger.e("MyApp  getNewMessage : ","onMessageEventListener is null")
        }
    }



    private fun addActivityLifecycleListener() {
        context?.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.e(TAG, "onActivityCreated: ${activity::class.java.simpleName}")
            }

            override fun onActivityStarted(activity: Activity) {
                Log.e(TAG, "onActivityStarted: ${activity::class.java.simpleName}")
            }

            override fun onActivityResumed(activity: Activity) {
                Log.e(TAG, "onActivityResumed: ${activity::class.java.simpleName}")
            }

            override fun onActivityPaused(activity: Activity) {
                Log.e(TAG, "onActivityPaused: ${activity::class.java.simpleName}")
            }

            override fun onActivityStopped(activity: Activity) {
                Log.e(TAG, "onActivityStopped: ${activity::class.java.simpleName}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.e(TAG, "onActivitySaveInstanceState: ${activity::class.java.simpleName}")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.e(TAG, "onActivityDestroyed: ${activity::class.java.simpleName}")
            }
        })
    }

}