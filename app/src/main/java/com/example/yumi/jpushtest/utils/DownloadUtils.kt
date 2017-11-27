package com.example.yumi.jpushtest.utils

import cn.jiguang.api.JCoreInterface
import cn.jpush.im.android.api.callback.DownloadCompletionCallback
import cn.jpush.im.android.api.content.ImageContent
import cn.jpush.im.android.api.content.VoiceContent
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.model.Conversation
import java.io.File

/**
 * Created by CimZzz(王彦雄) on 2017/11/27.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

fun downloadOriginImage(userName: String, mediaId : String, mediaCrc : Long, body : (Int, String?, File?)->Unit) {
    val content = ImageContent.fromJson("{\"media_crc32\":$mediaCrc,\"media_id\":\"$mediaId\"}", ContentType.image) as ImageContent
    val msg = Conversation.createSingleConversation(userName, JCoreInterface.getAppKey()).createSendMessage(content)
    content.downloadOriginImage(msg,object : DownloadCompletionCallback() {
        override fun onComplete(p0: Int, p1: String?, p2: File?) {
            body.invoke(p0,p1,p2)
        }
    })
}

fun downloadVoice(userName: String, mediaId : String, mediaCrc : Long, body : (Int, String?, File?)->Unit) {
    val content = VoiceContent.fromJson("{\"media_crc32\":$mediaCrc,\"media_id\":\"$mediaId\"}", ContentType.voice) as VoiceContent
    val msg = Conversation.createSingleConversation(userName, JCoreInterface.getAppKey()).createSendMessage(content)
    content.downloadVoiceFile(msg,object : DownloadCompletionCallback() {
        override fun onComplete(p0: Int, p1: String?, p2: File?) {
            body.invoke(p0,p1,p2)
        }

    })
}