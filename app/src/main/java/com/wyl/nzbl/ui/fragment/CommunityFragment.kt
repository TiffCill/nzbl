package com.wyl.nzbl.ui.fragment

import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoListCallback
import cn.jpush.im.android.api.model.UserInfo
import com.wyl.nzbl.BR
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.base.BaseFragment
import com.wyl.nzbl.vm.CommunityViewModel
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.databinding.FragmentCommunityBinding
import com.wyl.nzbl.ui.adapter.FriendAdapter
import com.wyl.nzbl.view.Logger

class CommunityFragment : BaseFragment<CommunityViewModel, FragmentCommunityBinding>(
    R.layout.fragment_community,
    CommunityViewModel::class.java
),
    View.OnClickListener {
    var friendAdapter: FriendAdapter? = null
    var friendsData = ArrayList<UserInfo>()

    override fun initView() {
        mDataBinding.rvFriends.layoutManager = LinearLayoutManager(context)
        var layouts = SparseArray<Int>()
        layouts.put(R.layout.item_friend,BR.friendUserInfo)
        friendAdapter = FriendAdapter(MyApp.getContext(),friendsData,layouts,OnClickFriendItem())
        mDataBinding.rvFriends.adapter =friendAdapter
    }

    override fun initData() {
        mDataBinding.btnAddFriend.setOnClickListener(this)

        ContactManager.getFriendList(object : GetUserInfoListCallback() {
            override fun gotResult(p0: Int, p1: String?, p2: MutableList<UserInfo>?) {
                Logger.i("getFriendList", "$p0    $p1   \n$p2")
                friendsData.addAll(p2!!)
                friendAdapter?.notifyDataSetChanged()
            }
        })
    }

    override fun initVM() {

    }

    override fun initVariable() {

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            mDataBinding.btnAddFriend.id -> {

            }
        }
    }

    inner class OnClickFriendItem : BaseItemClick<UserInfo>{
        override fun itemClick(data: UserInfo) {
            Toast.makeText(context, "${data.nickname}", Toast.LENGTH_SHORT).show()
        }
    }
}