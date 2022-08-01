package com.wyl.nzbl.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyl.nzbl.model.BaseResponse
import com.wyl.nzbl.model.login.UserData
import com.wyl.nzbl.repository.AdminRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class AdminViewModel : ViewModel() {
     val loginResponse = MutableLiveData<BaseResponse<UserData>>()
     val registerResponse = MutableLiveData<BaseResponse<UserData>>()

    private val repository = AdminRepository()

    private val TAG = AdminViewModel::class.java.simpleName
    fun toLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loginResponse.postValue(repository.toLogin(username, password))
            }catch (e:Exception){
                Log.e(TAG, "toLogin: ${e.toString()}")
            }
        }
    }

    fun toRegister(username: String,password: String,repassword:String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                registerResponse.postValue(repository.toRegister(username, password, repassword))
            }catch (e:Exception){
                Log.e(TAG, "toRegister: ${e.toString()}" )
            }
        }
    }
}