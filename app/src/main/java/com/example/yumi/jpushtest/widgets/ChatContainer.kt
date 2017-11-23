package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.yumi.jpushtest.utils.dp2px
import com.example.yumi.jpushtest.utils.px2dp

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ChatContainer(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val path : Path
    private val paint : Paint
    private val triStartY : Float
    private val triTopY : Float
    private val triBotY : Float
    private val triEndX : Float
    private val radius: Float
    private val dp5 : Int = dp2px(context,5f)
    private var leftSide : Boolean = true

    init {
        triStartY = (dp5 * 3).toFloat()
        triTopY = (dp5 * 2).toFloat()
        triBotY = (dp5 * 4).toFloat()
        triEndX = (dp5 * 2).toFloat()
        radius = dp5.toFloat()
        path = Path()
        paint = Paint()
        paint.color = Color.GREEN
        setPadding((triEndX + dp5).toInt(),dp5,dp5,dp5)
        minimumWidth = dp2px(context,80f)
        minimumHeight = dp2px(context,40f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthDp = px2dp(context,width)
        if(widthDp > 200) {
            val resizeWidth = dp2px(context,200f)
            super.onMeasure(MeasureSpec.makeMeasureSpec(resizeWidth,MeasureSpec.AT_MOST), heightMeasureSpec)
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawARGB(0,255,255,255)
        path.rewind()

        val widthF = width.toFloat()
        val heightF = height.toFloat()

        if(leftSide) {
            path.moveTo(0f, triStartY)
            path.lineTo(triEndX, triTopY)
            path.lineTo(triEndX, triBotY)
            path.close()
            path.addRoundRect(triEndX, 0f, widthF, heightF, radius, radius, Path.Direction.CCW)
        } else {
            path.moveTo(widthF, triStartY)
            path.lineTo(widthF - triEndX, triTopY)
            path.lineTo(widthF - triEndX, triBotY)
            path.close()
            path.addRoundRect(0f, 0f, widthF - triEndX, height.toFloat(), radius, radius, Path.Direction.CCW)
        }
        canvas.drawPath(path,paint)
    }

    fun setSide(isLeftSide : Boolean) {
        leftSide = isLeftSide
        if(leftSide) {
            setPadding((triEndX + dp5).toInt(), dp5, dp5, dp5)
        } else {
            setPadding(dp5,dp5,(triEndX+dp5).toInt(),dp5)
        }
        postInvalidate()
    }
}