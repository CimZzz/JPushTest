package com.example.yumi.jpushtest.environment.module

import android.content.Context
import cn.jpush.im.android.api.JMessageClient
import com.example.yumi.jpushtest.base.IModule

/**
 * Created by CimZzz(王彦雄) on 2017/12/1.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class IMModule(context: Context) : IModule(context) {
    lateinit var curUserName : String

    init {
        JMessageClient.init(context)
    }
}