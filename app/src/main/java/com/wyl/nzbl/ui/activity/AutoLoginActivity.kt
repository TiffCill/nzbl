package com.wyl.nzbl.ui.activity

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.Gravity
import android.view.View
import android.widget.*
import cn.jiguang.verifysdk.api.*
import cn.jpush.android.api.JPushInterface
import com.wyl.nzbl.MainActivity
import com.wyl.nzbl.MyApp.Companion.context
import com.wyl.nzbl.R
import com.wyl.nzbl.view.MyLoadingView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AutoLoginActivity : AppCompatActivity() {
    val TAG = "AutoLoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_auto_login)
        initView()
        Log.e(TAG, "onCreate: ${JPushInterface.getRegistrationID(this)}")
        initChannel()
//        setBadge()
    }

    private fun setBadge() {
        var extra = Bundle()
        extra.putString("package", "com.example.Jpushwyl")
        extra.putString("class", "com.wyl.nzbl.ui.activity.OnOpenClickActivity")
        extra.putInt("badgenumber", 15)
        context?.contentResolver?.call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"),"change_badge", null, extra)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        window.attributes.alpha = 0.5f
    }

    private var myLoadingView : MyLoadingView? = null

    private fun initView() {
         myLoadingView = findViewById<MyLoadingView>(R.id.my_loadingView)
    }


    private fun getUiConfig(): JVerifyUIConfig {
        var toast = Toast.makeText(this,"选英雄啊",Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER,0,80)
        var list : Array<PrivacyBean> = arrayOf(PrivacyBean("im your dad",null,"《"),
            PrivacyBean("im your mom",null,"《")
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
        layoutParams.setMargins(0,1500,0,0)
        otherTxt.layoutParams  = layoutParams
        otherTxt.gravity = Gravity.BOTTOM
        val returnBtn  = ImageView(this)
        returnBtn.setImageResource(R.drawable.back_)
        return JVerifyUIConfig.Builder()
//            .setAuthBGVideoPath("android.resource://" + this.packageName + "/" + R.raw.launch_video,null)
            .setAuthBGGifPath("beauty")
//            .setAuthBGImgPath("background")
            .setStatusBarHidden(true)
            .setNavHidden(false)
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

            .setLogBtnImgPath("login_seleter")
            .setPrivacyTextCenterGravity(true)
            .setPrivacyWithBookTitleMark(true)
            .setPrivacyCheckboxInCenter(true)
            .enableHintToast(true,toast)
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
            .setAppPrivacyColor(Color.parseColor("#FF9966"),Color.parseColor("#FF6666"))
            .setCheckedImgPath("checked_checkbox")
            .setUncheckedImgPath("uncheck_checkbox")
            .setPrivacyText("登录即同意","并使用本机号码登录")
            .addBottomView(otherTxt, JVerifyUIClickCallback { context, view ->
                Log.e(TAG, "getUiConfig: dissmiss")
//                JVerificationInterface.dismissLoginAuthActivity()
                var intent = Intent(applicationContext,OnOpenClickActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            })
            .build()
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun onLoginAuthEvent( event : String){
        Log.e(TAG, "onLoginAuthEvent: ${JPushInterface.getRegistrationID(this)}")
        val checkVerifyEnable = JVerificationInterface.checkVerifyEnable(this)
        Toast.makeText(this,
            if(checkVerifyEnable) "支持" else "不支持", Toast.LENGTH_SHORT).show()
        Log.e(TAG, "onLoginAuthEvent: $event   $checkVerifyEnable")
        if (event != "可以启动"){
            return
        }
        myLoadingView?.visibility = View.GONE
        val uiConfig = getUiConfig()
        if (JVerificationInterface.isInitSuccess()){
            val loginSettings = LoginSettings()
            loginSettings.timeout = 10000
            loginSettings.isAutoFinish = true
            loginSettings.authPageEventListener = object : AuthPageEventListener() {
                override fun onEvent(p0: Int, p1: String?) {
                    Log.d(TAG, when(p0){
                        1-> "授权页关闭"
                        2-> "授权页打开"
                        3-> "运营商协议点击"
                        4 -> "onEvent: 自定义协议1"
                        5 -> "onEvent: 自定义协议2"
                        6 -> "onEvent: 协议栏checkBox选中"
                        7 -> "onEvent: 协议栏checkBox取消选中"
                        8 -> "onEvent: 一键登录按钮点击事件"
                        else -> "有操作,但不知道具体是啥"
                    })
                }
            }
            JVerificationInterface.setCustomUIWithConfig(uiConfig,uiConfig)
            JVerificationInterface.loginAuth(this, loginSettings,VerifyListener { i, s, s2 ->
                Log.e(TAG, "[code]$i      $s    $s2")
                if (i == 6000){
                    startActivity(Intent(this, MainActivity::class.java))
                }
            })
        }else{
            Log.e(TAG, "onCreate: 初始化失败"  )
        }
    }
    private fun initChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var nm : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm != null) {
                var notificationChannelGroup = NotificationChannelGroup("MyGroupId", "自定义通知组")
                nm.createNotificationChannelGroup(notificationChannelGroup)
                var notificationChannel1 =
                    NotificationChannel("MyChannelId1", "自定义通知1", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel1.group = "MyGroupId"
                notificationChannel1.enableLights(true)
                notificationChannel1.enableVibration(true)
                nm.createNotificationChannel(notificationChannel1)

                var notificationChannel2 =
                    NotificationChannel("MyChannelId2", "自定义通知2", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel2.group = "MyGroupId"
                notificationChannel2.enableLights(true)
                notificationChannel2.enableVibration(true)
                nm.createNotificationChannel(notificationChannel2)
                logChannel("前")

//                nm.deleteNotificationChannelGroup("MyGroupId")
                nm.deleteNotificationChannelGroup("JIGUANG_CHANNEL_GROUP")
                nm.deleteNotificationChannel("JPush_2_0")
                nm.deleteNotificationChannel("JPush_3_7")
                nm.deleteNotificationChannel("MyChannelId1")
                logChannel("后")
            }
        }
    }
    fun logChannel(r:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var nm: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (r == "前") {
                for (i: NotificationChannelGroup in nm.notificationChannelGroups) {
                    Log.e(TAG, "前     logChannel: ${i}\n")
                }
                for (i: NotificationChannel in nm.notificationChannels) {
                    Log.e(TAG, "前     logChannel: ${i}\n")
                }
            }else{
                for (i: NotificationChannelGroup in nm.notificationChannelGroups) {
                    Log.i(TAG, "后     logChannel: ${i}\n")
                }
                for (i: NotificationChannel in nm.notificationChannels) {
                    Log.i(TAG, "后     logChannel: ${i}\n")
                }
            }
        }
    }
    fun getNavigationBarHeight(context: Context): Int {

        return 1
    }
}