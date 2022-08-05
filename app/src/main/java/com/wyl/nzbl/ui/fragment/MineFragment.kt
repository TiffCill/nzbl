package com.wyl.nzbl.ui.fragment


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.bumptech.glide.Glide
import com.wyl.nzbl.BR
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseFragment
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.databinding.FragmentMineBinding
import com.wyl.nzbl.model.mine.DetailsOptionsData
import com.wyl.nzbl.model.mine.MineOptionsData
import com.wyl.nzbl.ui.adapter.MineOptionsAdapter
import com.wyl.nzbl.util.Constant
import com.wyl.nzbl.view.Logger
import com.wyl.nzbl.vm.MineViewModel
import java.io.File
import java.util.*

class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>(
    R.layout.fragment_mine,
    MineViewModel::class.java
), View.OnClickListener {

    var mineOptionsData = ArrayList<MineOptionsData>()
    var detailsOptionsData1 = ArrayList<DetailsOptionsData>()
    var detailsOptionsData2 = ArrayList<DetailsOptionsData>()
    var detailsOptionsData3 = ArrayList<DetailsOptionsData>()
    var mineOptionsAdapter: MineOptionsAdapter? = null
    val REQUEST_PERMISSIONS_BACK = 1
    val CROP_IMAGE_BACK = 2
    val PICK_IMAGE_BACK = 3

    override fun initData() {
//        val currentTimeMillis = System.currentTimeMillis()
//        var format = SimpleDateFormat("yyyy年mm月dd日")
//        var date = Date(currentTimeMillis)
//        var nextFormat = SimpleDateFormat("yyyy年mm月dd日","2022年02月01日")
//        val formatString = format.format(date)


        //初始化选项数据
        detailsOptionsData1.add(DetailsOptionsData("设置", ""))
        detailsOptionsData1.add(DetailsOptionsData("设置", ""))
        detailsOptionsData1.add(DetailsOptionsData("设置", ""))
        mineOptionsData.add(MineOptionsData("选项一", detailsOptionsData1))
//        detailsOptionsData.clear()
        detailsOptionsData2.add(DetailsOptionsData("朋友圈", ""))
        detailsOptionsData2.add(DetailsOptionsData("朋友圈", ""))
        detailsOptionsData2.add(DetailsOptionsData("朋友圈", ""))
        mineOptionsData.add(MineOptionsData("选项二", detailsOptionsData2))
//        detailsOptionsData.clear()
        detailsOptionsData3.add(DetailsOptionsData("收藏", ""))
        detailsOptionsData3.add(DetailsOptionsData("收藏", ""))
        mineOptionsData.add(MineOptionsData("选项三", detailsOptionsData3))

        var layouts = SparseArray<Int>()
        layouts.put(R.layout.item_mine_options, BR.mineOptionsData)
        mineOptionsAdapter =
            MineOptionsAdapter(MyApp.getContext(), layouts, mineOptionsData, OnClickMineOptions())
        mDataBinding.rvUser.adapter = mineOptionsAdapter
        mDataBinding.rvUser.layoutManager = LinearLayoutManager(context)

    }

    override fun initVM() {
    }

    override fun initVariable() {
    }

    override fun initView() {
        //加载头像
        Glide.with(MyApp.getContext())
            .load(if (Constant.avatar != null && Constant.avatar!!.isNotEmpty()) Constant.avatar else R.mipmap.ic_launcher)
            .into(mDataBinding.ivAvatar)
        mDataBinding.ivAvatar.setOnClickListener(this)
        mDataBinding.tvName.text = Constant.nikeName
    }

    inner class OnClickMineOptions : BaseItemClick<MineOptionsData> {
        override fun itemClick(data: MineOptionsData) {

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            mDataBinding.ivAvatar.id -> {
                openPhotoAlbum()
            }
        }
    }

    /**
     * 打开相册
     */
    private fun openPhotoAlbum() {
        if (ActivityCompat.checkSelfPermission(
                MyApp.getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ) != PackageManager.PERMISSION_GRANTED
        ) { //判断是否授权
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //判断系统版本
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS_BACK
                )
            }
        } else {
            val intent = Intent(Intent.ACTION_PICK)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_BACK)
        }
    }

    /**
     *  获取动态申请权限结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_BACK && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_BACK)
        }
    }

    /**
     * 跳转剪切图片
     */
    private fun cropPhoto(uri : Uri){
        val file = File(buildUri().path)
        if (file.exists()){
            file.delete()
        }

        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.setDataAndType(uri, "image/*")
        cropIntent.putExtra("crop", "true")
        cropIntent.putExtra("aspectX", 1)
        cropIntent.putExtra("aspectY", 1)
        cropIntent.putExtra("outputX", 200)
        cropIntent.putExtra("outputY", 200)
        cropIntent.putExtra("return-data", false)
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, buildUri())
        startActivityForResult(
            cropIntent,
            CROP_IMAGE_BACK
        )
    }

    /**
     * 构建uri
     */
    private fun buildUri(): Uri{
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()){
            return  Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon()
                .appendPath("avatar_file.jpg")
                .build()
        }
        return Uri.fromFile(requireActivity().cacheDir).buildUpon()
            .appendPath("avatar_file.jpg").build()
    }
    /**
     * 获取本地相册
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CROP_IMAGE_BACK -> {        //剪切图返回
                Logger.i(
                    "filePath==",
                    "${buildUri().path}"
                )
                JMessageClient.updateUserAvatar(File(buildUri().path), object : BasicCallback() {
                    override fun gotResult(p0: Int, p1: String?) {
                        Logger.i("updateUserAvatar", "$p0   $p1")
                        Glide.with(requireActivity()).load(buildUri()).error(R.mipmap.ic_launcher)
                            .into(mDataBinding.ivAvatar)
                        if (p0 == 0) {
                            Toast.makeText(activity, "更新头像成功", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(activity, "失敗,${p1}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            PICK_IMAGE_BACK -> {        //相册图片回调
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val photoUrl = data.data
                    cropPhoto(photoUrl!!)
                }
            }
        }
    }
}

