package com.wyl.nzbl.util

import android.content.Context
import android.util.Log
import android.view.View
import cn.jiguang.ads.nativ.api.JNativeAd
import cn.jiguang.ads.nativ.api.JNativeAdApi
import cn.jiguang.ads.nativ.api.JNativeAdSlot
import cn.jiguang.ads.nativ.callback.OnNativeAdEventListener
import cn.jiguang.ads.nativ.callback.OnNativeAdLoadListener
import cn.jiguang.union.ads.base.api.JAdError
import java.util.*

class AdUtil{

    val TAG = AdUtil::class.simpleName

    companion object {
        private var adUtil: AdUtil? = null
        val jNativeAdSlot: JNativeAdSlot.Builder = JNativeAdSlot.Builder()
        fun getAdUtilInterface(context: Context): AdUtil {
            if (adUtil == null) {
                synchronized(context) {
                    if (adUtil == null) {
                        adUtil = AdUtil()
                    }
                }
            }
            return adUtil!!
        }
    }


     fun buildSlot(
        adCount: Int,
        adStyle: Int,
        loader: Int,
        render: Int,
        adPosition: String
    ): JNativeAdSlot {
        jNativeAdSlot.adCount = adCount
        jNativeAdSlot.adStyle = adStyle
        jNativeAdSlot.loader = loader
        jNativeAdSlot.render = render
        jNativeAdSlot.adPosition = adPosition
        return jNativeAdSlot.build()
    }

     fun loadNativeAd(context: Context,jNativeAdSlot: JNativeAdSlot): MutableList<JNativeAd>? {
        var ads: MutableList<JNativeAd>? = null
        if (jNativeAdSlot == null) {
            Log.e(TAG, "loadNativeAd: Sloat为空")
            return ads
        }
        JNativeAdApi.loadNativeAd(context, jNativeAdSlot, object : OnNativeAdLoadListener() {
            override fun onLoaded(p0: MutableList<JNativeAd>?) {
                Log.d(TAG, "onLoaded: ${p0.toString()}")
                ads = p0
            }

            override fun onError(error: JAdError?) {
                Log.e(TAG, "onError: ${error?.message}   ${error?.code}")
            }
        })
        return ads
    }

     fun renderAd(ads : MutableList<JNativeAd>?){
         if (null == ads){
             Log.e(TAG, "renderAd: ADS is empty")
             return
        }
        for ( ad in ads){
            if (ad.isValid)continue
            ad.render()
            ad.setOnNativeAdEventListener(object : OnNativeAdEventListener() {
                override fun onAdExposed(p0: JNativeAd?) {
                    Log.i(TAG, "onAdExposed: ${p0.toString()}")
                }

                override fun onAdClicked(view: View?) {
                    Log.i(TAG, "onAdClicked: ")
                }

                override fun onError(error: JAdError?, nativeAd: JNativeAd?) {
                    Log.e(TAG, "onError: ${error.toString()}     ${nativeAd.toString()}")
                }
            })
        }
    }
}