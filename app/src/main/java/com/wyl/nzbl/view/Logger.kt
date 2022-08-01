package com.wyl.nzbl.view

import android.util.Log
import com.wyl.nzbl.MyApp

class Logger {
    companion object {
        val TAG = MyApp.getContext().packageName
        fun i(tag: String , str : String){
            Log.i(TAG, "[   $tag   ] :  $str")
        }
        fun w(tag: String , str : String){
            Log.w(TAG, "[   $tag   ] :  $str")
        }
        fun e(tag: String , str : String){
            Log.e(TAG, "[   $tag   ] :  $str")
        }
        fun d(tag: String , str : String){
            Log.d(TAG, "[   $tag   ] :  $str")
        }
    }
}