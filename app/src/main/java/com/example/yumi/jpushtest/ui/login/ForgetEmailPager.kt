package com.example.yumi.jpushtest.ui.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BasePager
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.pager_forget_mail.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ForgetEmailPager : BasePager<IPresenter<*,*>>() {
    var stateRecord : StateRecord? = null


    var isRestore = false

    override fun init() {
    }

    override fun initViewID(): Int = R.layout.pager_forget_mail



    override fun onViewInitialization(savedInstanceState: Bundle?) {
        forgiveEmailNext.setOnClickListener {
            stateRecord!!.notifyState(ForgetPager.STATE_CONFIRM,false,forgiveEmailText.text.toString(),"123123123")
        }
    }

    override fun onResume() {
        super.onResume()
        if(isRestore) {
            isRestore = false
            forgiveEmailText.setText("")
        }
    }
}