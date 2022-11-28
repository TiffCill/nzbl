package com.wyl.nzbl.controller

import cn.jpush.im.android.api.event.MessageEvent

interface OnMessageEvent {
    fun getNewMessage(event: MessageEvent)
}