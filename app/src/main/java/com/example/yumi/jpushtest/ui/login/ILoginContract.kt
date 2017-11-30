package com.example.yumi.jpushtest.ui.login

import com.example.yumi.jpushtest.base.IMethod
import com.example.yumi.jpushtest.base.IView

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
interface ILoginContract {
    interface View : IView {
        fun loginSuccess()
    }

    interface Method : IMethod {
        fun login(userName : String,userPwd : String)
        fun register(userName : String,userPwd : String,validationCode : String)
    }
}