package com.wyl.nzbl.ui.adapter

import android.content.Context
import android.util.SparseArray
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import cn.jpush.im.android.api.model.UserInfo
import com.bumptech.glide.Glide
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
        val ivAvatar = dataBinding.root.findViewById<ImageView>(R.id.iv_avatar)
        Glide.with(context)
            .load(if (data.avatar !=  null && data.avatar.isNotEmpty()) data.avatar else R.mipmap.ic_launcher)
            .into(ivAvatar)
    }
}