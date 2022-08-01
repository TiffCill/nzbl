package com.wyl.nzbl.ui.fragment

import android.util.Log
import cn.jiguang.junion.player.ylplayer.JGPlayerConfig
import cn.jiguang.junion.player.ylplayer.callback.OnPlayerCallBack
import cn.jiguang.junion.ui.little.JGLittleVideoFragment
import com.wyl.nzbl.base.BaseFragment
import com.wyl.nzbl.R
import com.wyl.nzbl.databinding.FragmentVideoBinding
import com.wyl.nzbl.vm.VideoViewModel
import androidx.fragment.app.Fragment
import cn.jiguang.junion.ui.configs.LittleVideoConfig

class VideoFragment : BaseFragment<VideoViewModel, FragmentVideoBinding>(R.layout.fragment_video,VideoViewModel::class.java) {
    private val jgPlayerConfig: JGPlayerConfig?
        get() {
            return JGPlayerConfig.config()
        }
    var isPause = false
    private var fragment: Fragment? = null
    override fun initData() {
        jgPlayerConfig?.registerPlayerCallBack(object : OnPlayerCallBack{
            val TAG = OnPlayerCallBack::class.java.simpleName
            override fun onStart(p0: String?, p1: String?, p2: String?) {
                Log.d(TAG, "onStart: $p0      $p1      $p2")
            }

            override fun onPause(p0: String?, p1: String?, p2: String?) {
                Log.e(TAG, "onPause: $p0      $p1      $p2")
            }

            override fun onResume(p0: String?, p1: String?, p2: String?) {
                Log.e(TAG, "onResume: $p0      $p1      $p2")
            }

            override fun onComplete(p0: String?, p1: String?, p2: String?) {
                Log.e(TAG, "onComplete: $p0      $p1      $p2")
            }

            override fun onLoopComplete(p0: String?, p1: String?, p2: String?, p3: Int) {
                Log.e(TAG, "onLoopComplete: $p0      $p1      $p2    $p3")
            }

            override fun onStuckStart(p0: String?, p1: String?, p2: String?) {
                Log.e(TAG, "onStuckStart: $p0      $p1      $p2")
            }

            override fun onStuckEnd(p0: String?, p1: String?, p2: String?) {
                Log.e(TAG, "onStuckEnd: $p0      $p1      $p2")
            }

            override fun onError(p0: String?, p1: String?, p2: String?) {
                Log.e(TAG, "onError: $p0      $p1      $p2")
            }

            override fun onStop(p0: String?, p1: String?, p2: String?) {
                Log.e(TAG, "onStop: $p0      $p1      $p2")
            }

        })
        var littlePageConfig = LittleVideoConfig.getInstance()
        littlePageConfig.setVideoLoop(true)
        fragment = JGLittleVideoFragment.newInstance() as Fragment
        fragmentManager?.beginTransaction()?.replace(R.id.layout_video, fragment!!)?.commitAllowingStateLoss()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden && isPause && fragment!!.isAdded){
            fragmentManager?.beginTransaction()?.detach(fragment!!)
        }

        if (!hidden && !isPause && fragment!!.isAdded){
            fragmentManager?.beginTransaction()?.attach(fragment!!)
        }
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }
    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun initVM() {

    }

    override fun initVariable() {

    }

    override fun initView() {

    }
}