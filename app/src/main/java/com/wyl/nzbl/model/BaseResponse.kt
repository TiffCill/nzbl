package com.wyl.nzbl.model


data class BaseResponse<T>(
    var errorCode : Int,
    var data : T,
    var errorMsg : String)
