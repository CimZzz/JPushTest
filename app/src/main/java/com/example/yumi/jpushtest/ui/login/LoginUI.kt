package com.example.yumi.jpushtest.ui.login

import android.content.Context
import android.os.Bundle
import android.support.design.R.attr.height
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.BasePagerPool
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.utils.startAnimation
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import com.virtualightning.stateframework.constant.ReferenceType
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.ObserverBuilder
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.ui_login.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class LoginUI : BaseUI<IPresenter<*,*>>() {
    val stateRecord : StateRecord = StateRecord.newInstance(this.javaClass)
    val basePagerPool : BasePagerPool = BasePagerPool()

    var lastClickTime : Long = 0
    val backEventSteam : BackEventSteam = BackEventSteam {
        val curTime = System.currentTimeMillis()
        if(curTime - lastClickTime < 3000) {
            finish()
        }
        else {
            lastClickTime = curTime
            sendToast("再次点击退出应用")
        }
        true
    }

    companion object {
        val STATE_SHOW_THIRD = "s0"
        val STATE_FORGIVE = "s1"
        val STATE_ENTER = "s2"
    }

    override fun onBaseUICreate(creater: ActionBarUICreater?) {
        creater!!.setLayoutID(R.layout.ui_login)
        creater!!.setHasToolBar(false)
        openAutoCancelSoft = true

        stateRecord.registerObserver(ObserverBuilder().stateId(STATE_SHOW_THIRD).refType(ReferenceType.STRONG).observer(object:BaseObserver() {
            override fun notify(vararg objects: Any?) {
                if(objects[0] as Boolean) {
                    loginThiLog.visibility = View.VISIBLE
                    startAnimation(loginThiLog,R.anim.fade_in)
                } else {
                    loginThiLog.visibility = View.GONE
                    startAnimation(loginThiLog,R.anim.fade_out)
                }
            }
        }))

        stateRecord.registerObserver(ObserverBuilder().stateId(STATE_FORGIVE).refType(ReferenceType.STRONG).observer(object:BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val forgivePager = basePagerPool.getPager(ForgivePager::class.java)
                forgivePager.stateRecord = stateRecord
                changePager(forgivePager)
            }
        }))

        stateRecord.registerObserver(ObserverBuilder().stateId(STATE_ENTER).refType(ReferenceType.STRONG).observer(object:BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val enterPager = basePagerPool.getPager(EnterPager::class.java)
                enterPager.stateRecord = stateRecord
                changePager(enterPager)
            }
        }))

    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        val enterPager = basePagerPool.getPager(EnterPager::class.java)
        enterPager.stateRecord = stateRecord
        changePager(enterPager)
    }

    private fun changePager(pager:BasePager<*>) {
        backEventSteam.addEvent(pager.backEventSteam)
        val transition = supportFragmentManager.beginTransaction()
        transition.setCustomAnimations(R.anim.fade_in,0)
        transition.replace(R.id.customContainerId,pager)
        transition.addToBackStack(null)
        transition.commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            backEventSteam.checkBackEvent()
            true
        }
        else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        stateRecord.unregisterObserver()
    }
}