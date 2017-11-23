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


fun sp2px(context: Context, spVal: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,

            spVal, context.resources.displayMetrics).toInt()

}