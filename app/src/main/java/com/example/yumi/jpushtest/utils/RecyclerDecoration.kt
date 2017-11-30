package com.example.yumi.jpushtest.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by CimZzz(王彦雄) on 2017/11/28.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class RecyclerDecoration(context : Context, resId : Int) : RecyclerView.ItemDecoration() {
    private var mDivider : Drawable? = null


    init {
        mDivider = context.resources.getDrawable(resId,null)
    }

    @Override
    override fun onDraw(c : Canvas, parent : RecyclerView, state: RecyclerView.State) {
        drawHorizontalLine(c, parent,state)
    }

//画横线, 这里的parent其实是显示在屏幕显示的这部分
    private fun drawHorizontalLine(c : Canvas, parent : RecyclerView, state: RecyclerView.State){
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        (0 until childCount).map {
            parent.getChildAt(it)
        }.forEach {
            val params = it.layoutParams as RecyclerView.LayoutParams
            val top = it.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider!!.setBounds(left,top,right,bottom)
            mDivider!!.draw(c)
        }
    }


    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    override fun getItemOffsets(outRect: Rect, view: View, parent:RecyclerView, state:RecyclerView.State) {
        outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
    }
}