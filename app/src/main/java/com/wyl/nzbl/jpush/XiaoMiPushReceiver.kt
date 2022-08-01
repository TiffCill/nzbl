package com.wyl.nzbl.jpush

import android.content.Context
import cn.jpush.android.service.PluginXiaomiPlatformsReceiver
import com.xiaomi.mipush.sdk.MiPushCommandMessage
import com.xiaomi.mipush.sdk.MiPushMessage
import com.xiaomi.mipush.sdk.PushMessageReceiver

class XiaoMiPushReceiver: PushMessageReceiver() {
    private val pluginXiaomiPlatformsReceiver = PluginXiaomiPlatformsReceiver()

    override fun onCommandResult(p0: Context?, p1: MiPushCommandMessage?) {
        super.onCommandResult(p0, p1)
        pluginXiaomiPlatformsReceiver.onCommandResult(p0,p1)
    }

    override fun onNotificationMessageClicked(p0: Context?, p1: MiPushMessage?) {
        super.onNotificationMessageClicked(p0, p1)
        pluginXiaomiPlatformsReceiver.onNotificationMessageClicked(p0, p1)
    }

    override fun onNotificationMessageArrived(p0: Context?, p1: MiPushMessage?) {
        super.onNotificationMessageArrived(p0, p1)
        pluginXiaomiPlatformsReceiver.onNotificationMessageArrived(p0, p1)
    }

    override fun onReceiveRegisterResult(p0: Context?, p1: MiPushCommandMessage?) {
        super.onReceiveRegisterResult(p0, p1)
        pluginXiaomiPlatformsReceiver.onReceiveRegisterResult(p0, p1)
    }

    override fun onRequirePermissions(p0: Context?, p1: Array<out String>?) {
        super.onRequirePermissions(p0, p1)
        pluginXiaomiPlatformsReceiver.onRequirePermissions(p0, p1)
    }

    override fun onReceivePassThroughMessage(p0: Context?, p1: MiPushMessage?) {
        super.onReceivePassThroughMessage(p0, p1)
        pluginXiaomiPlatformsReceiver.onReceivePassThroughMessage(p0, p1)
    }
}