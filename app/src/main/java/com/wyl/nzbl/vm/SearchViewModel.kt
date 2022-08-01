package com.wyl.nzbl.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyl.nzbl.model.BaseResponse
import com.wyl.nzbl.model.home.SearchData
import com.wyl.nzbl.model.home.SearchHotKeyData
import com.wyl.nzbl.repository.HomeRepository
import com.wyl.nzbl.view.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel : ViewModel() {
    var hotKeyData = MutableLiveData<BaseResponse<List<SearchHotKeyData>>>()
    var searchData = MutableLiveData<BaseResponse<SearchData>>()
    private val repository = HomeRepository()
    private val TAG = SearchViewModel::class.java.simpleName

    fun getHotKey() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                hotKeyData.postValue(repository.getHotKey())
            } catch (e: Exception) {
                Log.e(TAG, "getHotKey: ${e.toString()}")
            }
        }
    }

    fun toSearch(page: Int, key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val toSearch = repository.toSearch(page, key)
                Logger.i("","$toSearch")
                searchData.postValue(toSearch!!)
            } catch (e: Exception) {
                Logger.e(TAG, "toSearch:${e.toString()}")
            }
        }
    }
}