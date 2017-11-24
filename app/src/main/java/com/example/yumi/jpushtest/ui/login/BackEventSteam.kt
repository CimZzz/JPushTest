package com.example.yumi.jpushtest.ui.login

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
class BackEventSteam(val handleMethod : ()->Boolean) {
    var subBackEventSteam : BackEventSteam? = null
    var parentBackEventSteam : BackEventSteam? = null

    fun checkBackEvent() : Boolean {
        return if(subBackEventSteam != null)
            subBackEventSteam!!.checkBackEvent()
        else handleMethod.invoke()
    }

    fun clear() {
        subBackEventSteam = null
        if(parentBackEventSteam != null)
            parentBackEventSteam!!.subBackEventSteam = null
        parentBackEventSteam = null
    }

    fun addEvent(eventSteam: BackEventSteam?) {
        subBackEventSteam = eventSteam
        if(eventSteam != null)
            eventSteam.parentBackEventSteam = this
    }
}