package com.example.yumi.jpushtest.environment.module

import android.content.Context
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.LoginStateChangeEvent
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.event.OfflineMessageEvent
import com.example.yumi.jpushtest.base.IModule
import com.example.yumi.jpushtest.utils.logV
import io.realm.Realm

/**
 * Created by CimZzz(王彦雄) on 2017/12/1.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class IMModule(context: Context) : IModule(context) {
    lateinit var curUserName : String
    var isRegister = false
    var isLogin = false

    init {
        JMessageClient.init(context)
        JMessageClient.registerEventReceiver(this)
    }


    fun destroy() {
        JMessageClient.unRegisterEventReceiver(this)
    }

    fun onEventMainThread(event: MessageEvent) {

    }

    fun onEventMainThread(event: LoginStateChangeEvent) {

    }

    fun onEventMainThread(event: OfflineMessageEvent) {

    }
}