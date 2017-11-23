package com.example.yumi.jpushtest.entity

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
open class BaseItem()

abstract class BaseChatItem (
        val msgId:Int,
        val fromUser : String,
        val toUser : String,
        val createTime : Long,
        val isSuccessSend : Boolean
) : BaseItem()

class TextChatItem(
        msgId : Int,
        fromUser : String,
        toUser : String,
        createTime : Long,
        isSuccessSend : Boolean,
        val message: String
) : BaseChatItem(msgId,fromUser,toUser,createTime,isSuccessSend)

class ImageChatItem(
        msgId : Int,
        fromUser : String,
        toUser : String,
        createTime : Long,
        isSuccessSend : Boolean,
        val imgLink : String
) : BaseChatItem(msgId,fromUser,toUser,createTime,isSuccessSend)