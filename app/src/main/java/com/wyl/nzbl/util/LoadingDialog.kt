package com.wyl.nzbl.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.wyl.nzbl.R

class LoadingDialog {


    companion object{
        var loadingDialog: LoadingDialog? = null
        private var dialog: PopupWindow? = null
        private var inflater: View? = null
        var activty: Activity? = null
        fun getInterface(context: Activity): LoadingDialog {
            this.activty = context
            if (null == loadingDialog) {
                synchronized(context) {
                    if (null == loadingDialog) {
                        loadingDialog = LoadingDialog()
                        settingDialog()
                    }
                }
            }
            return loadingDialog!!
        }

        private fun settingDialog() {
            inflater = LayoutInflater.from(activty).inflate(R.layout.dialog_loading, null)
            dialog = PopupWindow(inflater, ViewGroup.LayoutParams.WRAP_CONTENT, 300)
            dialog!!.setBackgroundDrawable(ColorDrawable())
            dialog!!.isOutsideTouchable = false
        }
    }

    fun closeDialog(){
        if (dialog!!.isShowing)
        dialog?.dismiss()
    }

    /**
     * 控制loading图的展示隐藏
     */
    fun handleDialog(view: View) {
        if (dialog!!.isShowing) dialog?.dismiss() else {
            dialog?.contentView =
                LayoutInflater.from(activty).inflate(R.layout.dialog_loading, null)
            dialog?.showAtLocation(view, Gravity.CENTER, 0, 0)
        }

        var myWindow = activty?.window?.attributes
        myWindow?.alpha = if (myWindow?.alpha == 0.5f) 1.0f else 0.5f
        activty?.window?.attributes = myWindow
    }

}