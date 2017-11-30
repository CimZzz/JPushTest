package com.example.yumi.jpushtest.ui.login

import com.alibaba.fastjson.JSONObject
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.HTTP
import com.example.yumi.jpushtest.environment.config.registerObserver
import com.example.yumi.jpushtest.extend.HTTPJSONObserver
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.stateframework.anno.state.BindObserver
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.utils.Analyzer

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

        stateRecord.registerObserver(STATE_LOGIN,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                view.showLoadingBar("正在登录")
                method.login(objects[0] as String, objects[1] as String)
            }
        })

        stateRecord.registerObserver(STATE_REGISTER,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                view.showLoadingBar("正在注册")
                method.register(objects[0] as String, objects[1] as String,objects[2] as String)
            }
        })

        stateRecord.registerObserver(HTTP.Login.STATE,object : HTTPJSONObserver() {
            override fun onHttpCallBack(isSuccess: Boolean, json: JSONObject?, msg: String?) {
                if(isSuccess) {
                    //登录成功
                    view.closeLoadingBar()
                    view.loginSuccess()
                }
                else {
                    view.closeLoadingBar()
                    view.sendToast(msg!!)
                }
            }
        })
    }
}
