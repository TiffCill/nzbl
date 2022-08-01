package com.wyl.nzbl.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<VM:ViewModel,DB:ViewDataBinding>(var layoutId:Int, var vmClass: Class<VM>) : Fragment(){
    protected lateinit var mDataBinding : DB
    protected lateinit var mViewModel: VM
    protected lateinit var mContext:Context
    protected lateinit var mActivity:Activity
    private var isPause = false
    private var isLoaded = false
    private var isHidden: Boolean? = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = this.context!!
        mActivity = this.activity!!
        mDataBinding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        mViewModel = ViewModelProvider(this).get(vmClass)

        return mDataBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lazyLoad()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        val TAG = BaseFragment::class.java.simpleName
        isHidden=hidden
        lazyLoad()
    }
    private fun lazyLoad(){
        if (!isLoaded&& isHidden == false){
            //懒加载。。。
            initView()
            initData()
            initVM()
            initVariable()
            isLoaded=true
        }
    }
    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onDestroy() {
        super.onDestroy()
        isLoaded = false
    }
    protected abstract fun initData()

    protected abstract fun initVM()

    protected abstract fun initVariable()

    protected abstract fun initView()

}