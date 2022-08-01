package com.wyl.nzbl.ui.fragment

import com.wyl.nzbl.base.BaseFragment
import com.wyl.nzbl.vm.MineViewModel
import com.wyl.nzbl.R
import com.wyl.nzbl.databinding.FragmentMineBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MineFragment : BaseFragment<MineViewModel,FragmentMineBinding>(R.layout.fragment_mine,MineViewModel::class.java) {
    override fun initData() {
//        val currentTimeMillis = System.currentTimeMillis()
//        var format = SimpleDateFormat("yyyy年mm月dd日")
//        var date = Date(currentTimeMillis)
//        var nextFormat = SimpleDateFormat("yyyy年mm月dd日","2022年02月01日")
//        val formatString = format.format(date)
    }

    override fun initVM() {
    }

    override fun initVariable() {
    }

    override fun initView() {
    }
}