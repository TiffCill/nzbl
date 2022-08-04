package com.wyl.nzbl.ui.adapter

import android.content.Context
import android.util.SparseArray
import androidx.databinding.ViewDataBinding
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.model.mine.DetailsOptionsData

class DetailsOptionsAdapter(
    context: Context,
    layouts: SparseArray<Int>,
    data: List<DetailsOptionsData>,
    click: BaseItemClick<DetailsOptionsData>
) : com.wyl.nzbl.base.BaseAdapter<DetailsOptionsData>(context, data, layouts, click){
    override fun layoutId(position: Int): Int {
        return R.layout.item_detail_options
    }

    override fun bindData(dataBinding: ViewDataBinding, data: DetailsOptionsData, layout: Int) {

    }
}