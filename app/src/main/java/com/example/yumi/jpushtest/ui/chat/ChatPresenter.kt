package com.example.yumi.jpushtest.ui.chat

import cn.jiguang.api.JCoreInterface
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.DownloadCompletionCallback
import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.content.VoiceContent
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.api.BasicCallback
import com.example.yumi.jpushtest.base.IPresenter
import com.example.yumi.jpushtest.entity.BaseChatItem
import com.example.yumi.jpushtest.entity.ImageChatItem
import com.example.yumi.jpushtest.entity.TextChatItem
import com.example.yumi.jpushtest.entity.VoiceChatItem
import com.example.yumi.jpushtest.utils.logV
import java.io.File

/**
 * Created by CimZzz(王彦雄) on ${Date}.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ChatPresenter(view: IChatContract.View, method: IChatContract.Method) : IPresenter<IChatContract.View, IChatContract.Method>(view, method) {
    companion object {
        val RES_SUCCESS = 0
        val RES_FAILED = 1
        val RES_INVALID = 2
    }

    init {
        JMessageClient.registerEventReceiver(this)
    }

    override fun onDestroy() {
        JMessageClient.unRegisterEventReceiver(this)
    }

    fun sendTextMsg(text : String) {
        val msg = JMessageClient.createSingleTextMessage(view.getOppositeUserName(),text)
        val convertMsg = TextChatItem(
                msg.id,
                view.getMyUserName(),
                view.getOppositeUserName(),
                msg.createTime,
                BaseChatItem.STATUS_LOADING,
                text)
        view.addMsg(convertMsg)
        msg.setOnSendCompleteCallback(object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if(p0 != 0)
                    view.sendToast("消息发送失败")
                view.sendMessageResult(msg.id,p0 == 0)
            }
        })
        JMessageClient.sendMessage(msg)
    }

    fun sendPicMsg(picPath : String) {
        val msg = JMessageClient.createSingleImageMessage(view.getOppositeUserName(), File(picPath))
        val convertMsg = ImageChatItem(
                msg.id,
                view.getMyUserName(),
                view.getOppositeUserName(),
                msg.createTime,
                BaseChatItem.STATUS_LOADING,
                picPath,
                BaseChatItem.DOWNLOAD_DOWNLOADED,
                0,
                "",
                0)
        view.addMsg(convertMsg)
        msg.setOnSendCompleteCallback(object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if(p0 != 0)
                    view.sendToast("发送失败")
                view.sendMessageResult(msg.id,p0 == 0)
            }
        })
        JMessageClient.sendMessage(msg)
    }

    fun sendVoiceMsg(voicePath : String,second : Int) {
        val msg = JMessageClient.createSingleVoiceMessage(view.getOppositeUserName(), File(voicePath),second)
        val convertMsg = VoiceChatItem(
                msg.id,
                view.getMyUserName(),
                view.getOppositeUserName(),
                msg.createTime,
                BaseChatItem.STATUS_LOADING,
                voicePath,
                second,
                false,
                BaseChatItem.DOWNLOAD_DOWNLOADED,
                0,
                "",
                0)
        view.addMsg(convertMsg)
        msg.setOnSendCompleteCallback(object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if(p0 != 0)
                    view.sendToast("发送失败")
                view.sendMessageResult(msg.id,p0 == 0)
            }
        })
        JMessageClient.sendMessage(msg)
    }

    fun downloadVoice(msgId : Int,mediaId : String,mediaCrc : Long) {
        val content = VoiceContent.fromJson("{\"media_crc32\":$mediaCrc,\"media_id\":\"$mediaId\"}", ContentType.voice) as VoiceContent
        val msg = Conversation.createSingleConversation(view.getMyUserName(), JCoreInterface.getAppKey()).createSendMessage(content)
        content.downloadVoiceFile(msg,object : DownloadCompletionCallback() {
            override fun onComplete(p0: Int, p1: String?, p2: File?) {
                view.sendToast("Download : $p0,$p1")
                if(p0 == 0)
                    view.setDownloadVoiceResult(RES_SUCCESS,msgId,p2!!.absolutePath)
                else view.setDownloadVoiceResult(RES_FAILED,msgId,"")
            }
        })
    }

    fun onEventMainThread(event: MessageEvent) {
        val msg = event.message
        logV("msgId : ${msg.id}")

        when(msg.contentType) {
            ContentType.text-> {
                val convertMsg = TextChatItem(
                        msg.id,
                        msg.fromUser.userName,
                        view.getMyUserName(),
                        msg.createTime,
                        BaseChatItem.STATUS_NORMAL,
                        (msg.content as TextContent).text
                )
                view.addMsg(convertMsg)
            }
            ContentType.image-> {
                val content = msg.content as ImageContent
                val convertMsg = ImageChatItem(
                        msg.id,
                        msg.fromUser.userName,
                        view.getMyUserName(),
                        msg.createTime,
                        BaseChatItem.STATUS_NORMAL,
                        content.localThumbnailPath,
                        BaseChatItem.DOWNLOAD_NOT_DOWNLOAD,
                        content.crc,
                        content.mediaID,
                        content.fileSize
                )
                view.addMsg(convertMsg)
            }
            ContentType.voice-> {
                val content = msg.content as VoiceContent
                val convertMsg = VoiceChatItem(
                        msg.id,
                        msg.fromUser.userName,
                        view.getMyUserName(),
                        msg.createTime,
                        BaseChatItem.STATUS_NORMAL,
                        "",
                        content.duration,
                        false,
                        BaseChatItem.DOWNLOAD_NOT_DOWNLOAD,
                        content.crc,
                        content.mediaID,
                        content.fileSize)
                view.addMsg(convertMsg)
            }
        }
    }
}