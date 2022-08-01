package com.wyl.nzbl.ui.adapter

import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseAdapter
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.BR
import com.wyl.nzbl.model.home.SearchListData

class SearchAdapter(
    context: Context,
    data: List<SearchListData>,
    layouts: SparseArray<Int>,
    itemClick: BaseItemClick<SearchListData>
) : BaseAdapter<SearchListData>(context, data, layouts, itemClick) {
    override fun layoutId(position: Int): Int {
        return R.layout.item_search
    }

    override fun bindData(dataBinding: ViewDataBinding, searchListData: SearchListData, layout: Int) {
        var str = StringBuilder(searchListData.title)
        if (str.contains("<") && str.contains(">")) {
            Log.e("TAG", "bindData: ${str.indexOf("<")} ${str.lastIndexOf(">")}")
            str.deleteRange(str.indexOf("<"), str.lastIndexOf(">")+1)
        }

        val tvItem = dataBinding.root.findViewById<TextView>(R.id.tv_item)
        tvItem.text = str
        dataBinding.setVariable(BR.searchItemClick, itemClick)
        dataBinding.setVariable(BR.searchItemData, searchListData)
    }
}