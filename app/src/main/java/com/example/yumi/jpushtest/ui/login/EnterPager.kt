package com.example.yumi.jpushtest.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.config.registerObserver
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.utils.startAnimation
import com.example.yumi.jpushtest.widgets.SliderSwitchView
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.pager_enter.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class EnterPager : BasePager<IPresenter<*,*>>() {
    var stateRecord : StateRecord? = null
    var isNeedRegisterInfo = false

    var loginUserName : EditText? = null
    lateinit var loginPassword : EditText
    lateinit var registerUserName : EditText
    lateinit var registerPassword : EditText
    lateinit var registerValidation : EditText

    init {
        eventSteam = EventSteam {
            return@EventSteam when(it) {
                LoginUI.EVENT_REGISTER_SUCCESS-> {
                    switchToView(true)
                    loginUserName!!.setText(registerUserName.text.toString())
                    true
                }
                LoginUI.EVENT_CLEAR_UI-> {
                    loginUserName!!.setText("")
                    loginPassword.setText("")
                    registerUserName.setText("")
                    registerPassword.setText("")
                    registerValidation.setText("")
                    true
                }
                else-> false
            }
        }
    }

    override fun init() {
        logV("Enter : Init")
    }

    override fun initViewID(): Int = R.layout.pager_enter

    override fun onViewInitialization(savedInstanceState: Bundle?) {
        logV("Enter : ViewInitialization : ${loginUserName?.text}")
        loginUserName = enterLoginUserName
        loginPassword = enterLoginUserPwd
        registerUserName = enterRegisterUserName
        registerPassword = enterRegisterUserPwd
        registerValidation = enterRegisterValidation

        if(isNeedRegisterInfo) {
            enterLoginUserName.setText(enterRegisterUserName.text.toString())
            isNeedRegisterInfo = false
        }
        else enterLoginUserName.setText("")
        enterRegisterUserName.setText("")
        enterLoginUserName.text.clear()
        loginUserName!!.setText("")

        enterRegisterText.setOnClickListener {
            if(enterTab.isLeftActive) {
                enterTab.setActiveSide(false)
                enterLogin.visibility = View.GONE
                enterRegister.visibility = View.VISIBLE
                startAnimation(enterRegister,R.anim.fade_in)
                stateRecord!!.notifyState(LoginUI.STATE_SHOW_THIRD,false)
            }
        }
        enterTab.switchListener = object : SliderSwitchView.ISwitchListener {
            override fun onClick(isLeft: Boolean) {
                switchToView(isLeft)
            }
        }

        enterForgive.setOnClickListener {
            stateRecord!!.notifyState(LoginUI.STATE_FORGIVE)
        }

        enterLoginBtn.setOnClickListener {
            stateRecord!!.notifyState(LoginPresenter.STATE_LOGIN,enterLoginUserName.text.toString(),enterLoginUserPwd.text.toString())
        }

        enterRegisterBtn.setOnClickListener {
            stateRecord!!.notifyState(LoginPresenter.STATE_REGISTER
                    , enterRegisterUserName.text.toString(),enterRegisterUserPwd.text.toString(),enterRegisterValidation.text.toString())
        }
    }


    private fun switchToView(isLogin : Boolean) {
        enterTab.setActiveSide(isLogin)
        if(isLogin) {
            enterRegister.visibility = View.GONE
            enterLogin.visibility = View.VISIBLE
            startAnimation(enterLogin,R.anim.fade_in)
            stateRecord!!.notifyState(LoginUI.STATE_SHOW_THIRD,true)
        } else {
            enterLogin.visibility = View.GONE
            enterRegister.visibility = View.VISIBLE
            startAnimation(enterRegister,R.anim.fade_in)
            stateRecord!!.notifyState(LoginUI.STATE_SHOW_THIRD,false)
        }
    }
}