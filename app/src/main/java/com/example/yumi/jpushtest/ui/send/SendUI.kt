package com.example.yumi.jpushtest.ui.send

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.entity.SendItem
import com.example.yumi.jpushtest.utils.RecyclerDecoration
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import kotlinx.android.synthetic.main.actionbar_title_back.*
import kotlinx.android.synthetic.main.ui_send.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class SendUI : BaseUI<IPresenter<*, *>>() {
    val adapter = SendAdapter()

    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_send)
        creater.setActionBarID(R.layout.actionbar_title_back)
    }


    override fun onViewInit(savedInstanceState: Bundle?) {
        actionbarTitle.text = "我要寄"
        sendHandleView.refreshComplete()

        sendList.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在获取数据中...")
        sendList.getLoadingLayoutProxy(true, false).setPullLabel("下拉更新...")
        sendList.getLoadingLayoutProxy(true, false).setReleaseLabel("松开以更新")

        sendList.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在获取数据中...")
        sendList.getLoadingLayoutProxy(false, true).setPullLabel("上拉获取更多...")
        sendList.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以更新")

        sendList.recyclerView.addItemDecoration(RecyclerDecoration(this,R.drawable.divider_send))
        sendList.recyclerView.layoutManager = LinearLayoutManager(this,OrientationHelper.VERTICAL,false)
        sendList.recyclerView.adapter = adapter

        adapter.addFirst(
                SendItem("-1","CimZzz",SendItem.ONLINE,"13:35","中国","15:40","日本",3.5f)
                ,SendItem("-2","TiwZzz",SendItem.ONLINE,"7:52","日本","15:40","中国",3.5f)

        )
    }
}