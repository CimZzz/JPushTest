package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.utils.dp2px
import com.example.yumi.jpushtest.utils.logV
import com.example.yumi.jpushtest.utils.sp2px

/**
 * Created by CimZzz(王彦雄) on 2017/11/27.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class CustomContainerView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    val bgView = ImageView(context)
    val subLayout = FrameLayout(context)
    var src : Int = -1
    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.CustomContainerView)
        src = arr.getResourceId(R.styleable.CustomContainerView_bg,-1)
        arr.recycle()
        subLayout.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom)
        subLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        subLayout.id = R.id.customContainerId
        bgView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        bgView.scaleType = ImageView.ScaleType.FIT_XY

        if(src != -1)
            bgView.setImageResource(src)
        setPadding(0,0,0,0)

        measureAllChildren = true
        addView(bgView)
        addView(subLayout)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        bgView.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(subLayout.measuredHeight,MeasureSpec.EXACTLY))
    }

    override fun addView(child: View?) {
        if(bgView == child || subLayout == child)
            super.addView(child)
        else subLayout.addView(child)
    }
}