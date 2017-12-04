package com.example.yumi.jpushtest.ui.order

import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater

/**
 * Created by CimZzz(王彦雄) on 2017/12/1.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class OrderUI : BaseUI<IPresenter<*, *>>() {
    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_order)
        creater.setActionBarID(R.layout.actionbar_title_back_send)
    }

    override fun onViewInit(savedInstanceState: Bundle?) {

    }

}