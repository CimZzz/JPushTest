package com.example.yumi.jpushtest.utils

import java.text.DecimalFormat

/**
 * Created by CimZzz(王彦雄) on 2017/11/22.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

fun isEmptyString(str : String ?) : Boolean = str?.equals("")?:true


fun convert2FileSize(size : Long) : String {
    var divideCount = 0
    var sizeD = size.toDouble()
    while (sizeD >= 1024) {
        sizeD /= 1024
        divideCount ++
    }

    val fmt = DecimalFormat("0.00")

    return when(divideCount) {
        1->"${fmt.format(sizeD)} KB"
        2->"${fmt.format(sizeD)} MB"
        3->"${fmt.format(sizeD)} GB"
        else->"${fmt.format(sizeD)} B"
    }
}

fun convert2TimeStr(second : Int) : String {
    if(second < 60)
        return "$second ''"
    else return "${second / 60} ' ${second % 60} ''"
}