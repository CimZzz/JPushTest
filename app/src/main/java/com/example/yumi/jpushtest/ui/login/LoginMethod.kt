package com.example.yumi.jpushtest.ui.login

import com.example.yumi.jpushtest.environment.module.HttpModule
import com.example.yumi.jpushtest.request.LoginRequest
import com.virtualightning.stateframework.state.StateRecord

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class LoginMethod(val stateRecord: StateRecord,val httpModule: HttpModule) : ILoginContract.Method {
    override fun register(userName: String, userPwd: String, validationCode: String) {

    }

    override fun login(userName: String, userPwd: String) {
        val loginRequest = LoginRequest(stateRecord)
        loginRequest.userName = userName
        loginRequest.userPwd = userPwd
        httpModule.enqueue(loginRequest)
    }

}