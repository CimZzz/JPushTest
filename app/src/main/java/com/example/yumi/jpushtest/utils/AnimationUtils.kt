package com.example.yumi.jpushtest.utils

import android.support.graphics.drawable.AnimationUtilsCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

fun startAnimation(view: View,id:Int) {
    view.clearAnimation()
    view.startAnimation(AnimationUtils.loadAnimation(view.context,id))
}