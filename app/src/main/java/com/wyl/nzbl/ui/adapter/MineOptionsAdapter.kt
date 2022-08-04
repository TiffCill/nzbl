package com.wyl.nzbl.ui.adapter

import android.content.Context
import android.util.SparseArray
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wyl.nzbl.BR
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseAdapter
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.model.mine.DetailsOptionsData
import com.wyl.nzbl.model.mine.MineOptionsData
import com.wyl.nzbl.view.Logger

class MineOptionsAdapter(
    context: Context,
    layouts: SparseArray<Int>,
    data: List<MineOptionsData>,
    click: BaseItemClick<MineOptionsData>
) : BaseAdapter<MineOptionsData>(context, data, layouts, click) {

    override fun layoutId(position: Int): Int {
        return R.layout.item_mine_options
    }

    override fun bindData(dataBinding: ViewDataBinding, data: MineOptionsData, layout: Int) {
        val rvDetails = dataBinding.root.findViewById<RecyclerView>(R.id.rv_details)
        rvDetails.layoutManager = LinearLayoutManager(context)
        var layouts = SparseArray<Int>()
        layouts.put(R.layout.item_detail_options,BR.detailOptionsData)
        rvDetails.adapter = DetailsOptionsAdapter(context,layouts,data.datas,OnClickDetailsItem())
        Logger.i("MineOptionsAdapter","${data.name}  \n ${data.datas.toString()}")
    }

    inner class OnClickDetailsItem:BaseItemClick<DetailsOptionsData>{
        override fun itemClick(data: DetailsOptionsData) {
            Toast.makeText(context, "${data.optionName}", Toast.LENGTH_SHORT).show()
        }
    }
}