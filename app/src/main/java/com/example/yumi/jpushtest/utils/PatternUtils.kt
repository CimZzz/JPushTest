package com.example.yumi.jpushtest.utils

import java.util.regex.Pattern

/**
 * Created by CimZzz(王彦雄) on 2017/12/6.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
private val phoneNumPattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")
private val emailPattern = Pattern.compile("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")

fun isPhoneNum(text : String?) : Boolean {
    if(isEmptyString(text))
        return false

    return phoneNumPattern.matcher(text).matches()
}

fun isEmail(text : String?) : Boolean {
    if(isEmptyString(text))
        return false

    return emailPattern.matcher(text).matches()
}