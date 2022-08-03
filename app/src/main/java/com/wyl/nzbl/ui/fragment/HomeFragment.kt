package com.wyl.nzbl.ui.fragment


import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.jiguang.ads.nativ.api.JNativeAd
import cn.jiguang.ads.nativ.api.JNativeAdApi
import cn.jiguang.ads.nativ.api.JNativeAdSlot
import cn.jiguang.ads.nativ.callback.OnNativeAdEventListener
import cn.jiguang.ads.nativ.callback.OnNativeAdLoadListener
import cn.jiguang.union.ads.base.api.JAdError
import cn.jiguang.union.ads.base.api.JAdSlot
import cn.jpush.android.api.CustomPushNotificationBuilder


import com.bumptech.glide.Glide
import com.wyl.nzbl.base.BaseFragment
import com.wyl.nzbl.vm.HomeViewModel
import com.wyl.nzbl.R
import com.wyl.nzbl.databinding.FragmentHomeBinding
import com.wyl.nzbl.model.home.HomeTabData
import com.wyl.nzbl.ui.activity.SearchActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.wyl.nzbl.BR
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.base.BaseClickItem
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.model.home.HomeBannerData
import com.wyl.nzbl.model.home.HomeNewTabList
import com.wyl.nzbl.ui.activity.DetailsActivity
import com.wyl.nzbl.ui.adapter.HomeNewTabAdapter
import com.wyl.nzbl.util.AdUtil
import com.xiaomi.push.it
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.listener.OnBannerListener


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    R.layout.fragment_home,
    HomeViewModel::class.java
),
    View.OnClickListener {
    private var beans = ArrayList<HomeNewTabList>()
    private val fragments = ArrayList<Fragment>()
    private var homeNewTabAdapter : HomeNewTabAdapter? = null

    override fun initView() {
        mDataBinding.tvSearch.setOnClickListener(this)
        var sparseArray = SparseArray<Int>()
        sparseArray.put(R.layout.item_home_new_tab,BR.homeNewTabData)
        homeNewTabAdapter = HomeNewTabAdapter(MyApp.getContext(),beans,sparseArray,OnClickHomeNewTabItem())
        mDataBinding.rvNewTab.adapter = homeNewTabAdapter
        mDataBinding.rvNewTab.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
    }


    override fun initData() {
        mViewModel.getTabs()
        mViewModel.getBanner()
        mViewModel.getNewTabs()

        val adUtilInterface = AdUtil.getAdUtilInterface(MyApp.getContext())
        adUtilInterface.renderAd(
            adUtilInterface.loadNativeAd(
                MyApp.context!!,
                adUtilInterface.buildSlot(
                    1,
                    JNativeAdSlot.STYLE_BANNER,
                    JNativeAdSlot.LOADER_SDK,
                    JNativeAdSlot.RENDER_SDK,
                    "Homepage_Banner"
                )
            )
        )

    }


    override fun initVariable() {


    }

    override fun initVM() {
        //Banner
        mViewModel.bannerResponse.observe(this) {
            val data = it.data
            mDataBinding.myBanner.adapter = object : BannerImageAdapter<HomeBannerData>(data) {
                override fun onBindView(
                    holder: BannerImageHolder?,
                    data: HomeBannerData?,
                    position: Int,
                    size: Int
                ) {
                    Glide.with(holder!!.itemView).load(data!!.imagePath).into(holder.imageView)
                }
            }
            mDataBinding.myBanner.setOnBannerListener { _, position ->
                startActivity(
                    Intent(context, DetailsActivity::class.java).putExtra(
                        "url",
                        data[position].url
                    )
                )
            }
        }
        //原tab
        mViewModel.tabsResponse.observe(this) {
            for (item: HomeTabData in it.data) {

            }
        }

        //新tab
        mViewModel.newTabsResponse.observe(this){
            val datas = it.data.datas
            if (datas == null||datas.isEmpty()) return@observe
            beans.addAll(datas)
            homeNewTabAdapter?.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            mDataBinding.tvSearch.id -> {
                startActivity(Intent(context, SearchActivity::class.java))
            }
        }

    }

    inner class OnClickHomeNewTabItem() :BaseItemClick<HomeNewTabList>{
        override fun itemClick(data: HomeNewTabList) {
            Toast.makeText(MyApp.context, "${data.title}", Toast.LENGTH_SHORT).show()
        }
    }
}
