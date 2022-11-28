package com.wyl.nzbl

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import cn.jpush.android.service.PluginXiaomiPlatformsReceiver
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import com.google.android.material.tabs.TabLayout
import com.wyl.nzbl.base.BaseActivity
import com.wyl.nzbl.databinding.ActivityMainBinding
import com.wyl.nzbl.ui.fragment.CommunityFragment
import com.wyl.nzbl.ui.fragment.HomeFragment
import com.wyl.nzbl.ui.fragment.MineFragment
import com.wyl.nzbl.ui.fragment.VideoFragment
import com.wyl.nzbl.util.Constant
import com.wyl.nzbl.vm.MainViewModel
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(
    R.layout.activity_main,
    MainViewModel::class.java
) {

    val TAG = "MainActivity"
    private val fragmentManager1: FragmentManager
        get() {
            return supportFragmentManager
        }


    override fun initView() {
//        setFullScreen()
        Log.e(TAG, "initView: $intent")
        var fragmentList = ArrayList<Fragment>()
        fragmentList.add(HomeFragment())
        fragmentList.add(CommunityFragment())
        fragmentList.add(VideoFragment())
        fragmentList.add(MineFragment())

        fragmentManager1.beginTransaction()
            .add(R.id.fragment_layout, fragmentList[0])
            .commit()

        mDataBinding.myTab.addTab(
            mDataBinding.myTab.newTab().setCustomView(getTabView(R.drawable.selector_home, "首页"))
        )
        mDataBinding.myTab.addTab(
            mDataBinding.myTab.newTab().setCustomView(getTabView(R.drawable.selector_setting, "设置"))
        )
        mDataBinding.myTab.addTab(
            mDataBinding.myTab.newTab().setCustomView(getTabView(R.drawable.selector_video, "视频"))
        )
        mDataBinding.myTab.addTab(
            mDataBinding.myTab.newTab().setCustomView(getTabView(R.drawable.selector_mine, "我的"))
        )

        mDataBinding.myTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val begin = fragmentManager1.beginTransaction()
                if (fragmentList[0].isVisible)
                    begin.hide(fragmentList[0])
                if (fragmentList[1].isVisible)
                    begin.hide(fragmentList[1])
                if (fragmentList[2].isVisible)
                    begin.hide(fragmentList[2])
                if (fragmentList[3].isVisible)
                    begin.hide(fragmentList[3])
                when (tab?.position) {
                    0 -> {
                        showFragment(begin, fragmentList[0])
                    }
                    1 -> {
                        showFragment(begin, fragmentList[1])
                    }
                    2 -> {
                        showFragment(begin, fragmentList[2])
                    }
                    3 -> {
                        showFragment(begin, fragmentList[3])
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    /**
     * 适配全面屏
     */
    private fun setFullScreen() {
        val window = window
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        Objects.requireNonNull(supportActionBar)?.hide()
    }

    override fun initVM() {

    }

    override fun initData() {
        val myInfo = JMessageClient.getMyInfo() ?: return
        Constant.nikeName = myInfo.nickname
        Constant.userId = myInfo.userID.toString()
        myInfo.getAvatarBitmap(object : GetAvatarBitmapCallback() {
            override fun gotResult(
                responseCode: Int,
                responseMessage: String,
                avatarBitmap: Bitmap
            ) {
                Constant.avatar =
                    if (responseCode == 0) avatarBitmap else BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.ic_launcher_round
                    )
            }
        })
        Constant.appkey = myInfo.appKey
        Constant.userName = myInfo.userName
    }

    override fun initVariable() {

    }

    private fun showFragment(begin: FragmentTransaction, fragment: Fragment) {
        if (fragment.isAdded)
            begin.show(fragment)
        else {
            begin.add(R.id.fragment_layout, fragment)
        }
        begin.commit()
    }

    /**
     * 自定义图标字体和文字
     */
    private fun getTabView(@DrawableRes resId: Int, str: String): View {
        var view = LayoutInflater.from(this).inflate(R.layout.tab_item, null)
        var tvItem = view.findViewById<TextView>(R.id.tv_item)
        var imgItem = view.findViewById<ImageView>(R.id.img_item)
        tvItem.text = str
        imgItem.setImageResource(resId)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume:")
    }

    override fun onNewIntent(intent: Intent?) {
        Log.e(TAG, "onNewIntent: $intent")
        super.onNewIntent(intent)
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: ")

    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop: ")
    }

}

