package com.example.yumi.jpushtest.ui.main

import android.content.Intent
import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.environment.REQ_MAIN
import com.example.yumi.jpushtest.environment.RES_DATE_CHANGE
import com.example.yumi.jpushtest.ui.country.CountryUI
import com.example.yumi.jpushtest.ui.send.SendUI
import com.example.yumi.jpushtest.ui.time.TimeUI
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.ui_main.*

/**
 * Created by CimZzz(王彦雄) on 11/27/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */
class MainUI : BaseUI<IPresenter<*,*>>() {

    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_main)
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        mainFromAddress.setOnClickListener {
            changeUI(CountryUI::class.java)
        }
        mainToAddress.setOnClickListener {
            changeUI(CountryUI::class.java)
        }
        mainTime.setOnClickListener {
            changeUI(TimeUI::class.java)
        }
        mainSendBtn.setOnClickListener {
            changeUI(SendUI::class.java)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQ_MAIN) {
            when(resultCode) {
                RES_DATE_CHANGE-> {

                }
            }
        }
        else super.onActivityResult(requestCode, resultCode, data)
    }

}