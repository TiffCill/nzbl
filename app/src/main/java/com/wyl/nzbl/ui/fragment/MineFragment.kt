package com.wyl.nzbl.ui.fragment

import android.util.SparseArray
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyl.nzbl.BR
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.base.BaseFragment
import com.wyl.nzbl.vm.MineViewModel
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.databinding.FragmentMineBinding
import com.wyl.nzbl.model.mine.DetailsOptionsData
import com.wyl.nzbl.model.mine.MineOptionsData
import com.wyl.nzbl.ui.adapter.MineOptionsAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>(
    R.layout.fragment_mine,
    MineViewModel::class.java
) {

    var mineOptionsData = ArrayList<MineOptionsData>()
    var detailsOptionsData1 = ArrayList<DetailsOptionsData>()
    var detailsOptionsData2 = ArrayList<DetailsOptionsData>()
    var detailsOptionsData3 = ArrayList<DetailsOptionsData>()
    var mineOptionsAdapter : MineOptionsAdapter? = null
    override fun initData() {
//        val currentTimeMillis = System.currentTimeMillis()
//        var format = SimpleDateFormat("yyyy年mm月dd日")
//        var date = Date(currentTimeMillis)
//        var nextFormat = SimpleDateFormat("yyyy年mm月dd日","2022年02月01日")
//        val formatString = format.format(date)

        detailsOptionsData1.add(DetailsOptionsData("设置", ""))
        detailsOptionsData1.add(DetailsOptionsData("设置", ""))
        detailsOptionsData1.add(DetailsOptionsData("设置", ""))
        mineOptionsData.add(MineOptionsData("选项一",detailsOptionsData1))
//        detailsOptionsData.clear()
        detailsOptionsData2.add(DetailsOptionsData("朋友圈", ""))
        detailsOptionsData2.add(DetailsOptionsData("朋友圈", ""))
        detailsOptionsData2.add(DetailsOptionsData("朋友圈", ""))
        mineOptionsData.add(MineOptionsData("选项二",detailsOptionsData2))
//        detailsOptionsData.clear()
        detailsOptionsData3.add(DetailsOptionsData("收藏", ""))
        detailsOptionsData3.add(DetailsOptionsData("收藏", ""))
        mineOptionsData.add(MineOptionsData("选项三",detailsOptionsData3))

        var layouts = SparseArray<Int>()
        layouts.put(R.layout.item_mine_options,BR.mineOptionsData)
        mineOptionsAdapter = MineOptionsAdapter(MyApp.getContext(),layouts,mineOptionsData,OnClickMineOptions())
        mDataBinding.rvUser.adapter = mineOptionsAdapter
        mDataBinding.rvUser.layoutManager = LinearLayoutManager(context)

    }

    override fun initVM() {
    }

    override fun initVariable() {
    }

    override fun initView() {
    }
    inner class OnClickMineOptions:BaseItemClick<MineOptionsData>{
        override fun itemClick(data: MineOptionsData) {

        }
    }
}