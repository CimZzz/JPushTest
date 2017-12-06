package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.isEmptyString
import com.example.yumi.jpushtest.utils.isPhoneNum
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.pager_forget_phone.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ForgetPhonePager : BasePager<IPresenter<*,*>>() {
    var stateRecord : StateRecord? = null

    var isRestore = false

    override fun initViewID(): Int = R.layout.pager_forget_phone

    override fun onViewInitialization(savedInstanceState: Bundle?) {
        forgetPhoneNext.setOnClickListener {
            val phoneNum = forgetPhoneNum.text.toString()
            val validation = forgetPhoneValidation.text.toString()

            if(isEmptyString(validation)) {
                sendToast("验证码不能为空")
                return@setOnClickListener
            }

            if(!isPhoneNum(phoneNum)) {
                sendToast("不是一个合法的手机号")
                return@setOnClickListener
            }


            if(isPhoneNum(phoneNum))
                stateRecord!!.notifyState(ForgetPager.STATE_CONFIRM,true
                    , phoneNum
                    , validation)

        }

        forgetPhoneValidationBtn.setOnClickListener {
            val phoneNum = forgetPhoneNum.text.toString()
            stateRecord!!.notifyState(LoginPresenter.STATE_PHONE_VALIDATION,phoneNum)
        }
    }

    override fun onResume() {
        super.onResume()
        if(isRestore) {
            isRestore = false
            forgetPhoneNum.setText("")
            forgetPhoneValidation.setText("")
        }
    }
}