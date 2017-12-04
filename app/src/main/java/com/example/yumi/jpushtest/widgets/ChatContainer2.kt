package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.utils.dp2px
import com.example.yumi.jpushtest.utils.px2dp

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class ChatContainer2(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val rectf : RectF = RectF()
    private val path : Path = Path()
    private val paint : Paint = Paint()

    private val dp10 = dp2px(context,20f).toFloat()
    private val oppositeColor : Int = context.resources.getColor(R.color.chat_ChatContainerOppositeColor)
    private val ownColor : Int = context.resources.getColor(R.color.chat_ChatContainerOwnColor)
    private val paddingSide : Int = context.resources.getDimensionPixelSize(R.dimen.chat_ChatContainerPaddingSide)
    private val paddingBox : Int = context.resources.getDimensionPixelSize(R.dimen.chat_ChatContainerPaddingBox)

    var isOwn = true

    init {

        setPadding((dp10 + paddingSide).toInt(), paddingBox , paddingSide , paddingBox)
        minimumWidth = dp2px(context,80f)
        minimumHeight = dp2px(context,40f)
        dividerDrawable = ColorDrawable(Color.TRANSPARENT)
    }

    fun setSide(isOwn : Boolean) {
        if(this.isOwn == isOwn)
            return
        this.isOwn = isOwn
        if(isOwn)
            setPadding((dp10 + paddingSide).toInt(), paddingBox , paddingSide , paddingBox)
        else setPadding(paddingSide, paddingBox ,(dp10 + paddingSide).toInt(),paddingBox)
        postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthDp = px2dp(context,width)
        if(widthDp > 200) {
            val resizeWidth = dp2px(context,250f)
            super.onMeasure(MeasureSpec.makeMeasureSpec(resizeWidth,MeasureSpec.AT_MOST), heightMeasureSpec)
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }


    override fun onDraw(canvas: Canvas) {

        path.rewind()

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        if(isOwn) {
            path.moveTo(0f, height)
            path.arcTo(-dp10, height - dp10 - dp10, dp10, height, 90f, -90f, true)
            path.lineTo(dp10 * 2, height)
            path.close()
            rectf.left = dp10
            rectf.right = width
            rectf.top = 0f
            rectf.bottom = height
            path.addRoundRect(rectf, dp10 / 2, dp10 / 2, Path.Direction.CW)
            paint.color = oppositeColor
        }
        else {
            path.moveTo(width, height)
            path.arcTo(width-dp10, height - dp10 - dp10, width + dp10, height, 180f, -90f, true)
            path.lineTo(dp10 * 2, height)
            path.close()
            rectf.left = 0f
            rectf.right = width - dp10
            rectf.top = 0f
            rectf.bottom = height
            path.addRoundRect(rectf, dp10 / 2, dp10 / 2, Path.Direction.CW)
            paint.color = ownColor
        }
        canvas.drawPath(path, paint)
    }
}