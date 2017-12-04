package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.yumi.jpushtest.R

/**
 * Created by CimZzz(王彦雄) on 2017/11/30.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class CircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    val paint = Paint()
    var color : Int = 0
        set(i) {
            field = i
            postInvalidate()
        }

    init {
        val typeArr = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        color = typeArr.getColor(R.styleable.CircleView_circleColor, Color.BLACK)
        typeArr.recycle()


    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = color
        canvas.drawCircle(canvas.width * 1.0f / 2,canvas.height * 1.0f / 2,canvas.width * 1.0f / 2,paint)
    }
}