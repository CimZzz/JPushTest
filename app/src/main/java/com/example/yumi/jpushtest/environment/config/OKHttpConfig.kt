package com.example.yumi.jpushtest.environment.config

import okhttp3.OkHttpClient
import android.content.Context
import com.example.yumi.jpushtest.utils.CookiesManager




/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
object OKHttpConfig {



    fun init(context:Context) : OkHttpClient =
            OkHttpClient.Builder()
                    .cookieJar(CookiesManager(context))
                    .build()

}