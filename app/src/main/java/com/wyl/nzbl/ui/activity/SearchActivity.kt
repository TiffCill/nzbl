package com.wyl.nzbl.ui.activity

import android.content.Intent
import android.util.SparseArray
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wyl.nzbl.BR
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.base.BaseActivity
import com.wyl.nzbl.vm.SearchViewModel
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.databinding.ActivitySearchBinding
import com.wyl.nzbl.model.home.SearchListData
import com.wyl.nzbl.model.home.SearchHotKeyData
import com.wyl.nzbl.ui.adapter.SearchAdapter
import com.wyl.nzbl.ui.adapter.HotKeyAdapter
import com.wyl.nzbl.view.Logger

class SearchActivity : BaseActivity<SearchViewModel, ActivitySearchBinding>(
    R.layout.activity_search,
    SearchViewModel::class.java
) {
    var hotKeys = ArrayList<SearchHotKeyData>()
    var searchData = ArrayList<SearchListData>()
    private lateinit var hotKeyAdapter: HotKeyAdapter
    private lateinit var searchAdapter: SearchAdapter
    override fun initView() {
        mDataBinding.loadingView.visibility = View.GONE
        mDataBinding.btnSearch.setOnClickListener {
            if (mDataBinding.editSearch.text.toString().isEmpty()) {
                Toast.makeText(MyApp.context, "输入内容为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mViewModel.toSearch(0, mDataBinding.editSearch.text.toString())
            showEnableLoading()
        }

        var sparseArray = SparseArray<Int>()
        sparseArray.put(R.layout.item_history, BR.itemData)

        hotKeyAdapter = HotKeyAdapter(
            this,
            hotKeys,
            sparseArray,
            ItemClick()
        )
//        sparseArray.clear() //clear必須放在此處
        var sparseArray1 = SparseArray<Int>()
        sparseArray1.put(R.layout.item_search, BR.searchItemData)

        searchAdapter = SearchAdapter(
            this,
            searchData,
            sparseArray1,
            SearchItemClick()
        )

        mDataBinding.rvHistory.adapter = hotKeyAdapter
        mDataBinding.rvHistory.layoutManager =
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL)

        mDataBinding.rvSearch.adapter = searchAdapter
        mDataBinding.rvSearch.layoutManager = LinearLayoutManager(this)
    }


    override fun initVM() {
        mViewModel.hotKeyData.observe(this) {
            hotKeys.clear()
            val data = it.data
            if (data.isEmpty() || data == null)return@observe
            hotKeys.addAll(data)
            hotKeyAdapter.notifyDataSetChanged()
            showEnableLoading()
        }
        mViewModel.searchData.observe(this) {
            this.searchData.clear()
            val data = it.data ?: return@observe
            this.searchData.addAll(data.datas)
            searchAdapter.notifyDataSetChanged()
            showEnableLoading()
        }
    }

    override fun initData() {
        mViewModel.getHotKey()
        showEnableLoading()
    }

    private fun showEnableLoading() {
        mDataBinding.loadingView.visibility =
            if (mDataBinding.loadingView.isVisible) View.GONE else View.VISIBLE

        if (!mDataBinding.loadingView.isFocusable) {
            mDataBinding.loadingView.isFocusable = true
            mDataBinding.loadingView.isFocusableInTouchMode = true
            mDataBinding.loadingView.requestFocus()
        } else {
            mDataBinding.loadingView.clearFocus()
        }

    }

    override fun initVariable() {

    }

    /**
     * 彈出軟鍵盤
     */
    private fun setFocusable() {
        mDataBinding.editSearch.isFocusable = true
        mDataBinding.editSearch.isFocusableInTouchMode = true
        mDataBinding.editSearch.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }


    inner class ItemClick : BaseItemClick<SearchHotKeyData> {
        override fun itemClick(data: SearchHotKeyData) {
            val name = data.name
            Logger.e("clickItem", "$name")
            Toast.makeText(MyApp.context, "$name", Toast.LENGTH_SHORT).show()
            mViewModel.toSearch(0, name!!)
            showEnableLoading()
        }
    }

    inner class SearchItemClick : BaseItemClick<SearchListData> {
        override fun itemClick(searchListData: SearchListData) {
            Logger.e("clickItem", searchListData.title)
            Toast.makeText(MyApp.context, "${searchListData.title}", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(MyApp.context, DetailsActivity::class.java).putExtra(
                    "url",
                    searchListData.link
                )
            )
        }
    }

}