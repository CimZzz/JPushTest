package com.example.yumi.jpushtest.entity

/**
 * Created by CimZzz(王彦雄) on 2017/11/30.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */

data class SendItem(
        val headerPic : String,
        val userName : String,
        val userStatus : Int,
        val fromTime : String,
        val fromAddress : String,
        val toTime : String,
        val toAddress : String,
        val weight : Float
) {
    companion object {
        const val ONLINE = 0
        const val OFFLINE = 1
    }
}