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
import com.example.yumi.jpushtest.entity.BaseChatItem
import com.example.yumi.jpushtest.entity.BaseItem
import com.example.yumi.jpushtest.entity.ImageChatItem
import com.example.yumi.jpushtest.entity.TextChatItem
import com.example.yumi.jpushtest.utils.loadPic
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.widgets.ChatContainer

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TEXT_CHAT_ITEM: Int = 0
    val IMG_CHAT_ITEM: Int = 1

    val msgArray : ArrayList<BaseItem> = ArrayList()
    val msgUniqueSet : HashSet<Int> = HashSet()
    var listener : IContentClickListener? = null

    val headPicClick = View.OnClickListener {

    }

    val contentClick = View.OnClickListener {
        logV("OnClick")
        val item = it.tag
        if(item is ImageChatItem) {
            listener?.onImageContentClick(item.imgLink,it)
        }
    }

    fun addMsg(msg : BaseChatItem) {
        msgArray.add(msg)
        notifyItemRangeInserted(msgArray.size,1)
    }

    override fun getItemCount(): Int = msgArray.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is BaseChatHolder) {
            val item = msgArray[position] as BaseChatItem
            holder.userName.text = item.fromUser
            if(item.fromUser == "CimZzz")
                holder.headPic.setImageResource(R.drawable.icon_cat)
            else holder.headPic.setImageResource(R.drawable.icon_dog)


            if(holder is TextChatHolder) {
                val concretedItem = item as TextChatItem
                holder.contentContainer.tag = concretedItem
                holder.textMsgView.text = concretedItem.message
            }
            else if (holder is ImageChatHolder) {
                val concretedItem = item as ImageChatItem
                holder.contentContainer.tag = concretedItem
                loadPic("file:///"+concretedItem.imgLink,holder.imgMsgView)
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

        return null
    }

    override fun getItemViewType(position: Int): Int {
        val item = msgArray[position]
        if(item is TextChatItem)
            return TEXT_CHAT_ITEM
        else if(item is ImageChatItem)
            return IMG_CHAT_ITEM

        return -1
    }

    abstract inner class BaseChatHolder(item: View) : RecyclerView.ViewHolder(item) {
        val headPic : ImageView = item.findViewById(R.id.chatItemHeadPic)
        val userName : TextView = item.findViewById(R.id.chatItemUserName)
        private val container: LinearLayout = item.findViewById(R.id.chatItemContainer)
        val contentContainer: ChatContainer = item.findViewById(R.id.chatItemContentContainer)
        var isOwn = false

        init {
            contentContainer.setOnClickListener(contentClick)
        }

        open fun isOwn(isOwn : Boolean) {
            if(this.isOwn == isOwn)
                return
            this.isOwn = isOwn
            val root = itemView as ViewGroup
            root.removeAllViews()

            if(!isOwn) {
                root.addView(headPic)
                container.gravity = Gravity.START
                contentContainer.setSide(true)
                root.addView(container)
            } else {
                container.gravity = Gravity.END
                contentContainer.setSide(false)
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

    interface IContentClickListener {
        fun onTextContentClick(text : String)
        fun onImageContentClick(imgPath : String,view : View)
        fun onVoiceContentClick(voicePath : String)
    }
}