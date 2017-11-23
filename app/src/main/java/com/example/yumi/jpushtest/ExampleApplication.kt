package com.example.yumi.jpushtest

import android.app.Application
import cn.jpush.im.android.api.JMessageClient

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

//        JMessageClient.setDebugMode(true)
        JMessageClient.init(this)
    }
}