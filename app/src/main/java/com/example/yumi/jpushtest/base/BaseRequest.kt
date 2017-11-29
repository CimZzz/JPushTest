package com.example.yumi.jpushtest.base

import com.alibaba.fastjson.JSONObject
import com.example.yumi.jpushtest.environment.HTTP
import com.example.yumi.jpushtest.environment.module.HttpModule
import com.virtualightning.stateframework.state.StateRecord
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
 abstract class BaseRequest(val stateRecord: StateRecord) : Callback {
    lateinit var token : String
    lateinit var request : Request
    lateinit var httpModule : HttpModule

    abstract fun createRequest() : Request

    fun init(httpModule : HttpModule) {
        this.httpModule = httpModule
        request = createRequest()
    }

    abstract fun onNetworkFailed()
    abstract fun onOccurFailed(code:Int,msg:String)
    abstract fun onSuccess(jsonObject:JSONObject)

    override fun onFailure(call: Call?, e: IOException?) {
        onNetworkFailed()
    }

    override fun onResponse(call: Call?, response: Response) {
        val json = JSONObject.parseObject(response.body()!!.string())
        val code = json.getIntValue(HTTP.CODE)
        if(code == 0) {
            onSuccess(json)
        }
        else if (code == 3) {
        httpModule.tokenInvalid(this)
    }
        else {
            onOccurFailed(code,json.getString(HTTP.MSG))
        }

    }
}