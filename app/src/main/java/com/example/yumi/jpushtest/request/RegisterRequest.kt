package com.example.yumi.jpushtest.request

import com.alibaba.fastjson.JSONObject
import com.example.yumi.jpushtest.base.BaseRequest
import com.example.yumi.jpushtest.environment.HTTP
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.stateframework.state.StateRecord
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.HttpMethod
import java.io.IOException

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class RegisterRequest(stateRecord: StateRecord) : BaseRequest(stateRecord) {
    lateinit var userName : String
    lateinit var userPwd : String
    lateinit var validation : String

    override fun createRequest(): Request = Request.Builder()
            .method("POST",FormBody.Builder()
                    .add(HTTP.Token.TOKEN,token)
                    .add(HTTP.Register.USERNAME,userName)
                    .add(HTTP.Register.USERPWD,userPwd)
                    .add(HTTP.Register.VALIDATION,validation)
                    .build())
            .url(HTTP.Register.URL)
            .build()


    override fun onSuccess(jsonObject: JSONObject) {
        val obj = JSONObject()
        obj.put(HTTP.Register.USERNAME,userName)
        obj.put(HTTP.Register.USERPWD,userPwd)
        jsonObject.put(HTTP.DATA,obj)
        stateRecord.notifyState(HTTP.Register.STATE,true,jsonObject,"")
    }

    override fun onNetworkFailed() {
        stateRecord.notifyState(HTTP.Register.STATE,false,null,"网络连接失败")
    }

    override fun onOccurFailed(code: Int,msg:String) {
        logV("Register Error : $code")
        stateRecord.notifyState(HTTP.Register.STATE,false,null,msg)
    }
}