package com.wyl.nzbl.jpush

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import cn.jpush.android.api.*
import cn.jpush.android.service.JPushMessageReceiver
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.base.StatusListener
import com.wyl.nzbl.base.StatusObserve
import com.wyl.nzbl.ui.activity.AutoLoginActivity
import com.wyl.nzbl.view.Logger
import org.greenrobot.eventbus.EventBus

class JpushReceiver : JPushMessageReceiver() {
    val TAG = JpushReceiver::class.java.simpleName
    override fun onMessage(p0: Context?, p1: CustomMessage?) {
        super.onMessage(p0, p1)
        Log.e(TAG, "onMessage: $p1")
    }

    override fun onNotifyMessageArrived(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageArrived(p0, p1)
        Log.e(
            TAG,
            "onNotifyMessageArrived: [messageId] ${p1?.msgId}   []${p1?.notificationContent}"
        )
        p1?.notificationExtras
    }


    override fun onAliasOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onAliasOperatorResult(p0, p1)
        Log.i(TAG, "onAliasOperatorResult: ${p1.toString()}")
    }

    override fun onTagOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onTagOperatorResult(p0, p1)
        Log.i(TAG, "onTagOperatorResult: ${p1.toString()}")
    }

    override fun onNotifyMessageOpened(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageOpened(p0, p1)
        Log.e(TAG, "onNotifyMessageOpened: ${p1.toString()}")
        var intent = Intent(p0, AutoLoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        p0?.startActivity(intent)
    }

    override fun onRegister(p0: Context?, p1: String?) {
//        super.onRegister(p0, p1)
        Log.e(
            TAG,
            "onRegister: $p1   ${NotificationManagerCompat.from(p0!!).areNotificationsEnabled()}"
        )
        EventBus.getDefault().postSticky(p1)
        var nm = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.deleteNotificationChannel("JPush_2_0")
            nm.deleteNotificationChannel("JPush_3_7")
            var nm: NotificationManager =
                p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannelGroups = nm.notificationChannelGroups
            for (i: NotificationChannelGroup in notificationChannelGroups) {
                Log.e(TAG, "     logChannel: ${i}\n")
            }
            for (i: NotificationChannel in nm.notificationChannels) {
                Log.e(TAG, "     logChannel: ${i}\n")
            }
        }
    }

    override fun onConnected(p0: Context?, p1: Boolean) {
        super.onConnected(p0, p1)
        Logger.i(TAG, "onConneted")
        StatusObserve.getInterface(MyApp.context!!).getListener()?.onConnectStatus(p1)
    }



    override fun onCommandResult(p0: Context?, p1: CmdMessage?) {
        Log.e(TAG, "[onCommandResult] $p1")
        if (p1 != null && p1?.cmd == 10000 && p1?.extra != null) {
            val token = p1?.extra.getString("token")
            val platform = p1?.extra.getInt("platform")
            var deviceName = "unknown"
            when (platform) {
                1 -> deviceName = "小米"
                2 -> deviceName = "华为"
                3 -> deviceName = "魅族"
                4 -> deviceName = "OPPO"
                5 -> deviceName = "VIVO"
                6 -> deviceName = "ASUS"
                7 -> deviceName = "FCM"
                8 -> deviceName = "小米"
            }
            Log.e(TAG, "onCommandResult: 获取到 $deviceName 的token : $token")
        }
    }
}