package com.example.yumi.jpushtest.environment.module

import android.content.Context
import com.alibaba.fastjson.JSONObject
import com.example.yumi.jpushtest.base.BaseRequest
import com.example.yumi.jpushtest.base.IModule
import com.example.yumi.jpushtest.environment.HTTP
import com.example.yumi.jpushtest.environment.config.OKHttpConfig
import com.example.yumi.jpushtest.utils.logV
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class HttpModule(context: Context) : IModule(context) {
    private var isGetting : Boolean = false
    private var token : String? = null
    private val httpClient = OKHttpConfig.init(context)
    private val requestList = LinkedList<BaseRequest>()

    fun enqueue(baseRequest: BaseRequest) {
        synchronized(this,{
            if(token == null) {
                requestList.add(baseRequest)
                if(!isGetting)
                    requestNewToken()
                return
            }
            else {
                baseRequest.token = token!!
                baseRequest.init(this)
                logV("发送请求:$baseRequest")
                httpClient.newCall(baseRequest.request).enqueue(baseRequest)
            }
        })
    }

    fun tokenInvalid(baseRequest: BaseRequest) {
        synchronized(this,{
            if(token == null) {
                if(isGetting) {
                    requestList.add(baseRequest)
                    return
                } else {
                    requestNewToken()
                }
            }

            if(baseRequest.token == token) {
                token = null
                requestList.add(baseRequest)
                requestNewToken()
            }
            else enqueue(baseRequest)
        })
    }

    private fun requestNewToken() {
        logV("正在申请新的Token")
        isGetting = true
        val req = Request.Builder().url(HTTP.Token.URL).method("GET",null).build()
        httpClient.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                synchronized(this@HttpModule) {
                    logV("token申请失败:网络连接失败")
                    requestList.forEach {
                        it.onNetworkFailed()
                    }
                    requestList.clear()
                    isGetting = false
                }
            }

            override fun onResponse(call: Call?, response: Response) {
                synchronized(this@HttpModule) {
                    token = JSONObject.parseObject(response.body()!!.string()).getJSONObject(HTTP.DATA).getString(HTTP.Token.TOKEN)
                    logV("token申请成功:$token")
                    requestList.forEach {
                        enqueue(it)
                    }
                    requestList.clear()
                    isGetting = false
                }
            }

        })
    }
}