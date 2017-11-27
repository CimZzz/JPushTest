package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.utils.dp2px
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.utils.sp2px



/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class SliderSwitchView(context : Context, attr : AttributeSet) : View(context,attr) {
    var fontSize : Float
    var fontColor : Int
    var fontColorFocused : Int
    var leftFont : String
    var rightFont : String
    var lineColor : Int
    var lineHeight : Float
    var lineHeightMargin : Float

    val paint : Paint = Paint()
    val rect : Rect = Rect()

    var leftL : Float = 0f
    var leftT : Float = 0f
    var leftR : Float = 0f
    var leftB : Float = 0f
    var leftCenterX: Float = 0f
    var leftCenterY: Float = 0f
    var leftWidth : Float = 0f
    var leftHeight : Float = 0f
    
    var rightL : Float = 0f
    var rightT : Float = 0f
    var rightR : Float = 0f
    var rightB : Float = 0f
    var rightCenterX: Float = 0f
    var rightCenterY: Float = 0f
    var rightWidth : Float = 0f
    var rightHeight : Float = 0f

    var maxWidth : Int = 0
    var maxHeight : Int = 0

    var isLeftActive = true

    var isLeftDown = true
    var isAllowClick = true

    var switchListener : ISwitchListener? = null

    init {
        val arr = context.obtainStyledAttributes(attr, R.styleable.SliderSwitchView)
        fontSize = arr.getDimension(R.styleable.SliderSwitchView_fontSize, sp2px(context,18f).toFloat())
        fontColor = arr.getColor(R.styleable.SliderSwitchView_fontColor, Color.BLACK)
        fontColorFocused = arr.getColor(R.styleable.SliderSwitchView_fontColorFocused, Color.BLACK)
        leftFont = arr.getString(R.styleable.SliderSwitchView_leftFont)
        rightFont = arr.getString(R.styleable.SliderSwitchView_rightFont)
        lineColor = arr.getColor(R.styleable.SliderSwitchView_lineColor, Color.BLACK)
        lineHeight = arr.getDimension(R.styleable.SliderSwitchView_lineHeight, dp2px(context,1f).toFloat())
        lineHeightMargin = arr.getDimension(R.styleable.SliderSwitchView_lineHeightMargin, dp2px(context,1f).toFloat())
        arr.recycle()

        paint.textSize = fontSize
        paint.textAlign = Paint.Align.CENTER
        paint.typeface = Typeface.DEFAULT_BOLD

        isClickable = true
    }

    fun setActiveSide(isLeftActive : Boolean) {
        if(this.isLeftActive == isLeftActive)
            return

        this.isLeftActive = isLeftActive
        postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when(action) {
            MotionEvent.ACTION_DOWN-> {
                val x = event.x
                val y = event.y

                if(isInArea(true,x,y)) {
                    isLeftDown = true
                    isAllowClick = true
                }
                else if(isInArea(false,x,y)) {
                    isLeftDown = false
                    isAllowClick = true
                }
                else return false
            }
            MotionEvent.ACTION_MOVE-> {
                if(!isAllowClick)
                    return false

                val x = event.x
                val y = event.y

                if(!isInArea(isLeftDown,x,y))
                    isAllowClick = false
            }
            MotionEvent.ACTION_UP-> {
                if(!isAllowClick)
                    return true

                if(isLeftDown == isLeftActive)
                    return true

                if(switchListener != null)
                    switchListener!!.onClick(isLeftDown)

                isLeftActive = isLeftDown
                postInvalidate()

            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        if(maxWidth != canvas.width || maxHeight != canvas.height) {
            maxWidth = canvas.width
            maxHeight = canvas.height
            val halfW = canvas.width / 2
            val fontMetrics = paint.fontMetricsInt

            paint.getTextBounds(leftFont,0,leftFont.length,rect)
            leftWidth = rect.width().toFloat()
            leftHeight = rect.height().toFloat()
            leftCenterX = halfW / 2.0f
            leftCenterY = (leftHeight - fontMetrics.bottom - fontMetrics.top) / 2
            leftL = leftCenterX - leftWidth / 2
            leftR = leftCenterX + leftWidth / 2
            leftT = canvas.height / 2 - leftHeight / 2
            leftB = canvas.height / 2 + leftHeight / 2

            paint.getTextBounds(rightFont,0,rightFont.length,rect)
            rightWidth = rect.width().toFloat()
            rightHeight = rect.height().toFloat()
            rightCenterX = halfW * 3 / 2.0f
            rightCenterY = (rightHeight - fontMetrics.bottom - fontMetrics.top) / 2
            rightL = rightCenterX - rightWidth / 2
            rightR = rightCenterX + rightWidth / 2
            rightT = canvas.height / 2 - rightHeight / 2
            rightB = canvas.height / 2 + rightHeight / 2
        }

        canvas.drawARGB(255,255,255,255)
        paint.reset()
        paint.textSize = fontSize
        paint.textAlign = Paint.Align.CENTER
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.strokeCap = Paint.Cap.ROUND


        if(isLeftActive) {
            paint.color = fontColorFocused
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(leftFont, leftCenterX, leftCenterY, paint)
            paint.strokeWidth = lineHeight
            val lineY = (leftCenterY + leftHeight + lineHeight) / 2 + lineHeightMargin
            canvas.drawLine(leftL,lineY, leftR,lineY,paint)

            paint.color = fontColor
            canvas.drawText(rightFont, rightCenterX, rightCenterY, paint)
        }
        else {
            paint.color = fontColorFocused
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(rightFont, rightCenterX, rightCenterY, paint)
            paint.strokeWidth = lineHeight
            val lineY = (rightCenterY + rightHeight + lineHeight) / 2 + lineHeightMargin
            canvas.drawLine(rightL,lineY, rightR,lineY,paint)

            paint.color = fontColor
            canvas.drawText(leftFont, leftCenterX, leftCenterY, paint)
        }
        

    }

    private fun isInArea(isLeft : Boolean , x : Float , y : Float) : Boolean {
        if(isLeft)
            return (x in leftL..leftR && y in leftT..leftB)
        else return (x in rightL..rightR && y in rightT..rightB)
    }

    interface ISwitchListener {
        fun onClick(isLeft: Boolean)
    }
}