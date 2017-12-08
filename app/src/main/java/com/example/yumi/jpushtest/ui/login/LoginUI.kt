package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.config.observer
import com.example.yumi.jpushtest.ui.main.MainUI
import com.example.yumi.jpushtest.utils.BasePagerPool
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.ui_login.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class LoginUI : BaseUI<IPresenter<*,*>>(),ILoginContract.View {

    companion object {
        val STATE_SHOW_THIRD = "s0"
        val STATE_FORGET = "s1"
        val STATE_ENTER = "s2"
        val STATE_SHOW_BACK = "s3"
        val STATE_REGISTER_SUCCESS = "s4"

        val EVENT_BACK = "e0"
    }

    val stateRecord : StateRecord = StateRecord.newInstance(this.javaClass)
    val basePagerPool : BasePagerPool = BasePagerPool()

    var lastClickTime : Long = 0
    val eventSteam: EventSteam = EventSteam {
        when(it) {
            EVENT_BACK-> {
                val curTime = System.currentTimeMillis()
                if(curTime - lastClickTime < 3000)
                    finish()
                else {
                    lastClickTime = curTime
                    sendToast("再次点击退出应用")
                }
            }
        }
        true
    }

    /*Implement interface function*/
    override fun loginSuccess() {
        changeUI(MainUI::class.java)
        finish()
    }

    override fun updatePwdSuccess() {
        stateRecord.notifyState(STATE_SHOW_THIRD,true)
        stateRecord.notifyState(STATE_SHOW_BACK,false)
        stateRecord.notifyState(STATE_ENTER)
    }




    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_login)
        creater.setHasToolBar(false)

        presenter = LoginPresenter(this,LoginMethod(stateRecord,getCustomApplication().httpModule))
        openAutoCancelSoft = true


        stateRecord.observer(STATE_SHOW_THIRD,object:BaseObserver() {
            override fun notify(vararg objects: Any?) {
                if(objects[0] as Boolean) {
                    loginThiLog.visibility = View.VISIBLE
                } else {
                    loginThiLog.visibility = View.GONE
                }
            }
        })

        stateRecord.observer(STATE_SHOW_BACK,object:BaseObserver() {
            override fun notify(vararg objects: Any?) {
                if(objects[0] as Boolean) {
                    loginBack.visibility = View.VISIBLE
                } else {
                    loginBack.visibility = View.GONE
                }
            }
        })

        stateRecord.observer(STATE_FORGET,object:BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val forget = basePagerPool.getPager(ForgetPager::class.java)
                forget.stateRecord = stateRecord
                changePager(forget)
            }
        })

        stateRecord.observer(STATE_ENTER,object:BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val enterPager = basePagerPool.getPager(EnterPager::class.java)
                enterPager.stateRecord = stateRecord
                enterPager.isRestore = true
                changePager(enterPager)
            }
        })

    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        loginBack.setOnClickListener {
            eventSteam.checkEvent(EVENT_BACK)
        }

        val enterPager = basePagerPool.getPager(EnterPager::class.java)
        enterPager.stateRecord = stateRecord
        changePager(enterPager)
    }

    private fun changePager(pager:BasePager<*>) {
        eventSteam.addEvent(pager.eventSteam)
        val transition = supportFragmentManager.beginTransaction()
        transition.setCustomAnimations(R.anim.fade_in,0)
        transition.replace(R.id.customContainerId,pager)
        transition.addToBackStack(null)
        transition.commit()
    }

    override fun gainStateRecord(): StateRecord? = stateRecord

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            eventSteam.checkEvent(EVENT_BACK)
            true
        }
        else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        stateRecord.unregisterObserver()
    }
}