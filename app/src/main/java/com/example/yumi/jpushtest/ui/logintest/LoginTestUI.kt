package com.example.yumi.jpushtest.ui.logintest

import android.os.Bundle
import android.view.View
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.event.OfflineMessageEvent
import cn.jpush.im.api.BasicCallback
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.ui.chat.ChatUI
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.ui_logintest.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class LoginTestUI : BaseUI<IPresenter<*,*>>() {
    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_logintest)
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        val clickListener = View.OnClickListener {
            var userName : String = ""
            var userPwd : String = ""
            var oppositeUserName : String = ""
            when(it.id) {
                R.id.cimzzzLogin->{
                    userName = "CimZzz"
                    userPwd = "123456"
                    oppositeUserName = "TiwZzz"
                }
                R.id.tiwzzzLogin->{
                    userName = "TiwZzz"
                    userPwd = "123456"
                    oppositeUserName = "CimZzz"
                }
            }
            JMessageClient.login(userName,userPwd,object : BasicCallback() {
                override fun gotResult(p0: Int, p1: String?) {
                    if(p0 == 0) {
                        val bundle = Bundle()
                        bundle.putString("UserName",userName)
                        bundle.putString("OppositeUserName",oppositeUserName)
                        JMessageClient.registerEventReceiver(this@LoginTestUI)
                        changeUI(ChatUI::class.java,bundle)
                    } else {
                        sendToast("登陆失败:$p1")
                    }
                }

            })
        }

        cimzzzLogin.setOnClickListener(clickListener)
        tiwzzzLogin.setOnClickListener(clickListener)
    }



    fun onEventMainThread(event: OfflineMessageEvent) {
        event.offlineMessageList.forEach {
            logV("Offline message : LoginUI")
        }
    }

    fun onEventMainThread(event: MessageEvent) {
    }
}