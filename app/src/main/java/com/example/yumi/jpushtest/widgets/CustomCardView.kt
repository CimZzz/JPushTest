package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.example.yumi.jpushtest.utils.logV

/**
 * Created by CimZzz(王彦雄) on 2017/11/27.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class CustomCardView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec)
        var maxChildHeight = 0
        val height = MeasureSpec.getSize(heightMeasureSpec)
        (0 until childCount)
                .map { getChildAt(it) }
                .forEach {
                    val tmpHeight = it.height
                    if(tmpHeight > maxChildHeight)
                        maxChildHeight = tmpHeight
                }


        logV("height : ${foreground?.minimumHeight}")
    }
}