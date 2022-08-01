package com.wyl.nzbl.ui.activity

import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import com.wyl.nzbl.base.BaseActivity
import com.wyl.nzbl.databinding.ActivityFaceRecognitionBinding
import com.wyl.nzbl.vm.FaceRecognitionViewModel
import com.wyl.nzbl.R

class FaceRecognitionActivity :
    BaseActivity<FaceRecognitionViewModel, ActivityFaceRecognitionBinding>(
        R.layout.activity_face_recognition,
        FaceRecognitionViewModel::class.java
    ) {
    private val TAG = FaceRecognitionActivity::class.java.simpleName

    override fun initView() {
        requestPermissions(99)
    }

    override fun initVM() {

    }

    override fun initData() {

    }

    override fun initVariable() {

    }
    private fun requestPermissions(code : Int) {
        try {
            var permissionArr = ArrayList<String>()
            val hasCamrea = checkSelfPermission(Manifest.permission.CAMERA)
            if(hasCamrea != PackageManager.PERMISSION_GRANTED){
                permissionArr.add(Manifest.permission.CAMERA)
            }

            val hasReadExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            if(hasReadExternalStorage != PackageManager.PERMISSION_GRANTED){
                permissionArr.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            val hasWriteExternalStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (hasWriteExternalStorage != PackageManager.PERMISSION_GRANTED){
                permissionArr.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (permissionArr.size > 0){
//                requestPermissions(permissionArr,code)
            }
        }catch (e:Exception){
            Log.e(TAG, "requestPermissions: ${e.toString()}", )
        }
    }
}


