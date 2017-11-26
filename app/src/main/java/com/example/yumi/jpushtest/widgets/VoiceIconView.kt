package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.utils.logV

/**
 * Created by CimZzz(王彦雄) on 11/26/17.<br>
 * Since : 帮帮速递_第一版 <br>
 * Description : <br>
 * 描述
 */
class VoiceIconView(context: Context,attrs : AttributeSet ) : View(context,attrs) {
    val DEFAULT_LINE_WIDTH = 12f

    val rectF = RectF()
    val paint = Paint()

    var index : Int = -1
    var isStartPlaying : Boolean = false

    val color : Int
    val colorActive : Int
    var isLeftSide : Boolean = true

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.VoiceIconView)

        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = arr.getDimension(R.styleable.VoiceIconView_lineWidth,DEFAULT_LINE_WIDTH)
        color = arr.getColor(R.styleable.VoiceIconView_color,Color.WHITE)
        colorActive = arr.getColor(R.styleable.VoiceIconView_colorActive,Color.YELLOW)

        arr.recycle()

    }

    fun startPlaying() {
        if(isStartPlaying)
            return
        index = -1
        isStartPlaying = true
        postInvalidate()
    }

    fun stopPlaying() {
        index = -1
        isStartPlaying = false
        postInvalidate()
    }

    fun setSide(isLeftSide : Boolean) {
        if(this.isLeftSide == isLeftSide)
            return

        this.isLeftSide = isLeftSide
        if(!isStartPlaying)
            postInvalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        index = -1
        isStartPlaying = false
    }

    override fun onDraw(canvas: Canvas) {
        val width = canvas.width * 1.0f
        val height = canvas.height * 1.0f
        val halfH = height / 2
        var startH = height / 3
        val incrementH = startH * 0.5f

        var startW : Float
        var incrementW : Float
        var startAngle : Float
        var sweepAngle : Float


        if(isLeftSide) {
            startW = width / 3
            incrementW = startW * 0.5f
            startAngle = 45f
            sweepAngle = -90f
        } else {
            startW = width / 3 * 2
            incrementW = -width / 3 * 0.5f
            startAngle = 225f
            sweepAngle = -90f
        }
        for(i in 0..2) {
            if(isLeftSide) {
                rectF.right = startW
                rectF.left = startW - startH
            }
            else {
                rectF.left = startW
                rectF.right = startW + startH
            }

            rectF.top = halfH - startH / 2
            rectF.bottom = halfH + startH / 2
            if(isStartPlaying) {
                if(index >= i)
                    paint.color = colorActive
                else paint.color = Color.TRANSPARENT
            }
            else paint.color = color
            canvas.drawArc(rectF,startAngle,sweepAngle,false,paint)

            startH += incrementH
            startW += incrementW
        }

        if(isStartPlaying) {
            index ++
            if(index > 3)
                index = -1
            postInvalidateDelayed(400)
        }
    }
}