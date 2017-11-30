package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
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
    lateinit var stateRecord : StateRecord

    var isRestore = false

    override fun init() = Unit

    override fun initViewID(): Int = R.layout.pager_enter

    override fun onViewInitialization(savedInstanceState: Bundle?) {
        enterTab.switchListener = object : SliderSwitchView.ISwitchListener {
            override fun onClick(isLeft: Boolean) {
                switchToView(isLeft)
            }
        }
        findViewById<View>(R.id.enterRegisterText).setOnClickListener {
            switchToView(false)
        }
        findViewById<View>(R.id.enterForget).setOnClickListener {
            stateRecord.notifyState(LoginUI.STATE_FORGET)
        }
        findViewById<View>(R.id.enterLoginBtn).setOnClickListener {
            stateRecord.notifyState(LoginPresenter.STATE_LOGIN,enterLoginUserName.text.toString(),enterLoginUserPwd.text.toString())
        }
        findViewById<View>(R.id.enterRegisterBtn).setOnClickListener {
            stateRecord.notifyState(LoginPresenter.STATE_REGISTER,enterRegisterUserName.text.toString(),enterRegisterUserPwd.text.toString(),enterRegisterValidation.text.toString())
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