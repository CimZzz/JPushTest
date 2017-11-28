package com.example.yumi.jpushtest.utils

import android.content.Context
import android.util.TypedValue



/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
fun dp2px(context : Context,dp : Float) : Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun px2dp(context : Context,px : Int) : Float {
    val density = context.resources.displayMetrics.density
    return (px * 1.0f / density + 0.5f).toInt().toFloat()
}

/**
 * convert px to its equivalent sp
 *
 * 将px转换为sp
 */
fun px2sp(context: Context, pxValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}


/**
 * convert sp to its equivalent px
 *
 * 将sp转换为px
 */
fun sp2px(context: Context, spValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}