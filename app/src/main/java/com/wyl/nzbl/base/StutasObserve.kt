package com.wyl.nzbl.base

import android.content.Context
import com.wyl.nzbl.MyApp

class StatusObserve {
    var statusListener:StatusListener? = null

    companion object {
        var statusObserve: StatusObserve? = null
        fun getInterface(context: Context): StatusObserve {
            if (statusObserve == null) {
                synchronized(context){
                    if (statusObserve == null){
                        statusObserve = StatusObserve()
                    }
                }
            }
            return statusObserve!!
        }
    }

    fun setListener(statusListener: StatusListener){
        this.statusListener = statusListener
    }

    fun getListener(): StatusListener? {
        return this.statusListener
    }
}