package com.example.yumi.jpushtest.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by CimZzz(王彦雄) on ${Date}.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

var toast : Toast? = null

fun sendToast(context : Context,content : Any?) {
    if(toast != null) {
        toast!!.cancel()
        toast = null
    }

    toast = Toast.makeText(context,content.toString(),Toast.LENGTH_SHORT)
    toast!!.show()
}