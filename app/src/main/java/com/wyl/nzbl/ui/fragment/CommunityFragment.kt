package com.wyl.nzbl.ui.fragment

import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoListCallback
import cn.jpush.im.android.api.model.UserInfo
import com.wyl.nzbl.base.BaseFragment
import com.wyl.nzbl.vm.CommunityViewModel
import com.wyl.nzbl.R
import com.wyl.nzbl.databinding.FragmentCommunityBinding
import com.wyl.nzbl.view.Logger

class CommunityFragment : BaseFragment<CommunityViewModel, FragmentCommunityBinding>(R.layout.fragment_community,CommunityViewModel::class.java) {
    override fun initData() {
        ContactManager.getFriendList(object : GetUserInfoListCallback(){
            override fun gotResult(p0: Int, p1: String?, p2: MutableList<UserInfo>?) {
                Logger.i("getFriendList","$p0    $p1   \n$p2")
            }
        })
    }

    override fun initVM() {

    }

    override fun initVariable() {

    }

    override fun initView() {

    }
}