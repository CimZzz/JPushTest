package com.example.yumi.jpushtest.ui.load

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.ui.login.LoginUI
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import com.virtualightning.stateframework.constant.ReferenceType
import com.virtualightning.stateframework.state.BaseObserver
import com.virtualightning.stateframework.state.ObserverBuilder
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.ui_load.*
import java.util.*
import android.util.DisplayMetrics



/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class LoadUI : BaseUI<IPresenter<*,*>>() {
    var timer : Timer = Timer()
    var task : TimerTask = Task()
    val stateRecord = StateRecord.newInstance(this.javaClass)

    override fun onBaseUICreate(creater: ActionBarUICreater?) {
        creater!!.setLayoutID(R.layout.ui_load)
        creater!!.setHasToolBar(false)
        stateRecord.registerObserver(ObserverBuilder().stateId("changeUI").refType(ReferenceType.STRONG).observer(object : BaseObserver() {
            override fun notify(vararg objects: Any?) {
                startActivity(Intent(this@LoadUI,LoginUI::class.java)
                        ,ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoadUI,loadBotText,"SHARE").toBundle())
                finish()
            }
        }))
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        sendToast(dm.densityDpi)
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        task = Task()
        timer.schedule(task,2000)
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        task.cancel()
    }

    override fun onDestroy() {
        stateRecord.unregisterObserver()
        super.onDestroy()
    }

    inner class Task : TimerTask() {
        override fun run() {
            stateRecord.notifyState("changeUI")
        }
    }
}