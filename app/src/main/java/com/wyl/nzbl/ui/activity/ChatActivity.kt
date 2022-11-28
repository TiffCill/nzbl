package com.wyl.nzbl.ui.activity

import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.android.api.JPushInterface
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.Message
import com.wyl.nzbl.BR
import com.wyl.nzbl.MyApp
import com.wyl.nzbl.R
import com.wyl.nzbl.base.BaseActivity
import com.wyl.nzbl.base.BaseClickItem
import com.wyl.nzbl.base.BaseItemClick
import com.wyl.nzbl.controller.OnMessageEvent
import com.wyl.nzbl.databinding.ActivityChatBinding
import com.wyl.nzbl.ui.adapter.ChatAdapter
import com.wyl.nzbl.util.Constant
import com.wyl.nzbl.view.Logger
import com.wyl.nzbl.vm.ChatViewModel
import java.util.*

class ChatActivity : BaseActivity<ChatViewModel, ActivityChatBinding>(R.layout.activity_chat,ChatViewModel::class.java),
    View.OnClickListener {
    var targetUserName: String? = null
    var targetAppkey: String? = null
    var conversation: Conversation? = null
    var chatAdapter : ChatAdapter? = null
    var chatList = LinkedList<Message>()
    override fun initView() {
        val extras = intent.extras
        targetUserName = extras?.getString("username")
        targetAppkey = extras?.getString("appkey")
        conversation = Conversation.createSingleConversation(targetUserName, targetAppkey)


        var sparseArray = SparseArray<Int>()
        sparseArray.put(R.layout.item_chat_tartget,BR.chatMessageData)
        sparseArray.put(R.layout.item_chat_mine,BR.chatMessageData)
        chatAdapter = ChatAdapter(MyApp.getContext(), chatList,sparseArray,OnClickChatMessageLlistener())
        mDataBinding.tvTargetName.text = extras?.getString("nike_name")
        mDataBinding.rvChat.layoutManager = LinearLayoutManager(this)
        mDataBinding.rvChat.adapter = chatAdapter

        mDataBinding.btnSend.setOnClickListener(this)

        MyApp.addOnMessageEventListener(object : OnMessageEvent {
            override fun getNewMessage(event: MessageEvent) {
                val message = event.message
                Logger.d("ChatActivity  getNewMessage",event.message.toJson())
                chatList.add(message)
                chatAdapter?.notifyDataSetChanged()
                mDataBinding.rvChat.scrollToPosition(chatAdapter?.itemCount!!)
            }
        })
    }

    override fun initVM() {

    }

    override fun initData() {

    }

    override fun initVariable() {

    }

    inner class OnClickChatMessageLlistener : BaseItemClick<Message>{
        override fun itemClick(data: Message) {
            val TAG = "OnClickChatMessageLlistener"
            Log.e(TAG, "itemClick: ${data.content.toJson()}")
            Toast.makeText(this@ChatActivity, "${data.content.toJson()}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_send ->{
                if (mDataBinding.etInput.text!!.isEmpty()){
                    Toast.makeText(this@ChatActivity, "发送消息不能为空", Toast.LENGTH_SHORT).show()
                    return
                }
                val message = conversation?.createSendTextMessage(mDataBinding.etInput.text.toString(),Constant.nikeName)
                JMessageClient.sendMessage(message)
                chatList.add(message!!)
                chatAdapter?.notifyDataSetChanged()
                mDataBinding.rvChat.scrollToPosition(chatAdapter?.itemCount!!)

                mDataBinding.etInput.setText("")
            }
        }
    }
}