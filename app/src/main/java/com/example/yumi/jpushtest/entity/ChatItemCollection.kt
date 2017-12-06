package com.example.yumi.jpushtest.entity

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
open class BaseItem

abstract class BaseChatItem (
        val msgId:Int,
        val fromUser : String,
        val toUser : String,
        val createTime : Long,
        var status : Int
) : BaseItem() {
    companion object {
        val STATUS_NORMAL = 0
        val STATUS_LOADING = 1
        val STATUS_FAILED = 2

        val DOWNLOAD_DOWNLOADED = 0
        val DOWNLOAD_NOT_DOWNLOAD = 1
        val DOWNLOAD_INVALID = 2
        val DOWNLOAD_NO_THUMBNAIL = 3

        val TYPE_TEXT = 0
        val TYPE_IMG = 1
        val TYPE_VOICE = 2
        val TYPE_ORDER = 3
    }
}



class TextChatItem(
        msgId : Int,
        fromUser : String,
        toUser : String,
        createTime : Long,
        status : Int,
        val message: String
) : BaseChatItem(msgId,fromUser,toUser,createTime,status)


class ImageChatItem(
        msgId : Int,
        fromUser : String,
        toUser : String,
        createTime : Long,
        status : Int,
        var imgLink : String,
        var downloadFlag : Int,
        val media_crc32 : Long,
        val media_id : String,
        val fileSize : Long
) : BaseChatItem(msgId,fromUser,toUser,createTime,status)

class VoiceChatItem(
        msgId : Int,
        fromUser : String,
        toUser : String,
        createTime : Long,
        status : Int,
        var voiceLink : String,
        var second : Int,
        var isPlaying : Boolean,
        var downloadFlag : Int,
        val media_crc32 : Long,
        val media_id : String,
        val fileSize : Long
) : BaseChatItem(msgId,fromUser,toUser,createTime,status)

class OrderChatItem(
        msgId : Int,
        fromUser : String,
        toUser : String,
        createTime : Long,
        status : Int,
        var orderId : String,
        var isRead : Boolean
) : BaseChatItem(msgId,fromUser,toUser,createTime,status)