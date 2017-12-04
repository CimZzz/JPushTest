package com.example.yumi.jpushtest.ui.chat2

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.entity.*
import com.example.yumi.jpushtest.utils.convert2TimeStr
import com.example.yumi.jpushtest.utils.loadPic
import com.example.yumi.jpushtest.widgets.ChatContainer2
import com.example.yumi.jpushtest.widgets.VoiceIconView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class ChatAdapter2(val curUserName : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TEXT_CHAT_ITEM: Int = 0
    val IMG_CHAT_ITEM: Int = 1
    val VOICE_CHAT_ITEM: Int = 2
    val OREDER_CHAT_ITEM: Int = 3

    val msgArray : ArrayList<BaseItem> = ArrayList()
    val dateFormat = SimpleDateFormat("h:MM a")
    var listener : IContentClickListener? = null

    val headPicClick = View.OnClickListener {

    }

    val contentClick = View.OnClickListener {
        val item = it.tag
        if(item is ImageChatItem) {
            listener?.onImageContentClick(item,it)
        }
        else if(item is VoiceChatItem) {
            listener?.onVoiceContentClick(item,it.findViewById(R.id.chatItemVoice))
        }
    }

//    val statusClick = View.OnClickListener {
//        if(it is ChatStatusView) {
//            if(it.curStatus == BaseChatItem.STATUS_FAILED) {
//                listener?.onUploadFailedClick(it.tag as BaseChatItem)
//            }
//        }
//    }

    fun addMsg(msg : BaseChatItem) {
        msgArray.add(msg)
        notifyItemRangeInserted(msgArray.size,1)
    }

    fun removeMsg(msgId : Int) {
        var index = -1
        for(i in msgArray.size-1 downTo 0) {
            val item = msgArray[i]
            if(item is BaseChatItem) {
                index = i
            }
        }
        if(index != -1) {
            msgArray.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updateMsgStatus(id : Int,status : Int) {
        var index = -1
        for(i in msgArray.size-1 downTo 0) {
            val item = msgArray[i]
            if(item is BaseChatItem) {
                if(item.msgId == id) {
                    index = i
                    item.status = status
                    break
                }
            }
        }
        if(index != -1)
            notifyItemChanged(index)
    }

    fun updateImagePath(id : Int,imgLink : String) {
        msgArray.forEach {
            if(it is ImageChatItem) {
                if(it.msgId == id) {
                    it.downloadFlag = BaseChatItem.DOWNLOAD_DOWNLOADED
                    it.imgLink = imgLink
                    return@forEach
                }
            }
        }
    }

    fun updateVoicePath(id : Int,voiceLink : String) {
        msgArray.forEach {
            if(it is VoiceChatItem) {
                if(it.msgId == id) {
                    it.downloadFlag = BaseChatItem.DOWNLOAD_DOWNLOADED
                    it.voiceLink = voiceLink
                    return@forEach
                }
            }
        }
    }

    fun updateVoicePlayStatus(id : Int,isPlaying : Boolean) {
        var index = -1
        for(i in msgArray.size-1 downTo 0) {
            val item = msgArray[i]
            if(item is VoiceChatItem) {
                if(item.msgId == id) {
                    item.isPlaying = isPlaying
                    index = i
                    break
                }
            }
        }
        if(index != -1)
            notifyItemChanged(index)
    }

    override fun getItemCount(): Int = msgArray.size

//    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is BaseChatHolder) {
            val item = msgArray[position] as BaseChatItem
            if(item.fromUser == "CimZzz")
                holder.headPic.setImageResource(R.drawable.icon_cat)
            else holder.headPic.setImageResource(R.drawable.icon_dog)

            holder.chatTime.text = dateFormat.format(item.createTime)

            if(holder is TextChatHolder) {
                val concretedItem = item as TextChatItem
                holder.chatContainer.tag = concretedItem
                holder.textMsgView.text = concretedItem.message
            }
            else if (holder is ImageChatHolder) {
                val concretedItem = item as ImageChatItem
                holder.chatContainer.tag = concretedItem
                loadPic(item.imgLink,holder.imageMsgView)
            }
            else if(holder is VoiceChatHolder) {
                val concretedItem = item as VoiceChatItem
                holder.voiceSecond.text = convert2TimeStr(concretedItem.second)
            }
            else if(holder is OrderChatHolder) {
                val concretedItem = item as OrderChatItem
                if(curUserName == item.fromUser) {
                    holder.orderTitle.text = "${concretedItem.fromUser}的寄件单"
                    holder.orderCheckTips.text = "寄件单"
                    if(concretedItem.isRead)
                        holder.readTips.visibility = View.VISIBLE
                    else holder.readTips.visibility = View.GONE
                } else {
                    holder.orderTitle.text = "${concretedItem.fromUser}发来了寄件单"
                    holder.orderCheckTips.text = "请查看${concretedItem.fromUser}的寄件单"
                    holder.readTips.visibility = View.GONE
                }
            }
            holder.isOwn(curUserName == item.fromUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val holder : RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == TEXT_CHAT_ITEM) {
            holder = TextChatHolder(inflater.inflate(R.layout.item_chat_text2,parent,false),
                    parent.context.resources.getColor(R.color.chat_ItemOwnTextColor),
                    parent.context.resources.getColor(R.color.chat_ItemOppositeTextColor))
            return holder
        }
        else if(viewType == IMG_CHAT_ITEM) {
            holder = ImageChatHolder(inflater.inflate(R.layout.item_chat_img2,parent,false))
            return holder
        }
        else if(viewType == VOICE_CHAT_ITEM) {
            holder = VoiceChatHolder(inflater.inflate(R.layout.item_chat_voice2,parent,false),
                    parent.context.resources.getColor(R.color.chat_ItemOwnTextColor),
                    parent.context.resources.getColor(R.color.chat_ItemOppositeTextColor))
            return holder
        }
        else if(viewType == OREDER_CHAT_ITEM) {
            holder = OrderChatHolder(inflater.inflate(R.layout.item_chat_order,parent,false),
                    parent.context.resources.getColor(R.color.chat_ItemOwnTextColor),
                    parent.context.resources.getColor(R.color.chat_ItemOppositeTextColor))
            return holder
        }

        return null
    }

    override fun getItemViewType(position: Int): Int {
        val item = msgArray[position]
        if(item is TextChatItem)
            return TEXT_CHAT_ITEM
        else if(item is ImageChatItem)
            return IMG_CHAT_ITEM
        else if(item is VoiceChatItem)
            return VOICE_CHAT_ITEM
        else if(item is OrderChatItem)
            return OREDER_CHAT_ITEM

        return -1
    }

    abstract inner class BaseChatHolder(item: View) : RecyclerView.ViewHolder(item) {

        /*View inclusion relation*/
        /*
        * root -> headPic
        *      -> chatContainer -> anyThing
        * */

        val headPic : ImageView = item.findViewById(R.id.chatItemHeadPic)
        val chatContainer: ChatContainer2 = item.findViewById(R.id.chatItemContainer)
        val chatTime : TextView = item.findViewById(R.id.chatItemTime)
        var isOwn = false

        init {
            chatContainer.setOnClickListener(contentClick)
        }

        open fun isOwn(isOwn : Boolean) {
            if(this.isOwn == isOwn)
                return
            this.isOwn = isOwn

            val root = itemView as LinearLayout
            root.removeAllViews()

            if(!isOwn) {
                root.gravity = Gravity.START
                chatContainer.setSide(true)
                onChatContainerReverse(chatContainer,isOwn)
                root.addView(headPic)
                root.addView(chatTime)
                root.addView(chatContainer)
            } else {
                root.gravity = Gravity.END
                chatContainer.setSide(false)
                onChatContainerReverse(chatContainer,isOwn)
                root.addView(chatTime)
                root.addView(chatContainer)
                root.addView(headPic)
            }
        }

        open fun onChatContainerReverse(chatContainer: ChatContainer2,isOwn: Boolean) {

        }
    }

    inner class TextChatHolder(item : View,val ownColor : Int,val oppsiteColor : Int) : BaseChatHolder(item) {
        val textMsgView: TextView = item.findViewById(R.id.chatItemText)
        override fun onChatContainerReverse(chatContainer: ChatContainer2, isOwn: Boolean) {
            if(isOwn)
                textMsgView.setTextColor(ownColor)
            else textMsgView.setTextColor(oppsiteColor)
        }
    }

    inner class ImageChatHolder(item : View) : BaseChatHolder(item) {
        val imageMsgView: ImageView = item.findViewById(R.id.chatItemImage)
    }

    inner class VoiceChatHolder(item : View,val ownColor : Int,val oppsiteColor : Int) : BaseChatHolder(item) {
        val voiceSecond : TextView = item.findViewById(R.id.chatItemVoiceTime)
        val voiceTips : TextView = item.findViewById(R.id.chatItemVoiceTips)
        val voiceBtn : ImageView = item.findViewById(R.id.chatItemVoiceBtn)

        override fun onChatContainerReverse(chatContainer: ChatContainer2, isOwn: Boolean) {
            if(isOwn) {
                voiceTips.setTextColor(ownColor)
                voiceSecond.setTextColor(ownColor)
            } else {
                voiceTips.setTextColor(oppsiteColor)
                voiceSecond.setTextColor(oppsiteColor)
            }
        }
    }

    inner class OrderChatHolder(item: View,val ownColor : Int,val oppsiteColor : Int) : BaseChatHolder(item) {
        val orderTitle : TextView = item.findViewById(R.id.chatItemOrderTitle)
        val orderCheckTips : TextView = item.findViewById(R.id.chatItemOrderCheckTips)
        val readTips : TextView = item.findViewById(R.id.chatItemOrderReadTips)

        override fun isOwn(isOwn: Boolean) {
            super.isOwn(isOwn)

            if(isOwn) {
                orderTitle.setTextColor(ownColor)
                orderCheckTips.setTextColor(ownColor)
                readTips.setTextColor(ownColor)
            } else {
                orderTitle.setTextColor(oppsiteColor)
                orderCheckTips.setTextColor(oppsiteColor)
                readTips.setTextColor(oppsiteColor)
            }
        }
    }

    interface IContentClickListener {
        fun onTextContentClick(text : String)
        fun onImageContentClick(item : ImageChatItem,view : View)
        fun onVoiceContentClick(item : VoiceChatItem,view : VoiceIconView)
        fun onUploadFailedClick(item : BaseChatItem)
    }
}