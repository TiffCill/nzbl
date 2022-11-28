package com.wyl.nzbl.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.blue
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.convertTo
import cn.jiguang.verifysdk.api.*
import cn.jpush.android.api.CustomPushNotificationBuilder
import cn.jpush.android.api.JPushInterface
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.wyl.nzbl.MainActivity
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseActivity
import com.wyl.nzbl.databinding.ActivityAdminLoginBinding
import com.wyl.nzbl.util.Constant
import com.wyl.nzbl.util.LoadingDialog
import com.wyl.nzbl.view.Logger
import com.wyl.nzbl.vm.AdminViewModel
import java.util.*

class AdminLoginActivity : BaseActivity<AdminViewModel, ActivityAdminLoginBinding>(
    R.layout.activity_admin_login,
    AdminViewModel::class.java
),
    View.OnClickListener {
    private val TAG = AdminLoginActivity::javaClass.name
    private var dialog: PopupWindow? = null
    private var inflater: View? = null
    override fun initView() {
        setFullScreen()

        Log.e(
            TAG, "initView: ${JPushInterface.getRegistrationID(this)}   " +
                    "${NotificationManagerCompat.from(this!!).areNotificationsEnabled()}" +
                    "\n"
        )
        inflater = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
        dialog = PopupWindow(inflater, ViewGroup.LayoutParams.WRAP_CONTENT, 300)
        dialog!!.setBackgroundDrawable(ColorDrawable())
        dialog!!.isOutsideTouchable = false

        mDataBinding.btnOther.setOnClickListener(this)
        mDataBinding.btnLogin.setOnClickListener(this)
        mDataBinding.btnRegister.setOnClickListener(this)

        val customPushNotificationBuilder = CustomPushNotificationBuilder(
            applicationContext,
            R.layout.a,
            R.id.icon,
            R.id.title,
            R.id.text,
            R.id.time
        )
        customPushNotificationBuilder.statusBarDrawable = R.drawable.logo
        customPushNotificationBuilder.layoutIconDrawable = R.drawable.launcher_button
        customPushNotificationBuilder.developerArg0 = "developerArg2"
        JPushInterface.setPushNotificationBuilder(2, customPushNotificationBuilder)
    }


    override fun initVM() {
        mViewModel.loginResponse.observe(this, androidx.lifecycle.Observer {
            LoadingDialog.getInterface(this).handleDialog(mDataBinding.btnLogin)
//            LoadingDialog.getInterface(this).handleDialog(mDataBinding.btnLogin)
            if (it.errorCode == 0) {
                gotoMainActivity()
            } else {
                Toast.makeText(this, "${it.errorMsg}    ${it.data}", Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.registerResponse.observe(this, androidx.lifecycle.Observer {
            if (it.errorCode == 0) {
                Log.e(TAG, "registerResponse: 注册成功")
            }
        })
    }

    private fun gotoMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun initData() {

    }

    override fun initVariable() {

    }

    override fun onDestroy() {
        super.onDestroy()
        LoadingDialog.getInterface(this).closeDialog()
    }

    /**
     * 点击事件
     */
    override fun onClick(v: View?) {
        Log.e(TAG, "login: ")
        when (v?.id) {
            //一键登录
            mDataBinding.btnOther.id -> {
                autoLogin()
            }
            //登陆
            mDataBinding.btnLogin.id -> {
                adminLogin()
            }
            mDataBinding.btnRegister.id -> {
                openActivityForResult.launch(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private val openActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                mDataBinding.etAdmin.setText(data?.extras?.getString("admin"))
                mDataBinding.etPassword.setText(data?.extras?.getString("pwd"))
            }
        }

    /**
     * 账号登录
     */
    private fun adminLogin() {
        val admin = mDataBinding.etAdmin.text.toString()
        val password = mDataBinding.etPassword.text.toString()
        JPushInterface.setAlias(MyApp.context, 5000, "ceshi")
        if (admin == null || admin.isEmpty() || password == null || password.isEmpty()) {
            Toast.makeText(this, "账号或密码不得为空", Toast.LENGTH_SHORT).show()
            return
        }
        LoadingDialog.getInterface(this).handleDialog(mDataBinding.btnLogin)
        mViewModel.toLogin(
            admin,
            password
        )

        JMessageClient.login(admin, password, object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if (p0 == 0) {
                    gotoMainActivity()
                    Logger.i("JmessageLogin", "$p0   $p1 登录成功")
                } else {
                    Logger.i("JmessageLogin", "$p0   $p1 失败")
                    Toast.makeText(MyApp.context, "[$p0]>>>  $p1", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /**
     * 一键登录
     */
    private fun autoLogin() {
        Log.e(TAG, "autoLogin: ")
        val verifyEnable: Boolean = JVerificationInterface.checkVerifyEnable(MyApp.getContext())
        val verifyIsInitSuccess: Boolean = JVerificationInterface.isInitSuccess()
        val uiConfig = getUiConfig()
        if (verifyEnable && verifyIsInitSuccess) {
            LoadingDialog.getInterface(this).handleDialog(mDataBinding.btnLogin)
            var loginSettings: LoginSettings = LoginSettings()
            loginSettings.timeout = 10000
            loginSettings.isAutoFinish = true
            loginSettings.authPageEventListener = object : AuthPageEventListener() {
                override fun onEvent(p0: Int, p1: String?) {
                    var str: String? = when (p0) {
                        1 -> "授权页关闭"
                        2 -> "授权页打开"
                        3 -> "运营商协议点击"
                        4 -> "onEvent: 自定义协议1"
                        5 -> "onEvent: 自定义协议2"
                        6 -> "onEvent: 协议栏checkBox选中"
                        7 -> "onEvent: 协议栏checkBox取消选中"
                        8 -> "onEvent: 一键登录按钮点击事件"
                        else -> "有操作,但不知道具体是啥"
                    }
                    Log.e(TAG, "onEvent: $str")
                    Toast.makeText(applicationContext, "$str", Toast.LENGTH_SHORT).show()
                }
            }
            JVerificationInterface.setCustomUIWithConfig(uiConfig, uiConfig)
            JVerificationInterface.loginAuth(
                MyApp.getContext(),
                loginSettings,
                VerifyListener { i, s, s2 ->
                    Log.e(TAG, "autoLogin: $i    \n $s  \n $s2")
                    if (i == 6000) {
                        mViewModel.toLogin("17610980168", "12345678")
                    } else {
                        Toast.makeText(this, "autoLogin  $i  \n $s \n $s2", Toast.LENGTH_SHORT)
                            .show()
                    }
                    LoadingDialog.getInterface(this).handleDialog(mDataBinding.btnLogin)
                }
            )
        } else {
            Toast.makeText(
                MyApp.getContext(),
                "流量$verifyEnable  初始化$verifyIsInitSuccess",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



//    private fun handleLoadingView(){
//        if (dialog!!.isShowing) dialog?.dismiss() else {
//            dialog?.contentView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
//            dialog?.showAtLocation(mDataBinding.btnLogin, Gravity.CENTER, 0, 0)
//        }
//
//        var myWindow = window.attributes
//        myWindow.alpha = if (myWindow.alpha == 0.5f) 1.0f else 0.5f
//        window.attributes = myWindow
//    }

    /**
     * 适配全面屏
     */
    private fun setFullScreen() {
        val window = window
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        Objects.requireNonNull(supportActionBar)?.hide()
    }


    /**
     * 一键登录授权页ui设置
     */
    private fun getUiConfig(): JVerifyUIConfig {
        var toast = Toast.makeText(this, "选英雄啊", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 80)
        var list: Array<PrivacyBean> = arrayOf(
            PrivacyBean("im your dad", null, "《"),
            PrivacyBean("im your mom", null, "《")
        )
        var otherTxt = TextView(this)
        otherTxt.text = "其他登录方式"
        otherTxt.setTextColor(Color.parseColor("#FFCCCC"))
        otherTxt.textSize = 16F
        var layoutParams = RelativeLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layoutParams.setMargins(0, 1500, 0, 0)
        otherTxt.layoutParams = layoutParams
        otherTxt.gravity = Gravity.CENTER
        val returnBtn = ImageView(this)
        returnBtn.setImageResource(R.drawable.back_)
        return JVerifyUIConfig.Builder()
//            .setAuthBGVideoPath("android.resource://" + this.packageName + "/" + R.raw.launch_video,null)
            .setAuthBGImgPath("background")
            .setStatusBarTransparent(true)
            .setStatusBarHidden(false)
            .setStatusBarDarkMode(true)
            .setNavHidden(true)
            .setLogoImgPath("logo")
            .setLogoOffsetY(300)
            .setLogoHeight(100)
            .setLogoWidth(100)
            .setLogBtnOffsetY(480)
            .setNumFieldOffsetY(420)
            .setNumberColor(Color.WHITE)
            .setNumberSize(16)
            .setNumberColor(R.color.pink)
            .setNumberTextBold(true)
            .setLogBtnText("上号！")
            .setLogBtnTextColor(Color.WHITE)
//            .setLogBtnImgPath("launcher_button")
            .setLogBtnImgPath("btn_selector")
            .setPrivacyTextCenterGravity(true)
            .setPrivacyWithBookTitleMark(true)
            .setPrivacyCheckboxInCenter(true)
//            .enableHintToast(true, toast)
            .setPrivacyNameAndUrlBeanList(list.toMutableList())
            .setPrivacyNavColor(R.color.pink)
            .setPrivacyNavTitleTextColor(R.color.blue)
            .setPrivacyNavReturnBtn(returnBtn)
            .setPrivacyCheckboxSize(14)
            .setPrivacyTextSize(12)
            .setPrivacyOffsetY(50)
            .setPrivacyOffsetX(50)
            .setAppPrivacyNavTitle1("这边是自定义协议1内容，im your dad")
            .setAppPrivacyNavTitle2("这边是自定义协议2内容，im your mom")
            .setSloganHidden(true)
            .setNeedStartAnim(false)
            .setAppPrivacyColor(Color.parseColor("#FF9966"), Color.parseColor("#FF6666"))
            .setCheckedImgPath("checked_checkbox")
            .setUncheckedImgPath("uncheck_checkbox")
            .setPrivacyText("登录即同意", "并使用本机号码登录")
            .addBottomView(otherTxt, JVerifyUIClickCallback { context, view ->
                Log.e(TAG, "getUiConfig: dissmiss")
                var intent = Intent(applicationContext, OnOpenClickActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            })
            .build()
    }
}

