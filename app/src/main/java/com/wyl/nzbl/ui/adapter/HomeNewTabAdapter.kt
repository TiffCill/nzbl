package com.wyl.nzbl.ui.adapter

import android.content.Context
import android.util.SparseArray
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.wyl.nzbl.R
import com.wyl.nzbl.BR
import com.wyl.nzbl.base.BaseAdapter
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.model.home.HomeNewTabList


class HomeNewTabAdapter(
    context: Context,
    datas: List<HomeNewTabList>,
    layouts: SparseArray<Int>,
    click: BaseItemClick<HomeNewTabList>
) : BaseAdapter<HomeNewTabList>(context, datas, layouts, click) {
    override fun layoutId(position: Int): Int {
        return R.layout.item_home_new_tab
    }

    override fun bindData(dataBinding: ViewDataBinding, data: HomeNewTabList, layout: Int) {
        dataBinding.setVariable(BR.homeNewTabData, data)
        val ivEnvelope = dataBinding.root.findViewById<ImageView>(R.id.iv_envelope)
        Glide.with(context).load(data.envelopePic).into(ivEnvelope)
    }
}