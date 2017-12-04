package com.example.yumi.jpushtest.ui

import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.ui_test.*

/**
 * Created by CimZzz(王彦雄) on 11/25/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */
class TestUI : BaseUI<IPresenter<*,*>>() {
    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_test)
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        testClick.setOnClickListener {
            logV("click")
            testContainer.setSide(!testContainer.isOwn)
        }
    }

}