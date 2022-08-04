package com.wyl.nzbl.ui.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo
import cn.jpush.im.api.BasicCallback
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.R
import com.wyl.nzbl.view.Logger

class RegisterActivity :  Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etAdmin = findViewById<EditText>(R.id.et_admin)
        val etPwd = findViewById<EditText>(R.id.et_pwd)
        val btnRegister = findViewById<Button>(R.id.btn_register)

        btnRegister.setOnClickListener {
            if (etAdmin.text.toString().isEmpty() || etPwd.text.toString().isEmpty()){
                Toast.makeText(this, "账号或密码为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            JMessageClient.register(etAdmin.text.toString(),etPwd.text.toString(),
                object : BasicCallback() {
                    override fun gotResult(p0: Int, p1: String?) {
                        Logger.i("JMessage register","$p0      $p1")
                        if (p0==0){
                            Toast.makeText(MyApp.getContext(), "注册成功", Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(MyApp.getContext(), "注册失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }
}