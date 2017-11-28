package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.utils.px2sp
import com.example.yumi.jpushtest.utils.sp2px

/**
 * Created by CimZzz(王彦雄) on 2017/11/28.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class AddressView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val bigAddress = TextView(context)
    private val smallAddress = TextView(context)

    init {
        val arr = context.obtainStyledAttributes(attrs,R.styleable.AddressView)
        bigAddress.text = arr.getString(R.styleable.AddressView_bigText)
        bigAddress.textSize = px2sp(context,arr.getDimension(R.styleable.AddressView_bigSize,20f)).toFloat()
        bigAddress.setTextColor(arr.getColor(R.styleable.AddressView_bigColor, Color.WHITE))
        
        smallAddress.text = arr.getString(R.styleable.AddressView_smallText)
        smallAddress.textSize = px2sp(context,arr.getDimension(R.styleable.AddressView_smallSize,20f)).toFloat()
        smallAddress.setTextColor(arr.getColor(R.styleable.AddressView_smallColor, Color.WHITE))
        val gap = arr.getDimension(R.styleable.AddressView_midGap,0f)

        arr.recycle()

        bigAddress.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        params.topMargin = gap.toInt()
        smallAddress.layoutParams = params
        orientation = VERTICAL
        gravity = Gravity.CENTER

        addView(bigAddress)
        addView(smallAddress)
    }
}