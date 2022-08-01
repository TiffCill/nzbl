package com.wyl.nzbl.ui.adapter

import android.content.Context
import android.util.SparseArray
import androidx.databinding.ViewDataBinding
import com.wyl.nzbl.base.BaseAdapter
import com.wyl.nzbl.model.home.SearchHotKeyData
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.BR

class HotKeyAdapter(
    context: Context,
    data: ArrayList<SearchHotKeyData>,
    layouts: SparseArray<Int>,
    itemClick: BaseItemClick<SearchHotKeyData>
) : BaseAdapter<SearchHotKeyData>(context,data,layouts,itemClick) {

    override fun layoutId(position: Int): Int {
        return R.layout.item_history
    }

    override fun bindData(dataBinding: ViewDataBinding, data: SearchHotKeyData, layout: Int){
        dataBinding.setVariable(BR.clickItem,itemClick)
        dataBinding.setVariable(BR.itemData,data)
    }
}