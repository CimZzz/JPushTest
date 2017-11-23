package com.example.yumi.jpushtest.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.yumi.jpushtest.R
import com.example.yumi.jpushtest.utils.dp2px
import com.example.yumi.jpushtest.utils.sp2px

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

class IconTextView : LinearLayout {

    private val imageView : ImageView = ImageView(context)
    private val textView : TextView = TextView(context)

    constructor(context: Context) : super(context) {
        val imgLparams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1f)
        val textLparams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)

        imageView.layoutParams = imgLparams

        textView.layoutParams = textLparams

        orientation = VERTICAL
        gravity = Gravity.CENTER
        addView(imageView)
        addView(textView)
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val imgLparams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1f)
        val textLparams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)

        val styleArr = context.obtainStyledAttributes(attrs, R.styleable.IconTextView)
        val src = styleArr.getDrawable(R.styleable.IconTextView_src)
        val imgPadding = styleArr.getDimension(R.styleable.IconTextView_imagePadding,0f).toInt()
        val text = styleArr.getText(R.styleable.IconTextView_text)
        val textColor = styleArr.getColor(R.styleable.IconTextView_textColor, Color.BLACK)

        imageView.layoutParams = imgLparams
        imageView.setImageDrawable(src)
        imageView.setPadding(imgPadding,imgPadding,imgPadding,imgPadding)

        textView.layoutParams = textLparams
        textView.text = text
        textView.setTextColor(textColor)

        styleArr.recycle()

        orientation = VERTICAL
        gravity = Gravity.CENTER
        addView(imageView)
        addView(textView)
    }


    fun setImage(resId : Int) {
        imageView.setImageResource(resId)
    }

    fun setText(text : String) {
        textView.text = text
    }

    fun setTextColor(color : Int) {
        textView.setTextColor(color)
    }

    fun setTextSize(sp : Float) {
        textView.textSize = sp2px(context,sp).toFloat()
    }

    fun setImageViewSize(dp : Float) {
        val params = imageView.layoutParams
        params.width = dp2px(context,dp)
        params.height = dp2px(context,dp)
        imageView.layoutParams = params
    }
}