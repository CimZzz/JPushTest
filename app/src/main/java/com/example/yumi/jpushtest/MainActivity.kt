package com.example.yumi.jpushtest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.enums.MessageStatus
import cn.jpush.im.android.api.event.*
import cn.jpush.im.android.api.model.Message
import cn.jpush.im.android.api.options.MessageSendingOptions
import cn.jpush.im.api.BasicCallback
import kotlinx.android.synthetic.main.activity_main.*
import cn.jpush.im.android.tasks.GetEventNotificationTaskMng.EventEntity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val dateFmt : DateFormat = SimpleDateFormat("yyyy年MM月dd日-hh:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JMessageClient.registerEventReceiver(this)

        register.setOnClickListener {
            JMessageClient.register(userName.editableText.toString(), userPwd.editableText.toString(), object : BasicCallback() {
                override fun gotResult(p0: Int, p1: String?) {
                    if (p0 == 0) {
                        log(login, "注册")
                    } else {
                        log(login, "错误码：$p0,$p1")
                    }
                }
            })
        }

        login.setOnClickListener {
            JMessageClient.login(userName.editableText.toString(), userPwd.editableText.toString(), object : BasicCallback() {
                override fun gotResult(p0: Int, p1: String?) {
                    if (p0 == 0) {
                        log(login, "登录成功")
                        register.isEnabled = false
                        login.isEnabled = false
                        userName.isEnabled = false
                        userPwd.isEnabled = false
                        logout.isEnabled = true
                        sendMsg.isEnabled = true
                        message.isEnabled = true
                        directUserName.isEnabled = true
                    } else {
                        log(login, "错误码：$p0,$p1")
                    }
                }
            })
        }

        logout.setOnClickListener {
            JMessageClient.logout()
            register.isEnabled = true
            login.isEnabled = true
            userName.isEnabled = true
            userPwd.isEnabled = true
            logout.isEnabled = false
            sendMsg.isEnabled = false
            message.isEnabled = false
            directUserName.isEnabled = false
        }

        sendMsg.setOnClickListener {
            val directUserName = directUserName.editableText.toString()
            val msgContent = message.editableText.toString()
            val msg = JMessageClient.createSingleTextMessage(directUserName,msgContent)
            if(msg != null) {
                msg.setOnSendCompleteCallback(object : BasicCallback() {
                    override fun gotResult(p0: Int, p1: String?) {
                        if (p0 == 0) {
                            chatContent.append("我 发送给 $directUserName:$msgContent\n")
                        } else log(login, "错误码：$p0,$p1")
                    }
                })
                JMessageClient.sendMessage(msg)
            }
        }

    }
    fun onEventMainThread(event: MessageEvent) {
        val content = (event.message.content as TextContent).text
        val time = event.message.createTime
        chatContent.append("[${dateFmt.format(Date(time))}]${event.message.fromUser.userName}发送给 我:$content\n")
        log(null,event.javaClass.simpleName)
    }
    fun onEventMainThread(event: OfflineMessageEvent) {
        event.offlineMessageList.forEach {
            val content = (it.content as TextContent).text
            val time = it.createTime
            chatContent.append("[${dateFmt.format(Date(time))}]${it.fromUser.userName}发送给 我:$content\n")

        }
        log(null,event.javaClass.simpleName)
    }

    fun onEventMainThread(event: ConversationRefreshEvent) {
        log(null,event.javaClass.simpleName)
    }

    fun onEventMainThread(event: MyInfoUpdatedEvent) {
        log(null,event.javaClass.simpleName)
    }

    fun onEventMainThread(event: NotificationClickEvent) {
        log(null,event.javaClass.simpleName)
    }

    fun onEventMainThread(event: LoginStateChangeEvent) {
        log(null,event.javaClass.simpleName)
    }

    fun onEventMainThread(event: CommandNotificationEvent) {
        log(null,event.javaClass.simpleName)
    }

    fun addMessage(tag: String,user : String?,content : String) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        log(null,"reqCode : $requestCode,resCode : $resultCode")
        super.onActivityResult(requestCode, resultCode, data)
    }
}
