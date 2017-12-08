package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import android.view.View
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.config.observer
import com.example.yumi.jpushtest.utils.isEmail
import com.example.yumi.jpushtest.utils.isPhoneNum
import com.example.yumi.jpushtest.utils.startAnimation
import com.example.yumi.jpushtest.widgets.SliderSwitchView
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.pager_enter.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class EnterPager : BasePager<IPresenter<*,*>>() {
    lateinit var stateRecord : StateRecord

    var isRestore = false

    override fun init() = stateRecord.observer(LoginUI.STATE_REGISTER_SUCCESS, object : BaseObserver() {
        override fun notify(vararg objects: Any?) {
            enterLoginUserName.setText(objects[0] as String)
            enterLoginUserPwd.setText(objects[1] as String)
            enterRegisterUserName.setText("")
            enterRegisterUserPwd.setText("")
            enterRegisterValidation.setText("")
            switchToView(true)
        }
    })
    override fun initViewID(): Int = R.layout.pager_enter

    override fun onViewInitialization(savedInstanceState: Bundle?) {
        enterTab.switchListener = object : SliderSwitchView.ISwitchListener {
            override fun onClick(isLeft: Boolean) {
                switchToView(isLeft)
            }
        }
        enterRegisterText.setOnClickListener {
            switchToView(false)
        }
        enterForget.setOnClickListener {
            stateRecord.notifyState(LoginUI.STATE_FORGET)
        }
        enterLoginBtn.setOnClickListener {
            stateRecord.notifyState(LoginPresenter.STATE_LOGIN,enterLoginUserName.text.toString(),enterLoginUserPwd.text.toString())
        }
        enterRegisterBtn.setOnClickListener {
            stateRecord.notifyState(LoginPresenter.STATE_REGISTER,enterRegisterUserName.text.toString(),enterRegisterUserPwd.text.toString(),enterRegisterValidation.text.toString())
        }
        enterRegisterValidationBtn.setOnClickListener {
            val userName = enterRegisterUserName.text.toString()
            if(isPhoneNum(userName))
                stateRecord.notifyState(LoginPresenter.STATE_PHONE_VALIDATION,userName,enterRegisterValidationBtn)
            else if(isEmail(userName))
                stateRecord.notifyState(LoginPresenter.STATE_EMAIL_VALIDATION,userName,enterRegisterValidationBtn)
            else sendToast("用户名格式不正确，必须为手机号或邮箱")
        }
    }

    private fun switchToView(isLogin : Boolean) {
        enterTab.setActiveSide(isLogin)
        if(isLogin) {
            enterRegister.visibility = View.GONE
            enterLogin.visibility = View.VISIBLE
            startAnimation(enterLogin,R.anim.fade_in)
            stateRecord.notifyState(LoginUI.STATE_SHOW_THIRD,true)
        } else {
            enterLogin.visibility = View.GONE
            enterRegister.visibility = View.VISIBLE
            startAnimation(enterRegister,R.anim.fade_in)
            stateRecord.notifyState(LoginUI.STATE_SHOW_THIRD,false)
        }
    }

    override fun onResume() {
        super.onResume()
        if(isRestore) {
            enterLoginUserName.setText("")
            enterLoginUserPwd.setText("")
            enterRegisterUserName.setText("")
            enterRegisterUserPwd.setText("")
            enterRegisterValidation.setText("")
        }
    }
}