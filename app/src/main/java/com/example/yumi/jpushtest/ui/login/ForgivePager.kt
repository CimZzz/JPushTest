package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.BasePagerPool
import com.example.yumi.jpushtest.widgets.SliderSwitchView
import com.virtualightning.stateframework.constant.ReferenceType
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.ObserverBuilder
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.pager_forgive.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ForgivePager : BasePager<IPresenter<*,*>>() {
    companion object {
        val STATE_CONFIRM = "s0"
        val STATE_CONFIRM_PWD = "s1"
        val STATE_BACK = "s2"
    }

    var stateRecord : StateRecord? = null
    var self : StateRecord = StateRecord.newInstance(this.javaClass)
    var basePagerPool : BasePagerPool = BasePagerPool()
    var isPhone = false

    init {
        backEventSteam = BackEventSteam {
            stateRecord!!.notifyState(LoginUI.STATE_SHOW_THIRD,true)
            stateRecord!!.notifyState(LoginUI.STATE_ENTER)
            backEventSteam!!.clear()
            closePager(basePagerPool.getPager(ForgivePhonePager::class.java))

            true
        }
    }

    override fun init() {
        self.registerObserver(ObserverBuilder().stateId(STATE_CONFIRM).refType(ReferenceType.STRONG).observer(object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val phonePager = basePagerPool.getPager(PasswordConfirmPager::class.java)
                phonePager.stateRecord = self
                changePager(phonePager)
            }
        }))

        self.registerObserver(ObserverBuilder().stateId(STATE_BACK).refType(ReferenceType.STRONG).observer(object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                if (isPhone) {
                    val phonePager = basePagerPool.getPager(ForgivePhonePager::class.java)
                    phonePager.stateRecord = self
                    changePager(phonePager)
                } else {
                    val mailPager = basePagerPool.getPager(ForgiveEmailPager::class.java)
                    mailPager.stateRecord = self
                    changePager(mailPager)
                }
            }
        }))
    }

    override fun initViewID(): Int = R.layout.pager_forgive

    override fun onViewInitialization(savedInstanceState: Bundle?) {
        stateRecord!!.notifyState(LoginUI.STATE_SHOW_THIRD,false)

        val startPager = basePagerPool.getPager(ForgivePhonePager::class.java)
        startPager.stateRecord = self
        changePager(startPager)
        isPhone = true

        forgiveTab.switchListener = object : SliderSwitchView.ISwitchListener {
            override fun onClick(isLeft: Boolean) {
                if(isLeft) {
                    //phone
                    val phonePager = basePagerPool.getPager(ForgivePhonePager::class.java)
                    phonePager.stateRecord = self
                    changePager(phonePager)
                    isPhone = true
                } else {
                    //mail
                    val mailPager = basePagerPool.getPager(ForgiveEmailPager::class.java)
                    mailPager.stateRecord = self
                    changePager(mailPager)
                    isPhone = false
                }
            }

        }
    }

    private fun closePager(pager:BasePager<*>) {
        val transition = fragmentManager.beginTransaction()
        transition.remove(pager)
        transition.addToBackStack(null)
        transition.commit()
    }

    private fun changePager(pager:BasePager<*>) {
        backEventSteam!!.addEvent(pager.backEventSteam)
        val transition = fragmentManager.beginTransaction()
        transition.setCustomAnimations(R.anim.fade_in,0)
        transition.replace(R.id.forgiveContainer,pager)
        transition.addToBackStack(null)
        transition.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        self.unregisterObserver()
    }
}