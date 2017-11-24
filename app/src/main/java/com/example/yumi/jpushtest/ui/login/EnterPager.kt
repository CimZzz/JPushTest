package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import android.view.View
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
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

    override fun init() {
    }

    override fun initViewID(): Int = R.layout.pager_enter

    override fun onViewInitialization(savedInstanceState: Bundle?) {
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
                if(isLeft) {
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

        enterForgive.setOnClickListener {
            stateRecord!!.notifyState(LoginUI.STATE_FORGIVE)
        }
    }

}