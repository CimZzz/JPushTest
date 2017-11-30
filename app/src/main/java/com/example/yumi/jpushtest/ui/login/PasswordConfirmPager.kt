package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.pager_pwdcofirm.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class PasswordConfirmPager : BasePager<IPresenter<*,*>>() {
    var stateRecord : StateRecord? = null
    var phoneNum : String = ""
    var email : String = ""
    var validationCode : String = ""


    override fun init() {
    }

    override fun initViewID(): Int = R.layout.pager_pwdcofirm

    override fun onViewInitialization(savedInstanceState: Bundle?) {
        pwdConfirmCompleted.setOnClickListener {
            stateRecord!!.notifyState(LoginPresenter.STATE_UPDATE_PWD,phoneNum,email,validationCode,pwdConfirmNewPwd.text.toString(),
                    pwdConfirmConfirm.text.toString(),validationCode)
        }
    }
}