package com.example.yumi.jpushtest.ui.chat2

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.base.BaseUI
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.entity.*
import com.example.yumi.jpushtest.environment.config.getIMModule
import com.handmark.pulltorefresh.library.PullToRefreshBase
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater
import com.virtualightning.stateframework.state.StateRecord
import kotlinx.android.synthetic.main.actionbar_user_back.*
import kotlinx.android.synthetic.main.ui_chat.*

/**
 * Created by CimZzz(王彦雄) on 2017/12/1.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ChatUI2 : BaseUI<ChatPresenter>() {
    companion object {
        val KV_OPPOSITE_USER_NAME = "OppositeUserName"
        val KV_OPPOSITE_HEAD_PIC = "OppositeHeadPic"
        val KV_OPPOSITE_USER_STATUS = "OppositeUserStatus"
    }
    val stateRecord : StateRecord = StateRecord.newInstance(ChatUI2::class.java)

    lateinit var oppositeUserName : String
    lateinit var headPic : String
    lateinit var adapter : ChatAdapter2

    var isOnline = false


    override fun onBaseUICreate(creater: ActionBarUICreater) {
        creater.setLayoutID(R.layout.ui_chat)
        creater.setActionBarID(R.layout.actionbar_user_back)
        openAutoCancelSoft = true
    }

    override fun onViewInit(savedInstanceState: Bundle?) {
        oppositeUserName = intent.getStringExtra(KV_OPPOSITE_USER_NAME)?:"TiwZzz"
        headPic = intent.getStringExtra(KV_OPPOSITE_HEAD_PIC)?:""
        isOnline = intent.getBooleanExtra(KV_OPPOSITE_USER_STATUS,true)

        getIMModule().curUserName = "CimZzz"
        adapter = ChatAdapter2(getIMModule().curUserName)

        actionbarBack.setOnClickListener {
            finish()
        }
        actionbarHeadPic.setImageResource(R.drawable.icon_dog)
        actionbarUserName.text = oppositeUserName

        if(isOnline) {
            actionbarStatus.color = resources.getColor(R.color.actionbar_OnlineColor)
            actionbarStatusText.setTextColor(resources.getColor(R.color.actionbar_OnlineTextColor))
        } else {
            actionbarStatus.color = resources.getColor(R.color.actionbar_OfflineColor)
            actionbarStatusText.setTextColor(resources.getColor(R.color.actionbar_OfflineTextColor))
        }


        chatList.mode = PullToRefreshBase.Mode.BOTH
        chatList.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在获取历史记录...")
        chatList.getLoadingLayoutProxy(true, false).setPullLabel("下拉获取历史记录...")
        chatList.getLoadingLayoutProxy(true, false).setReleaseLabel("松开以更新")
        chatList.recyclerView.layoutManager = LinearLayoutManager(this,OrientationHelper.VERTICAL,false)
        chatList.setAdapter(adapter)

        adapter.addMsg(TextChatItem(0,"CimZzz","TiwZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"12312312"))
        adapter.addMsg(TextChatItem(0,"TiwZzz","CimZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"12312312"))

        adapter.addMsg(ImageChatItem(0,"CimZzz","TiwZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=770964228,1616561111&fm=58&u_exp_0=203930613,1283255701&fm_exp_0=86&bpow=512&bpoh=512",BaseChatItem.DOWNLOAD_NOT_DOWNLOAD,10000,"0",11))
        adapter.addMsg(ImageChatItem(0,"TiwZzz","CimZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3546333755,3846203594&fm=27&gp=0.jpg",BaseChatItem.DOWNLOAD_NOT_DOWNLOAD,10000,"0",11))

        adapter.addMsg(VoiceChatItem(0,"CimZzz","TiwZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"",2,false,BaseChatItem.DOWNLOAD_NOT_DOWNLOAD,1,"1",1))
        adapter.addMsg(VoiceChatItem(0,"TiwZzz","CimZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"",12,false,BaseChatItem.DOWNLOAD_NOT_DOWNLOAD,1,"1",1))

        adapter.addMsg(OrderChatItem(0,"CimZzz","TiwZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"sadasdsad",false))
        adapter.addMsg(OrderChatItem(0,"CimZzz","TiwZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"sadasdsad",true))
        adapter.addMsg(OrderChatItem(0,"TiwZzz","CimZzz",System.currentTimeMillis(),BaseChatItem.STATUS_NORMAL,"sadasdsad",false))
    }

}