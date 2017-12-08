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
import cn.jpush.im.api.BasicCallback
import com.example.yumi.jpushtest.base.IModule
import com.example.yumi.jpushtest.entity.BaseChatItem
import com.example.yumi.jpushtest.entity.ImageChatItem
import com.example.yumi.jpushtest.entity.TextChatItem
import com.example.yumi.jpushtest.entity.VoiceChatItem
import com.example.yumi.jpushtest.environment.IMCode
import com.example.yumi.jpushtest.utils.logV
import com.virtualightning.stateframework.state.StateRecord
import java.io.File

/**
 * Created by CimZzz(王彦雄) on 2017/12/1.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class IMModule(context: Context) : IModule(context) {
    companion object {
        val STATE_NEW_MESSAGE = "im0"
        val STATE_LOGIN = "im1"
        val STATE_LOGOUT = "im2"
        val STATE_LOGIN_FAILED = "im3"
    }

    val stateRecord = StateRecord.newInstance(IMModule::class.java)!!

    lateinit var curUserName : String
    var isOnline = false

    init {
        JMessageClient.init(context)
        JMessageClient.registerEventReceiver(this)
    }

    fun sendTextMsg(text:String,toSomeOne : String) : TextChatItem {
        val msg = JMessageClient.createSingleTextMessage(toSomeOne,text)
        val item = TextChatItem(
                msg.id,
                curUserName,
                toSomeOne,
                msg.createTime,
                BaseChatItem.STATUS_LOADING,
                text)
        JMessageClient.sendMessage(msg)

        return item
    }

    fun sendPicMsg(picPath : String,toSomeOne: String) : ImageChatItem {
        val msg = JMessageClient.createSingleImageMessage(toSomeOne, File(picPath))
        val convertMsg = ImageChatItem(
                msg.id,
                curUserName,
                toSomeOne,
                msg.createTime,
                BaseChatItem.STATUS_LOADING,
                picPath,
                BaseChatItem.DOWNLOAD_DOWNLOADED,
                0,
                "",
                0)
        JMessageClient.sendMessage(msg)
        return convertMsg
    }

    fun sendVoiceMsg(voicePath : String,second : Int,toSomeOne: String) : VoiceChatItem {
        val msg = JMessageClient.createSingleVoiceMessage(toSomeOne, File(voicePath),second)
        val convertMsg = VoiceChatItem(
                msg.id,
                curUserName,
                toSomeOne,
                msg.createTime,
                BaseChatItem.STATUS_LOADING,
                voicePath,
                second,
                false,
                BaseChatItem.DOWNLOAD_DOWNLOADED,
                0,
                "",
                0)
        JMessageClient.sendMessage(msg)
        return convertMsg
    }

    fun login() {
        if(isOnline) {
            StateRecord.notifyWholeState(STATE_LOGIN)
            return
        }
        JMessageClient.register(curUserName,"123456",object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if(p0 == IMCode.REGISTER_USER_EXIST || p0 == IMCode.SUCCESS) {
                    JMessageClient.login(curUserName,"123456",object : BasicCallback() {
                        override fun gotResult(p0: Int, p1: String?) {
                            if(p0 == IMCode.SUCCESS) {
                                StateRecord.notifyWholeState(STATE_LOGIN)
                                isOnline = true
                            }
                            else {
                                StateRecord.notifyWholeState(STATE_LOGIN_FAILED)
                                logV("p0:$p0,msg:$p1")
                            }
                        }
                    })
                }
                else {
                    StateRecord.notifyWholeState(STATE_LOGIN_FAILED)
                    logV("p0:$p0,msg:$p1")
                }
            }

        })
    }

    fun onEventMainThread(event: LoginStateChangeEvent) {
        when(event.reason) {
            LoginStateChangeEvent.Reason.user_logout-> {
                isOnline = false
                StateRecord.notifyWholeState(STATE_LOGOUT)
            }
        }
        logV("用户状态变化:${event.myInfo},${event.reason}")
    }

    fun onEventMainThread(event: MessageEvent) {
        receiverMessage(event.message)
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
                StateRecord.notifyWholeState(STATE_NEW_MESSAGE,convertMsg)
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
                StateRecord.notifyWholeState(STATE_NEW_MESSAGE,convertMsg)
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
                StateRecord.notifyWholeState(STATE_NEW_MESSAGE,convertMsg)
            }
        }
    }
}