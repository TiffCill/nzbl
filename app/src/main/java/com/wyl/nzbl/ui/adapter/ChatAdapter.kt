package com.wyl.nzbl.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.SparseArray
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.model.Message
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseAdapter
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.util.Constant.Companion.userId
import com.wyl.nzbl.BR
import com.wyl.nzbl.util.Constant
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(
    context: Context,
    data: LinkedList<Message>,
    layouts: SparseArray<Int>,
    itemClick: BaseItemClick<Message>,
) : BaseAdapter<Message>(context, data, layouts, itemClick) {


    override fun layoutId(position: Int): Int {
        if (data[position].fromUser.userID.toString() == userId) {
            return R.layout.item_chat_mine
        }
        return R.layout.item_chat_tartget
    }

    override fun bindData(dataBinding: ViewDataBinding, data: Message, layout: Int) {
        val ivAvatar = dataBinding.root.findViewById<ImageView>(R.id.iv_avatar)
        val tvContent = dataBinding.root.findViewById<TextView>(R.id.tv_content)
        //展示头像
        if (data.fromUser != null && data.fromUser.avatar != null) {
            data.fromUser.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                override fun gotResult(p0: Int, p1: String?, p2: Bitmap?) {
                    if (p0 == 0) {
                        ivAvatar.setImageBitmap(p2)
                    }else{
                        ivAvatar.setImageResource(R.mipmap.ic_launcher)
                    }
                }
            })
        }else{
            ivAvatar.setImageResource(R.mipmap.ic_launcher)
        }
        //展示文本内容
        val content = data.content as TextContent
        tvContent.text = content.text
    }
}