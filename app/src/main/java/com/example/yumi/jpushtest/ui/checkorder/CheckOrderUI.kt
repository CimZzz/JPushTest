package com.example.yumi.jpushtest.ui.checkorder

import android.os.Bundle
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.actionbar_title_back.*
import kotlinx.android.synthetic.main.ui_checkorder.*

/**
 * Created by CimZzz(王彦雄) on 2017/12/4.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class CheckOrderUI : BaseUI<IPresenter<*, *>>() {

    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_checkorder)
        creater.setActionBarID(R.layout.actionbar_title_back)
    }


    override fun onViewInit(savedInstanceState: Bundle?) {
        actionbarTitle.text = "寄件单"
        actionbarBack.setOnClickListener {
            finish()
        }
        checkOrderContainer.scrollTo(-resources.getDimension(R.dimen.checkOrder_MarginSide).toInt(),0)
    }

}