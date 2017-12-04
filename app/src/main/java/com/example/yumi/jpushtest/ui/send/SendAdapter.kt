package com.example.yumi.jpushtest.ui.send

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.entity.SendItem
import com.example.yumi.jpushtest.utils.loadPic
import com.example.yumi.jpushtest.widgets.CircleView

/**
 * Created by CimZzz(王彦雄) on 2017/11/30.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class SendAdapter : RecyclerView.Adapter<SendAdapter.SendHolder>() {
    val sendItemList = ArrayList<SendItem>()
    lateinit var listener : View.OnClickListener

    fun addFirst(vararg items : SendItem) {
        sendItemList.addAll(0, items.toList())
    }

    fun addLast(vararg items : SendItem) {
        sendItemList.addAll(items.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendHolder
    = SendHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_send,parent,false))

    override fun onBindViewHolder(holder: SendHolder, position: Int) {
        val item = sendItemList[position]
        if(item.headerPic == "-1")
            holder.headerPic.setImageResource(R.drawable.icon_cat)
        if(item.headerPic == "-2")
            holder.headerPic.setImageResource(R.drawable.icon_dog)
        else loadPic(item.headerPic,holder.headerPic)
        holder.userName.text = item.userName
        when(item.userStatus) {
            SendItem.ONLINE-> {
                val res = holder.itemView.resources
                val statusColor = res.getColor(R.color.send_ItemOnlineColor)
                val textColor = res.getColor(R.color.send_ItemOnlineTextColor)
                holder.userStatusText.text = "在线"
                holder.userStatusText.setTextColor(textColor)
                holder.userStatus.color = statusColor
            }
            SendItem.OFFLINE-> {
                val res = holder.itemView.resources
                val statusColor = res.getColor(R.color.send_ItemOfflineColor)
                val textColor = res.getColor(R.color.send_ItemOfflineTextColor)
                holder.userStatusText.text = "离线"
                holder.userStatusText.setTextColor(textColor)
                holder.userStatus.color = statusColor
            }
        }
        holder.fromAddress.text = item.fromAddress
        holder.fromTime.text = item.fromTime
        holder.toAddress.text = item.toAddress
        holder.toTime.text = item.toTime
        holder.weight.text = item.weight.toString()
    }

    override fun getItemCount(): Int = sendItemList.size


    inner class SendHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerPic : ImageView = itemView.findViewById(R.id.sendItemHeadPic)
        val userName : TextView = itemView.findViewById(R.id.sendItemUserName)
        val userStatus : CircleView = itemView.findViewById(R.id.sendItemUserStatus)
        val userStatusText : TextView = itemView.findViewById(R.id.sendItemUserStatusText)
        val btn : Button = itemView.findViewById(R.id.sendItemContractBtn)
        val fromTime : TextView = itemView.findViewById(R.id.sendItemFromTime)
        val fromAddress : TextView = itemView.findViewById(R.id.sendItemFromAddress)
        val toTime : TextView = itemView.findViewById(R.id.sendItemToTime)
        val toAddress : TextView = itemView.findViewById(R.id.sendItemToAddress)
        val weight : TextView = itemView.findViewById(R.id.sendItemWeight)

        init {
            btn.setOnClickListener(listener)
        }
    }
}