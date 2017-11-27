package com.example.yumi.jpushtest.ui.chat

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.jpush.im.android.api.JMessageClient
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.entity.*
import com.example.yumi.jpushtest.utils.convert2TimeStr
import com.example.yumi.jpushtest.utils.isEmptyString
import com.example.yumi.jpushtest.utils.loadPic
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.widgets.ChatContainer
import com.example.yumi.jpushtest.widgets.ChatStatusView
import com.example.yumi.jpushtest.widgets.VoiceIconView

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TEXT_CHAT_ITEM: Int = 0
    val IMG_CHAT_ITEM: Int = 1
    val VOICE_CHAT_ITEM: Int = 2

    val msgArray : ArrayList<BaseItem> = ArrayList()
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

    val statusClick = View.OnClickListener {
        if(it is ChatStatusView) {
            if(it.curStatus == BaseChatItem.STATUS_FAILED) {
                listener?.onUploadFailedClick(it.tag as BaseChatItem)
            }
        }
    }

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is BaseChatHolder) {
            val item = msgArray[position] as BaseChatItem
            holder.userName.text = item.fromUser
            if(item.fromUser == "CimZzz")
                holder.headPic.setImageResource(R.drawable.icon_cat)
            else holder.headPic.setImageResource(R.drawable.icon_dog)

            holder.chatStatusView.setStatus(item.status)
            holder.chatStatusView.tag = item

            if(holder is TextChatHolder) {
                val concretedItem = item as TextChatItem
                holder.chatContainer.tag = concretedItem
                holder.textMsgView.text = concretedItem.message
            }
            else if (holder is ImageChatHolder) {
                val concretedItem = item as ImageChatItem
                holder.chatContainer.tag = concretedItem
                if(isEmptyString(concretedItem.imgLink)) {

                }
                else loadPic("file:///"+concretedItem.imgLink,holder.imgMsgView)
            }
            else if (holder is VoiceChatHolder) {
                val concretedItem = item as VoiceChatItem
                holder.chatContainer.tag = concretedItem
                holder.voiceTimeView.text = convert2TimeStr(concretedItem.second)
                if(item.isPlaying)
                    holder.voiceIconView.startPlaying()
                else holder.voiceIconView.stopPlaying()
            }
            holder.isOwn(JMessageClient.getMyInfo().userName == item.fromUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        var holder : RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == TEXT_CHAT_ITEM) {
            holder = TextChatHolder(inflater.inflate(R.layout.item_chat_text,parent,false))
            return holder
        }
        else if(viewType == IMG_CHAT_ITEM) {
            holder = ImageChatHolder(inflater.inflate(R.layout.item_chat_img,parent,false))
            return holder
        }
        else if(viewType == VOICE_CHAT_ITEM) {
            holder = VoiceChatHolder(inflater.inflate(R.layout.item_chat_voice,parent,false))
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

        return -1
    }

    abstract inner class BaseChatHolder(item: View) : RecyclerView.ViewHolder(item) {

        /*View inclusion relation*/
        /*
        * root -> headPic
        *      -> container -> userName
        *                   -> contentContainer -> chatContainer
        *                                       -> chatStatusView
        *
        * */

        val headPic : ImageView = item.findViewById(R.id.chatItemHeadPic)
        val userName : TextView = item.findViewById(R.id.chatItemUserName)
        val chatContainer: ChatContainer = item.findViewById(R.id.chatItemChatContainer)
        protected val contentContainer: LinearLayout = item.findViewById(R.id.chatItemContentContainer)
        protected val container: LinearLayout = item.findViewById(R.id.chatItemContainer)
        val chatStatusView : ChatStatusView = item.findViewById(R.id.chatItemStatus)
        var isOwn = false

        init {
            chatContainer.setOnClickListener(contentClick)
            chatStatusView.setOnClickListener(statusClick)
        }

        open fun isOwn(isOwn : Boolean) {
            if(this.isOwn == isOwn)
                return
            this.isOwn = isOwn
            val root = itemView as ViewGroup
            root.removeAllViews()
            contentContainer.removeAllViews()

            if(!isOwn) {
                container.gravity = Gravity.START
                chatContainer.setSide(true)
                contentContainer.addView(chatContainer)
                contentContainer.addView(chatStatusView)
                root.addView(headPic)
                root.addView(container)
            } else {
                container.gravity = Gravity.END
                chatContainer.setSide(false)
                contentContainer.addView(chatStatusView)
                contentContainer.addView(chatContainer)
                root.addView(container)
                root.addView(headPic)
            }
        }
    }

    inner class TextChatHolder(item : View) : BaseChatHolder(item) {
        val textMsgView: TextView = item.findViewById(R.id.chatItemText)

    }

    inner class ImageChatHolder(item : View) : BaseChatHolder(item) {
        val imgMsgView: ImageView = item.findViewById(R.id.chatItemImage)

    }

    inner class VoiceChatHolder(item : View) : BaseChatHolder(item) {
        val outContainer : ViewGroup = item.findViewById(R.id.chatItemChatOutContainer)
        val voiceIconView : VoiceIconView = item.findViewById(R.id.chatItemVoice)
        val voiceTimeView : TextView = item.findViewById(R.id.chatItemVoiceTime)

        override fun isOwn(isOwn: Boolean) {
            if(this.isOwn == isOwn)
                return
            this.isOwn = isOwn
            val root = itemView as ViewGroup
            root.removeAllViews()
            contentContainer.removeAllViews()
            outContainer.removeAllViews()

            if(!isOwn) {
                container.gravity = Gravity.START
                chatContainer.gravity = Gravity.START
                chatContainer.setSide(true)
                voiceIconView.setSide(true)
                outContainer.addView(chatContainer)
                outContainer.addView(voiceTimeView)
                contentContainer.addView(outContainer)
                contentContainer.addView(chatStatusView)
                root.addView(headPic)
                root.addView(container)
            } else {
                container.gravity = Gravity.END
                chatContainer.gravity = Gravity.END
                chatContainer.setSide(false)
                voiceIconView.setSide(false)
                outContainer.addView(voiceTimeView)
                outContainer.addView(chatContainer)
                contentContainer.addView(chatStatusView)
                contentContainer.addView(outContainer)
                root.addView(container)
                root.addView(headPic)
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