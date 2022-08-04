package com.wyl.nzbl.ui.adapter

import android.content.Context
import android.util.SparseArray
import androidx.databinding.ViewDataBinding
import cn.jpush.im.android.api.model.UserInfo
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseAdapter
import com.wyl.nzbl.base.BaseItemClick

class FriendAdapter(
    context: Context,
    data: List<UserInfo>,
    layouts: SparseArray<Int>,
    click: BaseItemClick<UserInfo>
) : BaseAdapter<UserInfo>(context, data, layouts, click) {
    override fun layoutId(position: Int): Int {
        return R.layout.item_friend
    }

    override fun bindData(dataBinding: ViewDataBinding, data: UserInfo, layout: Int) {

    }
}