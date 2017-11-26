package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.entity.BaseChatItem

/**
 * Created by CimZzz(王彦雄) on 11/25/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */
class ChatStatusView(context : Context,attrs : AttributeSet) : FrameLayout(context, attrs) {

    val loadView : ProgressBar = ProgressBar(context)
    val failedView : ImageView = ImageView(context)

    init {
        val loadViewParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        val failedViewParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)

        failedView.setImageResource(R.drawable.icon_failed)

        addView(loadView,loadViewParams)
        addView(failedView,failedViewParams)
    }


    fun setStatus(status : Int) {
        when(status) {
            BaseChatItem.STATUS_NORMAL-> {
                loadView.visibility = View.GONE
                failedView.visibility = View.GONE
            }
            BaseChatItem.STATUS_LOADING-> {
                loadView.visibility = View.VISIBLE
                failedView.visibility = View.GONE
            }
            BaseChatItem.STATUS_FAILED-> {
                loadView.visibility = View.GONE
                failedView.visibility = View.VISIBLE
            }
        }
    }
}