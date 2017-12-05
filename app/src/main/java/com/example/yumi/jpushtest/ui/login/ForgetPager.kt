package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.config.registerObserver
import com.example.yumi.jpushtest.utils.BasePagerPool
import com.example.yumi.jpushtest.widgets.SliderSwitchView
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.pager_forget.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ForgetPager : BasePager<IPresenter<*,*>>() {
    companion object {
        val STATE_CONFIRM = "ss0"
    }

    val basePagerPool : BasePagerPool = BasePagerPool()
    lateinit var stateRecord : StateRecord
    var isPhone = false
    var isConfirm = false

    init {
        eventSteam = EventSteam {
            if(it != LoginUI.EVENT_BACK)
                return@EventSteam false
            if(isConfirm) {
                if (isPhone)
                    changeToPhonePage(false)
                else changeToEmailPage(false)
                isConfirm = false
            }
            else {
                stateRecord.notifyState(LoginUI.STATE_SHOW_THIRD, true)
                stateRecord.notifyState(LoginUI.STATE_SHOW_BACK, false)
                stateRecord.notifyState(LoginUI.STATE_ENTER)
                eventSteam!!.clear()

                if(isPhone)
                    closePhonePage()
                else closeEmailPage()
            }
            true
        }
    }

    override fun init() {
        stateRecord.registerObserver(STATE_CONFIRM,object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                val confirmPage = basePagerPool.getPager(PasswordConfirmPager::class.java)
                confirmPage.stateRecord = stateRecord
                if(objects[0] as Boolean) {
                    confirmPage.phoneNum = objects[1] as String
                    confirmPage.email = ""
                }
                else {
                    confirmPage.email = objects[1] as String
                    confirmPage.phoneNum = ""
                }
                confirmPage.validationCode = objects[2] as String
                isConfirm = true
                changePager(confirmPage)
            }
        })
    }
    override fun initViewID(): Int = R.layout.pager_forget

    override fun onViewInitialization(savedInstanceState: Bundle?) {
        forgetTab.switchListener = object : SliderSwitchView.ISwitchListener {
            override fun onClick(isLeft: Boolean) {
                if(isLeft)
                //phone
                    changeToPhonePage(true)
                else
                //mail
                    changeToEmailPage(true)
            }

        }
        stateRecord.notifyState(LoginUI.STATE_SHOW_THIRD,false)
        stateRecord.notifyState(LoginUI.STATE_SHOW_BACK, true)

        changeToPhonePage(true)
    }

    private fun changeToPhonePage(isRestore : Boolean) {
        forgetTab.setActiveSide(true)
        val phonePager = basePagerPool.getPager(ForgetPhonePager::class.java)
        phonePager.stateRecord = stateRecord
        phonePager.isRestore = isRestore
        changePager(phonePager)
        isPhone = true
    }

    private fun changeToEmailPage(isRestore : Boolean) {
        forgetTab.setActiveSide(false)
        val mailPager = basePagerPool.getPager(ForgetEmailPager::class.java)
        mailPager.stateRecord = stateRecord
        mailPager.isRestore = isRestore
        changePager(mailPager)
        isPhone = false
    }

    private fun closePhonePage() {
        closePager(basePagerPool.getPager(ForgetPhonePager::class.java))
    }


    private fun closeEmailPage() {
        closePager(basePagerPool.getPager(ForgetEmailPager::class.java))
    }


    private fun closePager(pager:BasePager<*>) {
        val transition = fragmentManager.beginTransaction()
        transition.remove(pager)
        transition.addToBackStack(null)
        transition.commit()
    }

    private fun changePager(pager:BasePager<*>) {
        eventSteam!!.addEvent(pager.eventSteam)
        val transition = fragmentManager.beginTransaction()
        transition.setCustomAnimations(R.anim.fade_in,0)
        transition.replace(R.id.forgetContainer,pager)
        transition.addToBackStack(null)
        transition.commit()
    }
}