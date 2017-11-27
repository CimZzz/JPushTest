package com.example.yumi.jpushtest.ui.chat

import cn.jpush.im.android.api.model.Message
import com.example.yumi.jpushtest.base.IMethod
import com.example.yumi.jpushtest.base.IView
import com.example.yumi.jpushtest.entity.BaseChatItem


/**
 * Created by CimZzz(王彦雄) on ${Date}.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
interface IChatContract {
    interface View : IView {
        fun getOppositeUserName() : String
        fun getMyUserName() : String
        fun addMsg(msg : BaseChatItem)
        fun sendMessageResult(msgId : Int,result : Int)
        fun setDownloadVoiceResult(result:Int, msgId : Int, filePath : String)
    }

    interface Method : IMethod {
        
    }
}