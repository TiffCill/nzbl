package com.wyl.nzbl.base

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.jpush.android.api.JPushInterface
import com.wyl.nzbl.MyApp


abstract class BaseActivity<VM : ViewModel, DB : ViewDataBinding>(
    var layoutId: Int,
    val vmClass: Class<VM>
) : AppCompatActivity(), StatusListener {
    protected lateinit var mViewModel: VM
    protected lateinit var mDataBinding: DB
    protected lateinit var mContext: Context
    protected lateinit var mActivity: Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = baseContext
        mActivity = this
        mDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(vmClass)
        initView()
        initVM()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            initData()
            initVariable()
        }
    }

    override fun onConnectStatus(isConnected: Boolean) {
        if (isConnected) initData()
    }


    protected abstract fun initView()
    protected abstract fun initVM()
    protected abstract fun initData()
    protected abstract fun initVariable()

    override fun onResume() {
        super.onResume()
        val isRunning = JPushInterface.isPushStopped(MyApp.context)
        if (isRunning) JPushInterface.resumePush(MyApp.context)
        Log.e("BaseActivity", "onResume: $isRunning")
    }

    override fun onStop() {
        super.onStop()
        val isRunning = JPushInterface.isPushStopped(MyApp.context)
        if (!isRunning) JPushInterface.stopPush(MyApp.context)
        Log.e("BaseActivity", "onStop: $isRunning")
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    open fun isAppOnForeground(): Boolean {
        // Returns a list of application processes that are running on the
        // device
        val activityManager =
            applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val packageName = applicationContext.packageName
        val appProcesses = activityManager
            .runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName == packageName && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }
}