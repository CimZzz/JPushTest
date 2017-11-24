package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import android.view.View
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.BasePagerPool
import com.example.yumi.jpushtest.utils.startAnimation
import com.example.yumi.jpushtest.widgets.SliderSwitchView
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.pager_enter.*
import kotlinx.android.synthetic.main.pager_forgive.*
import kotlinx.android.synthetic.main.pager_forgive_mail.*
import kotlinx.android.synthetic.main.pager_forgive_phone.*
import kotlinx.android.synthetic.main.pager_pwdcofirm.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class PasswordConfirmPager : BasePager<IPresenter<*,*>>() {
    var stateRecord : StateRecord? = null

    init {
        backEventSteam = BackEventSteam {
            stateRecord!!.notifyState(ForgivePager.STATE_BACK)
            backEventSteam!!.clear()
            true
        }
    }

    override fun init() {
    }

    override fun initViewID(): Int = R.layout.pager_pwdcofirm

    override fun onViewInitialization(savedInstanceState: Bundle?) {
        pwdConfirmCompleted.setOnClickListener {
        }
    }
}