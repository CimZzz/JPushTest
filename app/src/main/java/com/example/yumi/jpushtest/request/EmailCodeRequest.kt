package com.example.yumi.jpushtest.request

import com.alibaba.fastjson.JSONObject
import com.example.yumi.jpushtest.base.BaseRequest
import com.example.yumi.jpushtest.environment.HTTP
import com.virtualightning.stateframework.state.StateRecord
import okhttp3.FormBody
import okhttp3.Request

/**
 * Created by CimZzz(王彦雄) on 2017/12/6.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class EmailCodeRequest(stateRecord: StateRecord) : BaseRequest(stateRecord) {
    lateinit var email: String

    override fun createRequest(): Request = Request.Builder()
            .method("POST", FormBody.Builder()
                    .add(HTTP.Token.TOKEN,token)
                    .add(HTTP.EmailCode.EMAIL, email)
                    .build())
            .url(HTTP.EmailCode.URL)
            .build()


    override fun onSuccess(jsonObject: JSONObject) {
        stateRecord.notifyState(HTTP.EmailCode.STATE,true,jsonObject,"")
    }

    override fun onNetworkFailed() {
        stateRecord.notifyState(HTTP.EmailCode.STATE,false,null,"网络连接失败")
    }

    override fun onOccurFailed(code: Int,msg:String) {
        stateRecord.notifyState(HTTP.EmailCode.STATE,false,null,msg)
    }
}