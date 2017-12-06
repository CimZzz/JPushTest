package com.example.yumi.jpushtest.ui.login

import com.example.yumi.jpushtest.environment.module.HttpModule
import com.example.yumi.jpushtest.request.*
import com.virtualightning.stateframework.state.StateRecord

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class LoginMethod(val stateRecord: StateRecord,val httpModule: HttpModule) : ILoginContract.Method {
    override fun getEmailCode(email: String) {
        val emailCodeRequest = EmailCodeRequest(stateRecord)
        emailCodeRequest.email = email
        httpModule.enqueue(emailCodeRequest)
    }

    override fun getPhoneCode(phoneNum: String) {
        val phoneCodeRequest = PhoneCodeRequest(stateRecord)
        phoneCodeRequest.phoneNum = phoneNum
        httpModule.enqueue(phoneCodeRequest)
    }

    override fun updatePassword(phoneNum: String, email: String, newPwd: String, validationCode: String) {
        val updateRequest = UpdatePasswordRequest(stateRecord)
        updateRequest.phoneNum = phoneNum
        updateRequest.email = email
        updateRequest.newPwd = newPwd
        updateRequest.validation = validationCode
        httpModule.enqueue(updateRequest)
    }


    override fun register(userName: String, userPwd: String, validationCode: String) {
        val registerRequest = RegisterRequest(stateRecord)
        registerRequest.userName = userName
        registerRequest.userPwd = userPwd
        registerRequest.validation = validationCode
        httpModule.enqueue(registerRequest)
    }

    override fun login(userName: String, userPwd: String) {
        val loginRequest = LoginRequest(stateRecord)
        loginRequest.userName = userName
        loginRequest.userPwd = userPwd
        httpModule.enqueue(loginRequest)
    }

}