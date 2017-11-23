package com.example.yumi.jpushtest.environment

import android.app.Application
import cn.jpush.im.android.api.JMessageClient
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JMessageClient.init(this)

//        val imageLoaderConfig = ImageLoaderConfiguration.B

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
    }
}