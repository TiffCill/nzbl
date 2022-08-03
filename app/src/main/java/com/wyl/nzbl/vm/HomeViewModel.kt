package com.wyl.nzbl.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyl.nzbl.model.BaseResponse
import com.wyl.nzbl.model.home.HomeBannerData
import com.wyl.nzbl.model.home.HomeNewTabBean
import com.wyl.nzbl.model.home.HomeTabData
import com.wyl.nzbl.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var bannerResponse = MutableLiveData<BaseResponse<List<HomeBannerData>>>()
    var tabsResponse = MutableLiveData<BaseResponse<List<HomeTabData>>>()
    var newTabsResponse = MutableLiveData<BaseResponse<HomeNewTabBean>>()

    var repository = HomeRepository()

    val TAG = HomeViewModel::class.java.simpleName
    fun getBanner() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bannerResponse.postValue(repository.getBanner())
            } catch (e: Exception) {
                Log.e(TAG, "getBanner: ${e.toString()}")
            }
        }
    }
    fun getTabs(){
        viewModelScope.launch (Dispatchers.IO){
            try {
                tabsResponse.postValue(repository.getHomeTabs())
            }catch (e:java.lang.Exception){
                Log.e(TAG, "getTabs: ${e.toString()}")
            }
        }
    }
    fun getNewTabs(){
        viewModelScope.launch (Dispatchers.IO ){
            try {
                newTabsResponse.postValue(repository.getHomeNewTabs())
            }catch (e:java.lang.Exception){
                Log.e(TAG, "getNewTabs: ${e.toString()}")
            }
        }
    }
}