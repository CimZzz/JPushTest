package com.example.yumi.jpushtest.environment.module

import android.content.Context
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.content.VoiceContent
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.event.LoginStateChangeEvent
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.event.OfflineMessageEvent
import cn.jpush.im.android.api.model.Message
import com.example.yumi.jpushtest.base.IModule
import com.example.yumi.jpushtest.entity.BaseChatItem
import com.example.yumi.jpushtest.entity.ImageChatItem
import com.example.yumi.jpushtest.entity.TextChatItem
import com.example.yumi.jpushtest.entity.VoiceChatItem

/**
 * Created by CimZzz(王彦雄) on 2017/12/1.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class IMModule(context: Context) : IModule(context) {
    lateinit var curUserName : String
    var isRegister = false
    var isLogin = false

    init {
        JMessageClient.init(context)
        JMessageClient.registerEventReceiver(this)
    }

    fun onEventMainThread(event: MessageEvent) {
        receiverMessage(event.message)
    }

    fun onEventMainThread(event: LoginStateChangeEvent) {

    }

    fun onEventMainThread(event: OfflineMessageEvent) {
        event.offlineMessageList.forEach {
            receiverMessage(it)
        }
    }

    private fun receiverMessage(msg: Message) {
        when(msg.contentType) {
            ContentType.text -> {
                val convertMsg = TextChatItem(
                        msg.id,
                        msg.fromUser.userName,
                        curUserName,
                        msg.createTime,
                        BaseChatItem.STATUS_NORMAL,
                        (msg.content as TextContent).text
                )
            }
            ContentType.image -> {
                val content = msg.content as ImageContent
                val convertMsg = ImageChatItem(
                        msg.id,
                        msg.fromUser.userName,
                        curUserName,
                        msg.createTime,
                        BaseChatItem.STATUS_NORMAL,
                        if (content.localThumbnailPath == null) "" else content.localThumbnailPath,
                        if (content.localThumbnailPath == null) BaseChatItem.DOWNLOAD_NOT_DOWNLOAD else BaseChatItem.DOWNLOAD_NO_THUMBNAIL,
                        content.crc,
                        content.mediaID,
                        content.fileSize
                )
            }
            ContentType.voice -> {
                val content = msg.content as VoiceContent
                val convertMsg = VoiceChatItem(
                        msg.id,
                        msg.fromUser.userName,
                        curUserName,
                        msg.createTime,
                        BaseChatItem.STATUS_NORMAL,
                        "",
                        content.duration,
                        false,
                        BaseChatItem.DOWNLOAD_NOT_DOWNLOAD,
                        content.crc,
                        content.mediaID,
                        content.fileSize)
            }
        }
    }
}