package com.example.yumi.jpushtest.ui.chat

import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.api.BasicCallback
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.entity.ImageChatItem
import com.example.yumi.jpushtest.entity.TextChatItem
import com.example.yumi.jpushtest.utils.logV
import java.io.File

/**
 * Created by CimZzz(王彦雄) on ${Date}.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ChatPresenter(view: IChatContract.View, method: IChatContract.Method) : IPresenter<IChatContract.View, IChatContract.Method>(view, method) {

    init {
        JMessageClient.registerEventReceiver(this)
    }

    override fun onDestroy() {
        JMessageClient.unRegisterEventReceiver(this)
    }

    fun sendTextMsg() {
        val textMsg = view.getTextMsg()
        val msg = JMessageClient.createSingleTextMessage(view.getOppositeUserName(),textMsg)
//        val convertMsg = TextChatItem(
//                msg.id,
//                view.getMyUserName(),
//                view.getOppositeUserName(),
//                msg.createTime,
//                textMsg)
//        view.addMsg(convertMsg)
        msg.setOnSendCompleteCallback(object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if(p0 != 0)
                    view.sendToast("发送失败")
//                view.sendMessageResult(msg.id,p0 == 0)
            }
        })
        JMessageClient.sendMessage(msg)
    }

    fun sendPicMsg(picPath : String) {
        val msg = JMessageClient.createSingleImageMessage(view.getOppositeUserName(), File(picPath))
        msg.setOnSendCompleteCallback(object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if(p0 != 0)
                    view.sendToast("发送失败")
            }
        })
        JMessageClient.sendMessage(msg)
    }

    fun onEventMainThread(event: MessageEvent) {
        val msg = event.message
        when(msg.contentType) {
            ContentType.text-> {
                val convertMsg = TextChatItem(
                        msg.id,
                        msg.fromUser.userName,
                        view.getMyUserName(),
                        msg.createTime,
                        true,
                        (msg.content as TextContent).text
                )
                view.addMsg(convertMsg)
            }
            ContentType.image-> {
                val content = msg.content as ImageContent

                logV("imageLink : " + content.localPath)
                logV("imageLink : " + content.localThumbnailPath)
                logV("imageLink : " + content.img_link)

                val convertMsg = ImageChatItem(
                        msg.id,
                        msg.fromUser.userName,
                        view.getMyUserName(),
                        msg.createTime,
                        true,
                        content.localThumbnailPath
                )
                view.addMsg(convertMsg)
            }
        }
    }
}