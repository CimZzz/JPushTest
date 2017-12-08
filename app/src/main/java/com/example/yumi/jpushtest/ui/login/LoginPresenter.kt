package com.example.yumi.jpushtest.ui.login

import com.alibaba.fastjson.JSONObject
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.HTTP
import com.example.yumi.jpushtest.environment.config.observer
import com.example.yumi.jpushtest.extend.HTTPJSONObserver
import com.example.yumi.jpushtest.utils.isEmail
import com.example.yumi.jpushtest.utils.isEmptyString
import com.example.yumi.jpushtest.utils.isPhoneNum
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.stateframework.state.BaseObserver

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br></br>
 * Since : 项目名称_版本 <br></br>
 * Description : <br></br>
 * 描述
 */
class LoginPresenter(view: ILoginContract.View, method: ILoginContract.Method) : IPresenter<ILoginContract.View, ILoginContract.Method>(view, method) {

    companion object {
        val STATE_LOGIN = "p1"
        val STATE_REGISTER = "p2"
        val STATE_PHONE_VALIDATION = "p3"
        val STATE_EMAIL_VALIDATION = "p4"
        val STATE_UPDATE_PWD = "p5"
    }

    init {
        val stateRecord = view.gainStateRecord()!!

        stateRecord.observer(STATE_LOGIN,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val userName = objects[0] as String
                val userPwd =  objects[1] as String

                if(isEmptyString(userName)) {
                    view.sendToast("用户名不能为空")
                    return
                }

                if(isEmptyString(userPwd)) {
                    view.sendToast("用户密码不能为空")
                    return
                }


                view.showLoadingBar("正在登录")
                method.login(userName,userPwd)
                view.loginSuccess()
            }
        })

        stateRecord.observer(STATE_REGISTER,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val userName = objects[0] as String
                val userPwd =  objects[1] as String
                val validationCode = objects[2] as String

                if(isEmptyString(userName)) {
                    view.sendToast("用户名不能为空")
                    return
                }

                if(isEmptyString(validationCode)) {
                    view.sendToast("验证码不能为空")
                    return
                }

                if(isEmptyString(userPwd)) {
                    view.sendToast("用户密码不能为空")
                    return
                }

                view.showLoadingBar("正在注册")
                method.register(userName,userPwd,validationCode)
            }
        })

        stateRecord.observer(STATE_UPDATE_PWD,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                /*
                * 0 phone num
                * 1 email
                * 2 new pwd
                * 3 confirm
                * 4 validation
                */
                val phoneNum = objects[0] as String
                val email = objects[1] as String
                val newPwd = objects[2] as String
                val confirm = objects[3] as String
                val validation = objects[4] as String


                if(isEmptyString(phoneNum) && isEmptyString(email)) {
                    view.sendToast("电话号或邮箱不能为空")
                    return
                }

                if(isEmptyString(newPwd) || isEmptyString(confirm)) {
                    view.sendToast("密码不能为空")
                    return
                }

                if(newPwd != confirm) {
                    view.sendToast("两次密码不一致")
                    return
                }


                if(isEmptyString(validation)) {
                    view.sendToast("验证码不能为空")
                    return
                }


                view.showLoadingBar("正在修改密码")
                method.updatePassword(phoneNum,email,newPwd,validation)
            }

        })

        stateRecord.observer(STATE_PHONE_VALIDATION,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val phoneNum = objects[0] as String
                if(!isPhoneNum(phoneNum)) {
                    view.sendToast("不是一个合法的手机号")
                    return
                }

                method.getPhoneCode(phoneNum)
            }
        })

        stateRecord.observer(STATE_EMAIL_VALIDATION,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val email = objects[0] as String
                if(!isEmail(email)) {
                    view.sendToast("不是一个合法的邮箱格式")
                    return
                }

                method.getEmailCode(email)
            }
        })

        stateRecord.observer(HTTP.Login.STATE,object : HTTPJSONObserver() {
            override fun onHttpCallBack(isSuccess: Boolean, json: JSONObject?, msg: String?) {
                if(isSuccess) {
                    //登录成功
                    view.sendToast("登录成功")
                    view.closeLoadingBar()
                    view.loginSuccess()
                }
                else {
                    view.closeLoadingBar()
                    view.sendToast(msg!!)
                }
            }
        })

        stateRecord.observer(HTTP.Register.STATE,object : HTTPJSONObserver() {
            override fun onHttpCallBack(isSuccess: Boolean, json: JSONObject?, msg: String?) {
                if(isSuccess) {
                    //注册成功
                    val data = json!!.getJSONObject(HTTP.DATA)
                    val userName = data.getString(HTTP.Register.USERNAME)
                    val userPwd = data.getString(HTTP.Register.USERPWD)
                    view.sendToast("注册成功")
                    view.closeLoadingBar()
                    stateRecord.notifyState(LoginUI.STATE_REGISTER_SUCCESS,userName,userPwd)
                }
                else {
                    view.closeLoadingBar()
                    view.sendToast(msg!!)
                }
            }
        })

        stateRecord.observer(HTTP.UpdatePwd.STATE,object : HTTPJSONObserver() {
            override fun onHttpCallBack(isSuccess: Boolean, json: JSONObject?, msg: String?) {
                if(isSuccess) {
                    //修改密码成功
                    view.sendToast("密码修改成功")
                    view.closeLoadingBar()
                    view.updatePwdSuccess()
                }
                else {
                    view.closeLoadingBar()
                    view.sendToast(msg!!)
                }
            }
        })

        stateRecord.observer(HTTP.PhoneCode.STATE,object : HTTPJSONObserver() {
            override fun onHttpCallBack(isSuccess: Boolean, json: JSONObject?, msg: String?) {
                if(isSuccess) {
                    val data = json!!.getJSONObject(HTTP.DATA)
                    view.showInfoBar(data.getString(HTTP.PhoneCode.CODE))
                }
                else logV(false)
            }
        })

        stateRecord.observer(HTTP.EmailCode.STATE,object : HTTPJSONObserver() {
            override fun onHttpCallBack(isSuccess: Boolean, json: JSONObject?, msg: String?) {
                if(isSuccess)
                    view.sendToast("发送邮件验证码成功")
                else view.sendToast("发送邮件验证码失败")
            }
        })
    }
}