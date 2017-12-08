package com.example.yumi.jpushtest.ui

import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.config.getIMModule
import com.example.yumi.jpushtest.environment.config.wholeObserver
import com.example.yumi.jpushtest.environment.module.IMModule
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.ui_test.*


/**
 * Created by CimZzz(王彦雄) on 11/25/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */
class TestUI : BaseUI<IPresenter<*,*>>() {
    val stateRecord : StateRecord = StateRecord.newInstance(TestUI::class.java)
    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_test)
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        getIMModule().curUserName = "CimZzz"
        testBtn.setOnClickListener {
            getIMModule().login()
            getIMModule().sendTextMsg("13465","TiwZzz")
        }

        stateRecord.wholeObserver(IMModule.STATE_LOGIN,object : BaseObserver() {
            override fun notify(vararg objects: Any) {
                logV("登录成功")
            }
        })
    }

}