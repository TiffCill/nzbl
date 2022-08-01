package com.wyl.nzbl.ui.fragment

import com.wyl.nzbl.base.BaseFragment
import com.wyl.nzbl.databinding.FragmentDetailsBinding
import com.wyl.nzbl.vm.DetailsViewModel
import com.wyl.nzbl.R
import com.wyl.nzbl.model.home.HomeTabData

class DetailsFragment(private val bean: HomeTabData) : BaseFragment<DetailsViewModel, FragmentDetailsBinding>(
    R.layout.fragment_details,DetailsViewModel::class.java){

    companion object{
        val instance : DetailsFragment by lazy { DetailsFragment(HomeTabData(arrayListOf(),1,1,"sd",2,4,false,1)) }
    }
    override fun initData() {
        mDataBinding.tvDetails.text = bean.name
    }

    override fun initVM() {

    }

    override fun initVariable() {

    }

    override fun initView() {

    }

}