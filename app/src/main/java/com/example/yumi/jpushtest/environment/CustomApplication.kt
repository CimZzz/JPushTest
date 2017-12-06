package com.example.yumi.jpushtest.environment

import android.app.Application
import cn.jpush.im.android.api.JMessageClient
import com.example.yumi.jpushtest.environment.module.FileModule
import com.example.yumi.jpushtest.environment.module.HttpModule
import com.example.yumi.jpushtest.environment.module.IMModule
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import io.realm.Realm

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class CustomApplication : Application() {

    lateinit var fileModule : FileModule
    lateinit var httpModule : HttpModule
    lateinit var imModule :IMModule

    override fun onCreate() {
        super.onCreate()
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))

        fileModule = FileModule(this)
        httpModule = HttpModule(this)
        imModule = IMModule(this)
    }
}